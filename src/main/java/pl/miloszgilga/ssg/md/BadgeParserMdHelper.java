package pl.miloszgilga.ssg.md;

import org.apache.hc.core5.net.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.miloszgilga.ssg.AbstractFrontmatterReader;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class BadgeParserMdHelper implements MdHelperBase {
	private static final Logger LOG = LoggerFactory.getLogger(BadgeParserMdHelper.class);

	private static final String BASE_URL = "https://img.shields.io/badge";
	private static final int EXPECTED_ARGS_COUNT = 4;
	private static final String BADGE_DATA_DELIMITER = "\\$";

	@Override
	public String inject(String args, AbstractFrontmatterReader reader) throws Exception {
		final String[] badgeData = args.split(BADGE_DATA_DELIMITER);
		if (badgeData.length != EXPECTED_ARGS_COUNT) {
			throw new IOException(String.format(
				"Unable to parse badge arguments for: %s. Found: %s arguments, expected: %s.",
				args, badgeData.length, EXPECTED_ARGS_COUNT
			));
		}
		final String text = badgeData[0];
		final String logo = badgeData[1];
		final String color = badgeData[2];
		final String onClikHref = badgeData[3];
		final String badge = wrapWithMarkdownTag(buildBadgeUrl(text, logo, color), onClikHref);
		LOG.debug("Generated badge: {}.", badge);
		return badge;
	}

	@Override
	public boolean injectBeforeHtmlProcessing() {
		return true;
	}

	@Override
	public String helperName() {
		return "badge";
	}

	private URI buildBadgeUrl(String text, String name, String color) throws URISyntaxException {
		final URIBuilder uri = new URIBuilder(String.format("%s/%s-%s", BASE_URL, text, color).replace(" ", "%20"));
		uri.addParameter("style", "for-the-badge");
		uri.addParameter("logo", name);
		uri.addParameter("logoColor", "white");
		return uri.build();
	}

	private String wrapWithMarkdownTag(URI badgeUri, String onClikHref) {
		return String.format("[![](%s)](%s) &nbsp;", badgeUri, onClikHref);
	}
}
