package ie.gmit.impressionengine.crawler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Loads requested file and returns it's contents as a string
 */
public class FileContentsFetcher {

	/**
	 * Loads the contents of the file at the provided file path as a
	 * <code>String</code> and returns it to the caller.
	 * 
	 * @param PATH_TO_FILE
	 *            OS-Independent absolute path to file
	 * @return Contents of file as <code>String</code>
	 */
	public static String getFileContents(final String PATH_TO_FILE) {

		FileInputStream fs = null;
		try {
			fs = new FileInputStream(PATH_TO_FILE);
		} catch (FileNotFoundException e) {
			System.out.println("Error: Could not find the file specified");
		}
		BufferedReader bReader = new BufferedReader(new InputStreamReader(fs));
		StringBuilder stringBuilder = new StringBuilder();
		String currentLine;
		// Iterate through file and append each line to string builder
		try {
			while ((currentLine = bReader.readLine()) != null) {
				/*
				 * Where there would be a new line character add some
				 * whitespace.
				 */
				stringBuilder.append(currentLine).append(' ');
			}
		} catch (IOException e) {
			System.out
					.println("Error: Unexpected error occured while processing file");
		}
		// Close open resources and return requesting line String
		try {
			bReader.close();
			fs.close();
		} catch (IOException e) {
			System.out
					.println("Error: Unexpected error occured while closing file");
		}

		return stringBuilder.toString().trim();

	}
}
