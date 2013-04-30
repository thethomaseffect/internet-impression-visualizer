package ie.gmit.impressionengine.scoring.links;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages a collection of previously visited links and allows several
 * operations to be made on them.
 */
public class PreviouslyVisitedURLs {

	// private BlockingSet<String> previouslyVisitedUrls;
	private static Set<String> previouslyVisitedURLs = Collections
			.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
	private static Set<String> previouslyVisitedHostnames = Collections
			.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

	public void addURL(final String URL) {
		checkNotNull(URL, "Error: URL Cannot be Null");
		URL url = null;
		try {
			url = new URL(URL);
			previouslyVisitedHostnames.add(url.getHost());
			previouslyVisitedURLs.add(URL);

		} catch (MalformedURLException e) {
			System.out.printf("Error: %s is a malformedvalid URL.\n", URL);
		}

	}

	public boolean isPreviouslyVisitedHostname(final String URL) {
		URL url = null;
		try {
			url = new URL(URL);
		} catch (MalformedURLException e) {
			System.out.printf("Error: %s is a malformed URL.\n", URL);
		}
		if (url != null) {
			return previouslyVisitedHostnames.contains(url.getHost());
		}
		return false;
	}

	public boolean isPreviouslyVisitedURL(final String URL) {
		return previouslyVisitedURLs.contains(URL);
	}

}
