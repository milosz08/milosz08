/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg.node;

import org.jsoup.nodes.Element;
import pl.miloszgilga.ssg.FileReader;
import pl.miloszgilga.ssg.i18n.I18n;

import java.util.Map;

public class ProjectsGalleryNodeGenerator extends AbstractNodeGenerator {
	private static final String IMAGES_ABS_URL = "/images/%s/%d.jpg";

	private final I18n i18n;

	public ProjectsGalleryNodeGenerator(I18n i18n) {
		super();
		this.i18n = i18n;
	}

	@Override
	protected WebDocument prepareContent(WebDocument webDocument, Map<String, String> args) {
		final String projectSlug = args.get("fileNameWithoutExt");
		final FileReader fileReader = new FileReader("public/images/" + projectSlug);
		final int imagesCount = fileReader.getCountOfFilesInPath();
		if (imagesCount == 0) {
			return webDocument;
		}
		final Element container = webDocument.createClassNode("div", "gallery__container");
		for (int i = 1; i <= imagesCount; i++) {
			final String tag = createImageTag(i);
			final String url = String.format(IMAGES_ABS_URL, projectSlug, i);

			final Element anchor = webDocument.createAnchorNode(createImageId(i), "gallery__image-link");
			final Element image = webDocument.createImageNode(url, tag, "gallery__image");
			anchor.appendChild(image);

			final Element previewContainer = webDocument.createClassWithIdNode("div", tag, "gallery__preview-container");
			final Element previewWrapper = webDocument.createClassNode("div", "gallery__preview-image-container");

			final Element navigationEndContainer = webDocument
				.createClassNode("div", "gallery__preview-navigation gallery__preview-navigation--end");

			final Element closeBtn = webDocument.createAnchorNode("#", "gallery__preview-navigation-link");
			closeBtn.text(i18n.t("gallery.close"));
			navigationEndContainer.appendChild(closeBtn);

			final Element previewImage = webDocument.createImageNode(url, tag, "gallery__preview-image");
			navigationEndContainer.appendChild(previewImage);

			final Element navigationContainer = webDocument.createClassNode("div", "gallery__preview-navigation");
			final Element prevBtn = createNavigationBtn(webDocument, i - 1, i == 1, "gallery.prev");
			final Element nextBtn = createNavigationBtn(webDocument, i + 1, i == imagesCount, "gallery.next");

			navigationContainer.appendChild(prevBtn);
			navigationContainer.appendChild(nextBtn);

			previewWrapper.appendChild(navigationEndContainer);
			previewWrapper.appendChild(previewImage);
			previewWrapper.appendChild(navigationContainer);

			previewContainer.appendChild(previewWrapper);

			container.appendChild(anchor);
			container.appendChild(previewContainer);
		}
		webDocument.appendChild(container);
		return webDocument;
	}

	@Override
	public String injectorContentName() {
		return "projectGallery";
	}

	private String createImageId(int index) {
		return "#" + createImageTag(index);
	}

	private String createImageTag(int index) {
		return String.format("image-%d", index);
	}

	private Element createNavigationBtn(WebDocument webDocument, int index, boolean visibleCond, String i18nKey) {
		final Element btn = webDocument.createAnchorNode(createImageId(index), "gallery__preview-navigation-link", Map.of(
			"gallery__preview-navigation-link--visible-off", visibleCond
		));
		btn.text(i18n.t(i18nKey));
		return btn;
	}
}
