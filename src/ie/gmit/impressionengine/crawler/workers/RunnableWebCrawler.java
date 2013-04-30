package ie.gmit.impressionengine.crawler.workers;

import ie.gmit.impressionengine.crawler.WebpageFetcher;
import ie.gmit.impressionengine.crawler.concurrentqueues.LinksQueue;
import ie.gmit.impressionengine.crawler.concurrentqueues.PageQueue;
import ie.gmit.impressionengine.scoring.links.PreviouslyVisitedURLs;

/**
 * Consumes links from a <code>LinksQueue</code> and produces items for to a
 * <code>PageQueue</code>.
 * 
 */
public class RunnableWebCrawler implements Runnable {

	private static LinksQueue linksQueue;
	private static PageQueue pageQueue;
	private static PreviouslyVisitedURLs prevVisitedURLS;
	private volatile boolean shutdown = false;

	public RunnableWebCrawler(final LinksQueue linksQueue,
			final PageQueue pageQueue,
			final PreviouslyVisitedURLs prevVisitedURLS) {
		RunnableWebCrawler.linksQueue = linksQueue;
		RunnableWebCrawler.pageQueue = pageQueue;
		RunnableWebCrawler.prevVisitedURLS = prevVisitedURLS;
	}

	public boolean isShutdown() {
		return shutdown;
	}

	@Override
	public void run() {
		while (!shutdown) {
			// Get Link from queue
			String currentLink = linksQueue.getHeadOrAwait();
			// Send retrieved page to queue
			String currentWebpage = new WebpageFetcher(currentLink)
					.getWebpage();
			if (!currentWebpage.equals("Null35902345")) {
				pageQueue.add(currentWebpage);
				// Add to previously visited URLs
				prevVisitedURLS.addURL(currentLink);
				System.out.printf(
						"[CRAWLER] Added %s to previously visited URLs.\n",
						currentLink);
				System.out.printf(
						"[CRAWLER] Added contents of %s to page queue.\n",
						currentLink);
			}
		}

	}

	public void setShutdown(final boolean shutdown) {
		this.shutdown = shutdown;
	}

}
