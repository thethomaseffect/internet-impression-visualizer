package ie.gmit.impressionengine.searchparser;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SearchResultLinksList {

	public static List<String> getResultLinkList(final String QUERY) {
		checkNotNull(QUERY, "Error: No Search Query Provided");
		final ObjectMapper mapper = new ObjectMapper();
		final BingSearch search = new BingSearch();
		final String resultsJson = search.getJSONSearchResults(QUERY);
		JsonNode input = null;
		try {
			input = mapper.readTree(resultsJson);
		} catch (JsonProcessingException e) {
			System.out.println("Error: Could not parse JSON");
		} catch (IOException e) {
			System.out.println("Error: Could not retrieve web results");
		}

		final JsonNode results = input.get("d").get("results");
		return getResultsAsList(results);

	}

	private static List<String> getResultsAsList(final JsonNode results) {
		List<String> links = new ArrayList<String>(10);
		for (final JsonNode result : results) {
			links.add(result.path("Url").asText());
		}
		return links;
	}

	public static void main(final String[] args) {
		List<String> actualLinks = getResultLinkList("Thomas Geraghty");
		for (String link : actualLinks) {
			System.out.println(link);
		}
	}

}
