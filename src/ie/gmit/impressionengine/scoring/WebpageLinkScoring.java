package ie.gmit.impressionengine.scoring;

import static com.google.common.base.Preconditions.checkNotNull;
import ie.gmit.impressionengine.crawler.WebpageLink;
import ie.gmit.impressionengine.scoring.links.PreviouslyVisitedURLs;
import ie.gmit.impressionengine.scoring.rules.LinkContainsSearchTermRule;
import ie.gmit.impressionengine.scoring.rules.LinkTextContainsSearchTermRule;
import ie.gmit.impressionengine.scoring.rules.RuleScalers;

public class WebpageLinkScoring {

	private String SEARCH_TERM = "TEST TERM";

	private final LinkContainsSearchTermRule linkContainsSearchTerm = new LinkContainsSearchTermRule(
			SEARCH_TERM);
	private final LinkTextContainsSearchTermRule linkTextContainsSearchTerm = new LinkTextContainsSearchTermRule(
			SEARCH_TERM);

	public WebpageLinkScoring(final String SEARCH_TERM) {
		checkNotNull(SEARCH_TERM, "Error: Search Term cannot be null");
		this.SEARCH_TERM = SEARCH_TERM;
	}

	/*
	 * Some Basics:
	 * 
	 * If link has same hostname as a previously visited hostname it gets scored
	 * DOWN. If link contains the search term it gets scored UP.
	 */

	public int getLinkScore(final WebpageLink link,
			final PreviouslyVisitedURLs prevVisitedURL, final String searchTerm) {
		String url = link.getUrl();
		if (!url.contains("http://")) {
			return 0;
		}
		// If Link has been previously visited return 0
		if (prevVisitedURL.isPreviouslyVisitedURL(link.getUrl())) {
			return 0;
		}
		float hostnameScore;
		if (prevVisitedURL.isPreviouslyVisitedHostname(link.getUrl())) {
			hostnameScore = 1;
		} else {
			hostnameScore = 0;
		}
		float linkContainsSearchtermScore = linkContainsSearchTerm.apply(link
				.getUrl());

		float linkTextContainsSearchTermScore = linkTextContainsSearchTerm
				.apply(link.getLinkText());

		return (int) (hostnameScore
				* RuleScalers.PREVIOUSLY_VISITED_HOSTNAME_SCALER
				+ linkContainsSearchtermScore
				* RuleScalers.LINK_CONTAINS_SEARCH_TERM_SCALER + linkTextContainsSearchTermScore
				* RuleScalers.LINK_TEXT_CONTAINS_SEARCH_TERM_SCALER);
	}
}
