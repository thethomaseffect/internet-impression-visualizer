package ie.gmit.impressionengine.scoring.rules;

import ie.gmit.impressionengine.scoring.ScoringUtilities;

public class LinkTextContainsSearchTermRule implements IRule<String> {

	private final String[] TOKANIZED_SEARCH_TERM;
	private final int TOKANIZED_WORDS_COUNT;

	public LinkTextContainsSearchTermRule(String searchTerm) {
		searchTerm = searchTerm.toLowerCase();
		TOKANIZED_SEARCH_TERM = ScoringUtilities
				.getTokanizedSearchTerm(searchTerm);
		TOKANIZED_WORDS_COUNT = TOKANIZED_SEARCH_TERM.length;
	}

	@Override
	public float apply(final String linkText) {
		int tokensFoundCount = 0;
		for (String token : TOKANIZED_SEARCH_TERM) {
			if (linkText.contains(token)) {
				tokensFoundCount++;
			}
		}
		// INFO: If no tokens are found, the result of division will always
		// equal zero
		return (float) tokensFoundCount / TOKANIZED_WORDS_COUNT;
	}

}
