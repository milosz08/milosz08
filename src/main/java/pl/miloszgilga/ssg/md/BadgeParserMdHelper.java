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
		final String onClickHref = badgeData[3];
		final String badge = wrapWithMarkdownTag(buildBadgeUrl(text, logo, color), onClickHref);
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
		uri.addParameter("logoColor", calcBadgeLogoColorBaseBackground(color));
		return uri.build();
	}

	private String wrapWithMarkdownTag(URI badgeUri, String onClickHref) {
		return String.format("[![](%s)](%s) &nbsp;", badgeUri, onClickHref);
	}

	private String calcBadgeLogoColorBaseBackground(String bgColor) {
		if (bgColor.startsWith("#")) {
			bgColor = bgColor.substring(1);
		}
		final int r = Integer.parseInt(bgColor.substring(0, 2), 16);
		final int g = Integer.parseInt(bgColor.substring(2, 4), 16);
		final int b = Integer.parseInt(bgColor.substring(4, 6), 16);

		final double luminance = (0.299 * r + 0.587 * g + 0.114 * b);
		return luminance > 186 ? "000000" : "FFFFFF";
	}
}
