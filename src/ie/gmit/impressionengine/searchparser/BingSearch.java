package ie.gmit.impressionengine.searchparser;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BingSearch {
	private static final String ENCODED_API_KEY = "<YOUR_KEY_HERE>";
	private static final int MAX_RESULTS = 10;

	/**
	 * @param searchQuery
	 * @return
	 */
	private String formatQueryString(final String searchQuery) {
		return searchQuery.replaceAll(" ", "%20");
	}

	public String getJSONSearchResults(String searchQuery) {
		checkNotNull(searchQuery, "Error: No search query provided");
		URL bingSearchURL;
		StringBuilder stringBuilder = new StringBuilder();
		searchQuery = formatQueryString(searchQuery);
		try {
			bingSearchURL = new URL(
					"https://api.datamarket.azure.com/Bing/Search/"
							+ "v1/Web?Query=%27" + searchQuery + "%27&$top="
							+ MAX_RESULTS + "&$format=Json");
			HttpURLConnection connection = (HttpURLConnection) bingSearchURL
					.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Basic "
					+ ENCODED_API_KEY);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));

			String currentLine;
			while ((currentLine = bufferedReader.readLine()) != null) {
				stringBuilder.append(currentLine);
			}

			connection.disconnect();
		} catch (MalformedURLException e) {
			System.out.println("Error: Not a valid URL");
		} catch (IOException e) {
			System.out.println("Error: Could not retrieve search results");
		}
		return stringBuilder.toString();
	}

}