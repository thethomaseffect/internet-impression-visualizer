package ie.gmit.impressionengine.crawler;

/**
 * Provides a lightweight implementation of the Apache Commons StringUtils Strip
 * command tailored to this application
 */
public final class WordStripper {

	private static final String legalCharacters = "qwertyuiopasdfghjklzxcvbnm"
			+ "QWERTYUIOPASDFGHJKLZXCVBNM";

	/**
	 * Strips non-letter character from the end of the word
	 * 
	 * @param wordString
	 *            Word to be striped
	 * @return Word with no non-letter characters at end of word
	 */
	private static String stripEndOfWord(final String wordString) {
		int end;
		// If provided string is null or empty return
		if (wordString == null || (end = wordString.length()) == 0) {
			return wordString;
		}
		// This iterates through the string as an array until it finds a valid
		// ending character and then returns a substring ending at that
		// character
		while (end != 0
				&& !(legalCharacters.indexOf(wordString.charAt(end - 1)) != -1)) {
			end--;
		}

		return wordString.substring(0, end);
	}

	/**
	 * Strips non-letter character from the start of the word
	 * 
	 * @param wordString
	 *            Word to be striped
	 * @return Word with no non-letter characters at start of word
	 */
	private static String stripStartOfWord(final String wordString) {
		int stringLength;
		// If provided string is null or empty return
		if (wordString == null || (stringLength = wordString.length()) == 0) {
			return wordString;
		}
		int start = 0;
		// This iterates through the string as an array until it finds a valid
		// starting character and then returns a substring starting at that
		// character
		while (start != stringLength
				&& !(legalCharacters.indexOf(wordString.charAt(start)) != -1)) {
			start++;
		}

		return wordString.substring(start);
	}

	/**
	 * Removes the non-letter characters to the left and right of the provided
	 * word while retaining all others.
	 * 
	 * @param wordString
	 * @return
	 */
	public static String stripWord(String wordString) {
		// Strip start of word and then pass transformed word to stripEndOfWord
		// to complete
		wordString = stripStartOfWord(wordString);
		return stripEndOfWord(wordString);
	}

	// Private Constructor as this is a utility class
	private WordStripper() {
		throw new UnsupportedOperationException(
				"WordStripper is a utility class "
						+ "and should not be instantiated");
	}
}
