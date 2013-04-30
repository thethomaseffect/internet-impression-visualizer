package ie.gmit.impressionengine.crawler;

public class WebpageLink {

	private final String url;
	private final String linkText;

	public WebpageLink(final String url, final String linkText) {
		this.url = url;
		this.linkText = linkText;
	}

	public String getLinkText() {
		return linkText;
	}

	public String getUrl() {
		return url;
	}

	/**
	 * Converts the <code>WebpageLink</code> to a <code>ComparableLink</code> to
	 * allow insertion into the links queue.
	 * 
	 * @param score
	 * @param depth
	 * @return
	 */
	public final ComparableLink toComparableLink(final int score,
			final int depth) {
		return new ComparableLink(score, this.url, depth);
	}

}
