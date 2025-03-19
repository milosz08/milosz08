package pl.miloszgilga.ssg.md;

import org.apache.commons.lang3.StringUtils;
import pl.miloszgilga.ssg.AbstractFrontmatterReader;
import pl.miloszgilga.ssg.i18n.I18n;
import pl.miloszgilga.ssg.node.AbstractNodeGenerator;
import pl.miloszgilga.ssg.node.ProjectsGalleryNodeGenerator;

import java.util.HashMap;
import java.util.Map;

public class HtmlInjectorMdHelper implements MdHelperBase {
	private final Map<String, AbstractNodeGenerator> nodeGenerators;

	public HtmlInjectorMdHelper(I18n i18n) {
		nodeGenerators = new HashMap<>();
		registerNodeGenerator(new ProjectsGalleryNodeGenerator(i18n));
	}

	@Override
	public String inject(String args, AbstractFrontmatterReader reader) {
		String renderedContent = StringUtils.EMPTY;
		for (final Map.Entry<String, AbstractNodeGenerator> generator : nodeGenerators.entrySet()) {
			if (generator.getKey().equals(args)) {
				renderedContent = generator.getValue().generate(reader.getSections());
			}
		}
		return renderedContent;
	}

	@Override
	public boolean injectBeforeHtmlProcessing() {
		return false;
	}

	@Override
	public String helperName() {
		return "html";
	}

	private void registerNodeGenerator(AbstractNodeGenerator nodeGenerator) {
		nodeGenerators.put(nodeGenerator.injectorContentName(), nodeGenerator);
	}
}
