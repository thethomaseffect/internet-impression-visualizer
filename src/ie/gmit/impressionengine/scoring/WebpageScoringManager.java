package ie.gmit.impressionengine.scoring;

import ie.gmit.impressionengine.crawler.ComparableLink;
import ie.gmit.impressionengine.crawler.WebpageLink;
import ie.gmit.impressionengine.scoring.links.PreviouslyVisitedURLs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class WebpageScoringManager {

	/*
	 * Take a HTML page, get text and use for word scoring. Get links and use
	 * for link scoring. Use a list sorted by a reverse comparator to get top 5
	 * or any links. For words, set a minimum target score and return a list
	 * (WITH DUPLICATES) of of all words that meet the min.
	 */

	private static final Comparator<ComparableLink> largestFirstComparator = Collections
			.reverseOrder(null);
	private final WebpageLinkScoring linkScoring;
	private final WebpageWordScoring wordScoring;

	private final List<WebpageLink> pageLinks;
	private final int PARENT_DEPTH;
	private final PreviouslyVisitedURLs prevVisitedURLs;
	private final String SEARCH_TERM;
	private final List<String> webpageText;
	private final List<Integer> searchTermIndexes;

	public WebpageScoringManager(final String WEB_PAGE,
			final String SEARCH_TERM,
			final PreviouslyVisitedURLs prevVisitedURLs,
			final Set<String> adjectiveSet, final Set<String> stopwordsSet,
			final int PARENT_DEPTH) {
		this.SEARCH_TERM = SEARCH_TERM;
		this.prevVisitedURLs = prevVisitedURLs;
		this.pageLinks = WebpageDataExtractor.getWebpageLinks(WEB_PAGE);
		this.webpageText = ScoringUtilities.getPageWordsAsList(WEB_PAGE);
		this.linkScoring = new WebpageLinkScoring(SEARCH_TERM);
		this.wordScoring = new WebpageWordScoring(adjectiveSet, stopwordsSet);
		this.PARENT_DEPTH = PARENT_DEPTH;
		this.searchTermIndexes = ScoringUtilities
				.getSearchTermIndexLocationsOrNull(this.webpageText,
						this.SEARCH_TERM);
	}

	public List<String> getScoredWords(final int MINIMUM_WORD_SCORE) {
		List<String> minScoreBreatingWords = new ArrayList<String>();
		for (int i = 0; i < webpageText.size(); i++) {
			String currentWord = webpageText.get(i);
			if (wordScoring.getWordScore(currentWord, i, searchTermIndexes) > MINIMUM_WORD_SCORE) {
				minScoreBreatingWords.add(currentWord);
			}
		}
		return minScoreBreatingWords;
	}

	public List<ComparableLink> returnTopLinks(final int TOP_LINK_AMT) {
		List<ComparableLink> scoredLinks = new ArrayList<ComparableLink>();
		for (WebpageLink link : pageLinks) {
			scoredLinks.add(link.toComparableLink(linkScoring.getLinkScore(
					link, prevVisitedURLs, SEARCH_TERM), PARENT_DEPTH + 1));
		}
		// Sort the Scored Links
		Collections.sort(scoredLinks, largestFirstComparator);
		// If list was smaller than TOP_LINK_AMT return it
		if (scoredLinks.size() < TOP_LINK_AMT) {
			return scoredLinks;
		}
		List<ComparableLink> topLinks = new ArrayList<ComparableLink>(
				TOP_LINK_AMT);
		// Build a list of the top scored links
		for (int i = 0; i < TOP_LINK_AMT; i++) {
			topLinks.add(scoredLinks.get(i));
		}
		return topLinks;
	}
}
