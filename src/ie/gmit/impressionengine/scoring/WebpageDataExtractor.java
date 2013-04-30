package ie.gmit.impressionengine.scoring;

import static com.google.common.base.Preconditions.checkNotNull;

import ie.gmit.impressionengine.crawler.WebpageLink;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Uses JSoup to extract data from the webpage.
 * 
 */
public class WebpageDataExtractor {

	public static List<WebpageLink> getWebpageLinks(final String webpage) {
		checkNotNull(webpage, "Error: Webpage cannot be null");
		Document doc = Jsoup.parse(webpage);
		Elements links = doc.select("a[href]");
		List<WebpageLink> linksList = new ArrayList<WebpageLink>();
		for (Element link : links) {
			linksList.add(new WebpageLink(link.attr("abs:href"), link.text()
					.trim()));
		}
		return linksList;
	}

	/**
	 * Removes non-text elements from webpage and returns it's contents
	 * 
	 * @param webpage
	 *            Webpage with non-text elements
	 * @return Webpage with no non-text elements
	 */
	public static String RemoveNonTextElements(final String webpage) {
		checkNotNull(webpage, "Error: Webpage cannot be null");
		Document doc = Jsoup.parse(webpage);
		return doc.text();
	}

}
