package ie.gmit.impressionengine.crawler.concurrentqueues;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CloudWordQueue {

	private static BlockingQueue<String> cloudWordQueue = new ArrayBlockingQueue<String>(
			100000);

	public void add(final String cloudWord) {
		cloudWordQueue.add(cloudWord);
	}

	public String getHeadOrAwait() {
		try {
			return cloudWordQueue.take();
		} catch (InterruptedException e) {
			System.out
					.println("Error: getHeadOrAwait was interrupted while waiting for a value.");
		}
		return null;
	}

}
