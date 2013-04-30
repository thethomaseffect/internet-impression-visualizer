package ie.gmit.impressionengine.scoring.rules;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class LocalSearchTermRule implements IRule<Integer> {

	private final ImmutableList<Integer> SEARCH_TERM_INDEX_LOCATIONS;
	private static final int MAX_DISTANCE_FROM_SEARCH_TERM = 20;
	private static final int DISTANCE_PERCENTAGE_SCALER = 100 / MAX_DISTANCE_FROM_SEARCH_TERM;

	public LocalSearchTermRule(final List<Integer> SEARCH_TERM_INDEX_LOCATIONS) {
		checkNotNull(SEARCH_TERM_INDEX_LOCATIONS,
				"Error: Search Term Index Locations Cannot be Null");
		this.SEARCH_TERM_INDEX_LOCATIONS = new ImmutableList.Builder<Integer>()
				.addAll(SEARCH_TERM_INDEX_LOCATIONS).build();
	}

	@Override
	public float apply(final Integer WORD_INDEX) {
		checkNotNull(WORD_INDEX, "Error: Word Index cannot be null");
		int minDistanceToSearchTerm = Integer.MAX_VALUE;
		for (Integer indexLocation : SEARCH_TERM_INDEX_LOCATIONS) {

			if (distanceBetweenIndexes(WORD_INDEX, indexLocation) < minDistanceToSearchTerm) {
				minDistanceToSearchTerm = distanceBetweenIndexes(WORD_INDEX,
						indexLocation);
			}
		}
		if (minDistanceToSearchTerm >= MAX_DISTANCE_FROM_SEARCH_TERM) {
			return 0;
		}
		return getDistanceScore(minDistanceToSearchTerm);
	}

	/**
	 * Returns the distance between two word's indexes.
	 * 
	 * @param indexA
	 *            First word's index.
	 * @param indexB
	 *            Second word's index.
	 * @return Positive value representing the difference in the two indexes
	 *         being compared.
	 */
	private int distanceBetweenIndexes(final int indexA, final int indexB) {
		int distance = indexA - indexB;
		if (distance < 0) {
			return distance * -1;
		}
		return distance;
	}

	/**
	 * Returns a value between 0 and 1, where 1.00 represents the case where the
	 * word being examined is the search term and 0.00 is greater or equal to
	 * the maximum distance allowed.
	 * 
	 * @param distance
	 *            The distance between the index of a word and the search term.
	 * @return Floating point number between 0 and 1 representing the overall
	 *         score of the word's location in respect to the search term.
	 */
	private float getDistanceScore(final int distance) {
		return distance % MAX_DISTANCE_FROM_SEARCH_TERM
				* DISTANCE_PERCENTAGE_SCALER / 100;
	}
}
