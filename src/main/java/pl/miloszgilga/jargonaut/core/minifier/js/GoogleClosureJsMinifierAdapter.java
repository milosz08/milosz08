package pl.miloszgilga.jargonaut.core.minifier.js;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;

import com.google.javascript.jscomp.CompilerOptions;

import com.google.javascript.jscomp.Result;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.WarningLevel;

import pl.miloszgilga.jargonaut.core.GenerationHaltedException;
import pl.miloszgilga.jargonaut.core.event.BuildEventBus;
import pl.miloszgilga.jargonaut.core.minifier.GenericMinifier;

import java.util.List;
import java.util.logging.Level;

public class GoogleClosureJsMinifierAdapter extends GenericMinifier implements JsMinifier {
  static {
    Compiler.setLoggingLevel(Level.OFF);
  }

  public GoogleClosureJsMinifierAdapter(BuildEventBus eventBus) {
    super(eventBus);
  }

  @Override
  public String minify(String rawContent) {
    final Compiler compiler = new Compiler();
    final CompilerOptions options = new CompilerOptions();

    CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);
    WarningLevel.QUIET.setOptionsForWarningLevel(options);

    options.setLanguageIn(CompilerOptions.LanguageMode.ECMASCRIPT_2020);
    options.setLanguageOut(CompilerOptions.LanguageMode.ECMASCRIPT_2020);
    options.setEmitUseStrict(true);

    final String wrappedContent = "(function() {\n" + rawContent + "\n})();";
    final SourceFile extern = SourceFile.fromCode("externs.js", "");
    final SourceFile input = SourceFile.fromCode("input.js", wrappedContent);

    final Result result = compiler.compile(List.of(extern), List.of(input), options);
    if (!result.success) {
      throw new GenerationHaltedException("JS compilation failed: " + result.errors);
    }
    return compiler.toSource();
  }
}
