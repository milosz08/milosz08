package pl.miloszgilga.ssg.page;

public record ProcesssedPage(
	String qualifiedName,
	String rawHtmlContent
) {
	@Override
	public String toString() {
		return qualifiedName;
	}
}
