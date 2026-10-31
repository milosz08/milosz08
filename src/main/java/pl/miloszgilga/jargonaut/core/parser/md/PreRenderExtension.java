package pl.miloszgilga.jargonaut.core.parser.md;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.util.data.MutableDataHolder;

import org.jetbrains.annotations.NotNull;

import pl.miloszgilga.jargonaut.core.polyglot.MathRenderer;
import pl.miloszgilga.jargonaut.core.polyglot.SyntaxHighlighter;

public class PreRenderExtension implements HtmlRenderer.HtmlRendererExtension {
  private final SyntaxHighlighter highlighter;
  private final MathRenderer mathRenderer;

  public PreRenderExtension(SyntaxHighlighter highlighter, MathRenderer mathRenderer) {
    this.highlighter = highlighter;
    this.mathRenderer = mathRenderer;
  }

  @Override
  public void rendererOptions(@NotNull MutableDataHolder options) {
  }

  @Override
  public void extend(HtmlRenderer.@NotNull Builder htmlRendererBuilder, @NotNull String rendererType) {
    htmlRendererBuilder.nodeRendererFactory(options -> new CustomNodeRenderer(highlighter, mathRenderer));
  }
}
