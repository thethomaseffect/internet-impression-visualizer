package ie.gmit.impressionengine.searchparser;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SearchResultLinksListTest {

	@Test
	public void testGetResultLinkList() {
		List<String> expectedLinks = new ArrayList<String>();
		// Could add more URLs but changing result ranking would mean changing
		// test data
		expectedLinks.add("http://www.w3.org/XML/Query/test-suite/");
		expectedLinks
				.add("http://www.w3.org/2003/03/rdfqr-tests/rdf-query-testcases.html");

		List<String> actualLinks = SearchResultLinksList
				.getResultLinkList("Test Query");
		for (int i = 0; i < expectedLinks.size(); i++) {
			assertEquals(expectedLinks.get(i), actualLinks.get(i));
		}
	}

}
