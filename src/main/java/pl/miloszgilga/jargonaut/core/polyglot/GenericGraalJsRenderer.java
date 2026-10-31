package pl.miloszgilga.jargonaut.core.polyglot;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import pl.miloszgilga.jargonaut.core.GenerationHaltedException;
import pl.miloszgilga.jargonaut.core.SilentCloseable;
import pl.miloszgilga.jargonaut.core.event.BuildEvent;
import pl.miloszgilga.jargonaut.core.event.BuildEventBus;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class GenericGraalJsRenderer implements SilentCloseable {
  protected final SiteBuildContext context;
  protected final BuildEventBus eventBus;
  protected final Context graalVmContext;
  protected final Value functionValue;

  static {
    System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
    System.setProperty("polyglotimpl.DisableVersionChecks", "true");
  }

  protected GenericGraalJsRenderer(String libUrl,
                                   SiteBuildContext context,
                                   BuildEventBus eventBus,
                                   String wrapperResourcePath) {
    this.context = context;
    this.eventBus = eventBus;
    graalVmContext = Context.create("js");
    try {
      loadExternalLibrary(libUrl);
      functionValue = loadWrapperScript(wrapperResourcePath);
    } catch (Exception ex) {
      graalVmContext.close();
      throw new GenerationHaltedException(ex);
    }
  }

  @Override
  public void close() {
    if (graalVmContext != null) {
      graalVmContext.close();
    }
  }

  private void loadExternalLibrary(String libUrl) throws Exception {
    final Path cacheDir = Path.of(context.getConfig().getCache().getCacheDir(), "__js");
    if (!Files.exists(cacheDir)) {
      Files.createDirectories(cacheDir);
    }
    final String fileName = UUID.nameUUIDFromBytes(libUrl.getBytes(StandardCharsets.UTF_8)) + ".js";
    final Path cachedFile = cacheDir.resolve(fileName);
    final String scriptContent;
    if (Files.exists(cachedFile)) {
      eventBus.publish(BuildEvent.spinnerDetail("Loading library from cache: " + libUrl));
      scriptContent = Files.readString(cachedFile, StandardCharsets.UTF_8);
    } else {
      eventBus.publish(BuildEvent.spinnerDetail("Downloading library from URL: " + libUrl));
      try (final HttpClient client = HttpClient.newHttpClient()) {
        final HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(libUrl))
          .GET()
          .build();
        final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
          throw new GenerationHaltedException("Unable to download lib: " + libUrl + ", status: " +
            response.statusCode());
        }
        scriptContent = response.body();
        Files.writeString(cachedFile, scriptContent, StandardCharsets.UTF_8);
      }
    }
    graalVmContext.eval("js", scriptContent);
  }

  private Value loadWrapperScript(String wrapperResourcePath) throws Exception {
    try (final InputStream inputStream = getClass().getResourceAsStream(wrapperResourcePath)) {
      if (inputStream == null) {
        throw new GenerationHaltedException("Unable to find file: " + wrapperResourcePath + " in classpath");
      }
      final String wrapperCode = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
      return graalVmContext.eval("js", wrapperCode);
    }
  }
}
