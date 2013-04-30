package ie.gmit.impressionengine.scoring;

import static com.google.common.base.Preconditions.checkNotNull;
import ie.gmit.impressionengine.crawler.WordStripper;

import java.util.ArrayList;
import java.util.List;

public class ScoringUtilities {

	/**
	 * Takes a <code>String</code> representing a HTML page, extracts the text
	 * and returns an ordered list of uppercase words.
	 * 
	 * @param webpage
	 *            String representing a HTML webpage's contents.
	 * 
	 */
	public static List<String> getPageWordsAsList(final String webpage) {
		checkNotNull(webpage, "Error: Webpage String cannot be null");
		List<String> pageWords = new ArrayList<String>();

		// Split the string into an array of strings on white space
		String[] pageWordsArray = webpage.split(" ");

		for (String word : pageWordsArray) {
			// Strip characters that aren't in the English alphabet
			word = WordStripper.stripWord(word);
			// Ignore words less than 3 characters long and nulls
			if (word.length() > 2 && word != null) {
				pageWords.add(word.toUpperCase());
			}
		}

		return pageWords;
	}

	public static List<Integer> getSearchTermIndexLocationsOrNull(
			final List<String> pageWords, final String searchTerm) {
		String[] tokanizedSearchTerm = getTokanizedSearchTerm(searchTerm
				.toUpperCase());
		List<Integer> indexLocations = new ArrayList<Integer>();
		for (int i = 0; i < pageWords.size(); i++) {
			for (String token : tokanizedSearchTerm) {
				if (token.equals(pageWords.get(i))) {
					indexLocations.add(i);
				}
			}
		}
		return indexLocations;
	}

	public static String[] getTokanizedSearchTerm(final String searchTerm) {
		return searchTerm.split(" ");
	}

}
