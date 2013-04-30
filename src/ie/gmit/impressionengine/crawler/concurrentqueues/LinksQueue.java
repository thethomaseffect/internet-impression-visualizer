package ie.gmit.impressionengine.crawler.concurrentqueues;

import ie.gmit.impressionengine.crawler.ComparableLink;
import ie.gmit.impressionengine.scoring.links.PreviouslyVisitedURLs;

import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public class LinksQueue {

	private static final Comparator<ComparableLink> largestFirstComparator = Collections
			.reverseOrder(null);
	private static final PriorityBlockingQueue<ComparableLink> linksQueue = new PriorityBlockingQueue<ComparableLink>(
			100, largestFirstComparator);
	private static PreviouslyVisitedURLs prevVisitedURLs = new PreviouslyVisitedURLs();

	public LinksQueue(final PreviouslyVisitedURLs prevVisitedURLs) {
		LinksQueue.prevVisitedURLs = prevVisitedURLs;
	}

	public void add(final ComparableLink link) {
		if (!prevVisitedURLs.isPreviouslyVisitedURL(link.getLink())) {
			linksQueue.add(link);
		}
	}

	public String getHeadOrAwait() {
		try {
			return linksQueue.take().getLink();
		} catch (InterruptedException e) {
			System.out
					.println("Error: getHeadOrAwait was interrupted while waiting for a value.");
		}
		return null;
	}

	public String getHeadOrNull() {
		return linksQueue.poll().getLink();
	}

}
