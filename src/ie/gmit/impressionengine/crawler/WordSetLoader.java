package ie.gmit.impressionengine.crawler;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * Contains methods that allow loading a text file of words and returning it as
 * <code>Set</code>. Text file should contain one word per line.
 * </p>
 * <p>
 * Note that <code>WordSetLoader</code> makes the assumption that all the words
 * being loaded are completely uppercase. If there is ever a time when this
 * cannot be guaranteed of the source, words should have their case changed
 * before adding to the <code>Set</code> (Omitted by default for increased
 * efficiency).
 * </p>
 */
public class WordSetLoader {

	public Set<String> getWordSet(final String PATH_TO_FILE) {
		checkNotNull(PATH_TO_FILE, "Error: File path provided was null");
		String currentWord = "";
		InputStream inputStream = null;
		Set<String> wordSet = new HashSet<String>();
		try {
			// INFO: This line cannot be used from a static context and is the
			// reason this class needs to be instantiated
			inputStream = this.getClass().getResourceAsStream(PATH_TO_FILE);
			BufferedReader bReader = new BufferedReader(new InputStreamReader(
					inputStream));

			// Iterate through file and add each word to the set
			while (currentWord != null) {
				currentWord = bReader.readLine();
				if (currentWord != null) {
					wordSet.add(currentWord);
				}
			}
			bReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error: Could not find the specified File");
		} catch (IOException e) {
			System.out.println("Error: Problem accessing specified File");
		}
		// Close open resources
		try {
			inputStream.close();
		} catch (IOException e) {
			System.out.println("Error: Could not close FileInputStream");
		}
		return wordSet;
	}

}
