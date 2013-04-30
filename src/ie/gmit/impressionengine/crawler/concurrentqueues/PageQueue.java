package ie.gmit.impressionengine.crawler.concurrentqueues;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PageQueue {

	private static BlockingQueue<String> pageQueue = new ArrayBlockingQueue<String>(
			20);

	public void add(final String webpage) {
		pageQueue.add(webpage);
	}

	public String getHeadOrAwait() {
		try {
			return pageQueue.take();
		} catch (InterruptedException e) {
			System.out
					.println("Error: getHeadOrAwait was interrupted while waiting for a value.");
		}
		return null;
	}

}
