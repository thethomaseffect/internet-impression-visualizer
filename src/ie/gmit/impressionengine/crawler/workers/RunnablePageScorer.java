package ie.gmit.impressionengine.crawler.workers;

import ie.gmit.impressionengine.crawler.ComparableLink;
import ie.gmit.impressionengine.crawler.concurrentqueues.CloudWordQueue;
import ie.gmit.impressionengine.crawler.concurrentqueues.LinksQueue;
import ie.gmit.impressionengine.crawler.concurrentqueues.PageQueue;
import ie.gmit.impressionengine.scoring.WebpageScoringManager;
import ie.gmit.impressionengine.scoring.links.PreviouslyVisitedURLs;

import java.util.List;
import java.util.Set;

/**
 * Consumes items from a <code>PageQueue</code> and produces items for
 * <code>LinksQueue</code> and <code>CloudWordQueue</code>.
 * 
 * @author thomas
 * 
 */
public class RunnablePageScorer implements Runnable {

	private static String SEARCH_TERM;
	private static PageQueue pageQueue;
	private static LinksQueue linksQueue;
	private static CloudWordQueue cloudWordQueue;
	private static PreviouslyVisitedURLs prevVisitedURLs;
	private static Set<String> adjectiveSet;
	private static Set<String> stopwordSet;
	private volatile boolean shutdown = false;

	public RunnablePageScorer(final String SEARCH_TERM,
			final PageQueue pageQueue, final LinksQueue linksQueue,
			final CloudWordQueue cloudWordQueue,
			final PreviouslyVisitedURLs prevVisitedURLs,
			final Set<String> adjectiveSet, final Set<String> stopwordSet) {
		RunnablePageScorer.SEARCH_TERM = SEARCH_TERM;
		RunnablePageScorer.pageQueue = pageQueue;
		RunnablePageScorer.linksQueue = linksQueue;
		RunnablePageScorer.cloudWordQueue = cloudWordQueue;
		RunnablePageScorer.prevVisitedURLs = prevVisitedURLs;
		RunnablePageScorer.adjectiveSet = adjectiveSet;
		RunnablePageScorer.stopwordSet = stopwordSet;
	}

	public boolean isShutdown() {
		return shutdown;
	}

	@Override
	public void run() {
		while (!shutdown) {
			// Get the next page off the queue or wait
			String currentPage = pageQueue.getHeadOrAwait();
			// Score the pages links and send them to the links queue
			// TODO: Create a page object with contents and depth
			// TODO: Only score links if page isn't at maximum depth
			WebpageScoringManager scoring = new WebpageScoringManager(
					currentPage, SEARCH_TERM, prevVisitedURLs, adjectiveSet,
					stopwordSet, 0);
			List<ComparableLink> topLinks = scoring.returnTopLinks(5);
			for (ComparableLink link : topLinks) {
				if (link.getLink().contains("http://")) {
					linksQueue.add(link);
					System.out.printf("[SCORER] Added %s to links queue.\n",
							link.getLink());
				}
			}
			// Score the page's words and send them to the cloud words queue
			List<String> minBeatingScoredWords = scoring.getScoredWords(20);
			for (String word : minBeatingScoredWords) {
				System.out.printf("[SCORER] Added %s to cloud word queue\n",
						word);
				cloudWordQueue.add(word);
			}

		}

	}

	public void setShutdown(final boolean shutdown) {
		this.shutdown = shutdown;
	}

}
