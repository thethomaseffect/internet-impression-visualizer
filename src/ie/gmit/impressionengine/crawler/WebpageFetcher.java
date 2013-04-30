package ie.gmit.impressionengine.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

// TODO: Update documentation to reflect changes to url

/**
 * Gets the contents of a webpage and saves it to a string. Note that this class
 * does not remove HTML from the webpage. This should be done during parsing.
 */
public class WebpageFetcher {

	private final String url;

	public WebpageFetcher(final String url) {
		this.url = url;
	}

	private String getURL() {
		return url;
	}

	/**
	 * Gets the webpage contents and returns it as a String
	 * 
	 * @return Webpage contents
	 */
	public String getWebpage() {
		InputStream inputStream = null;
		String currentLine;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			URL webpage = new URL(getURL());
			inputStream = webpage.openStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			while ((currentLine = bufferedReader.readLine()) != null) {
				stringBuilder.append(currentLine).append(' ');
			}
			// Close bufferedReader
			bufferedReader.close();
			String pageText = stringBuilder.toString().trim();
			if (!pageText.equals(null)) {
				return pageText;
			} else {
				return "Null35902345";
			}
		} catch (IOException ioe) {
			System.out.printf("Error: Could not fetch webpage %s\n", url);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ioe) {
				System.out.println("Error: Could not close InputStream");
			}
		}
		return "Null35902345";
	}

}
