package pl.miloszgilga.jargonaut.core.parser.md;

import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.ext.gitlab.GitLabInlineMath;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;

import org.jetbrains.annotations.Nullable;

import pl.miloszgilga.jargonaut.core.polyglot.MathRenderer;
import pl.miloszgilga.jargonaut.core.polyglot.SyntaxHighlighter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomNodeRenderer implements NodeRenderer {
  private final SyntaxHighlighter highlighter;
  private final MathRenderer mathRenderer;

  public CustomNodeRenderer(SyntaxHighlighter highlighter, MathRenderer mathRenderer) {
    this.highlighter = highlighter;
    this.mathRenderer = mathRenderer;
  }

  @Override
  public @Nullable Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
    return new HashSet<>(List.of(
      new NodeRenderingHandler<>(FencedCodeBlock.class, this::renderBlock),
      new NodeRenderingHandler<>(GitLabInlineMath.class, this::renderMathInline)
    ));
  }

  private void renderBlock(FencedCodeBlock node, NodeRendererContext context, HtmlWriter html) {
    final String language = node.getInfo().toString().trim();
    final String code = node.getContentChars().normalizeEOL();
    if ("math".equalsIgnoreCase(language)) {
      final String mathHtml = mathRenderer.renderMath(code, true);
      html.line().append(mathHtml).line();
      return;
    }
    final String highlightedCode = highlighter.renderHighlight(code, language);
    html.line()
      .withAttr().tag("pre").openPre()
      .attr("class", "hljs language-" + language).withAttr().tag("code")
      .raw(highlightedCode)
      .closeTag("code")
      .closeTag("pre").closePre()
      .line();
  }

  private void renderMathInline(GitLabInlineMath node, NodeRendererContext context, HtmlWriter html) {
    final String tex = node.getText().toString();
    final String mathHtml = mathRenderer.renderMath(tex, false);
    html.raw(mathHtml);
  }
}
