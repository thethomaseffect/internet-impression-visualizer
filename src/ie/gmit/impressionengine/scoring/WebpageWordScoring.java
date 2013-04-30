package ie.gmit.impressionengine.scoring;

import ie.gmit.impressionengine.scoring.rules.IsAdjectiveRule;
import ie.gmit.impressionengine.scoring.rules.IsStopWordRule;
import ie.gmit.impressionengine.scoring.rules.LocalSearchTermRule;
import ie.gmit.impressionengine.scoring.rules.RuleScalers;

import java.util.List;
import java.util.Set;

/**
 * Provides scoring methods for the words on a webpage.
 * 
 */
public class WebpageWordScoring {

	private static IsAdjectiveRule isAdjective;

	private static IsStopWordRule isStopWord;

	public WebpageWordScoring(final Set<String> stopwordSet,
			final Set<String> adjectiveSet) {
		WebpageWordScoring.isAdjective = new IsAdjectiveRule(stopwordSet);
		WebpageWordScoring.isStopWord = new IsStopWordRule(adjectiveSet);
	}

	/**
	 * <p>
	 * Returns a score for the provided word.
	 * </p>
	 * <p>
	 * If a word is in the list of adjectives, it is scored HIGH. If a word is
	 * close to the search term, it is scored HIGH.
	 * </p>
	 * 
	 * @param word
	 *            The word being scored.
	 * @param wordIndex
	 *            The index of the word being scored.
	 * @param searchTermIndexes
	 *            The indexes of all occurrences of the search term in the word
	 *            list.
	 * @return A positive integer representing the score of the word.
	 */
	public int getWordScore(final String word, final int wordIndex,
			final List<Integer> searchTermIndexes) {
		final LocalSearchTermRule localSearchTerm = new LocalSearchTermRule(
				searchTermIndexes);
		// If it's a stopword give it a score of zero
		if (isStopWord.apply(word) > 0) {
			return 0;
		}
		// The closer an adjective is to the search term the higher the score
		return (int) (isAdjective.apply(word) * RuleScalers.IS_ADJECTIVE_SCALER * (localSearchTerm
				.apply(wordIndex) * RuleScalers.LOCAL_SEARCH_TERM_SCALER + 1));

	}
}
