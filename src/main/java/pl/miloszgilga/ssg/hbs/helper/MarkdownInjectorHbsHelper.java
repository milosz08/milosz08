package pl.miloszgilga.ssg.hbs.helper;

import pl.miloszgilga.ssg.i18n.I18n;
import pl.miloszgilga.ssg.md.BadgeParserMdHelper;
import pl.miloszgilga.ssg.md.HtmlInjectorMdHelper;
import pl.miloszgilga.ssg.md.MarkdownRenderer;

public class MarkdownInjectorHbsHelper extends AbstractHbsHelperBase {
	private final MarkdownRenderer markdownRenderer;

	public MarkdownInjectorHbsHelper(I18n i18n) {
		markdownRenderer = new MarkdownRenderer(i18n);
		markdownRenderer.registerHelper(new BadgeParserMdHelper());
		markdownRenderer.registerHelper(new HtmlInjectorMdHelper(i18n));
	}

	@Override
	String inject(String args) {
		return markdownRenderer.render(args);
	}

	@Override
	public String helperName() {
		return "md";
	}
}
