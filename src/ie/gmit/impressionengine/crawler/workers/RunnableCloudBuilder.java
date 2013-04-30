package ie.gmit.impressionengine.crawler.workers;

import ie.gmit.impressionengine.cloud.WordCloud;
import ie.gmit.impressionengine.crawler.concurrentqueues.CloudWordQueue;

/**
 * Consumes items from a <code>CloudWordQueue</code> and produces a
 * <code>MultiSet</code> used to create visual word cloud.
 * 
 * @author thomas
 * 
 */
public class RunnableCloudBuilder implements Runnable {
	private static CloudWordQueue cloudWordQueue;
	private static WordCloud wordCloud;
	private volatile boolean shutdown = false;

	public RunnableCloudBuilder(final CloudWordQueue cloudWordQueue,
			final WordCloud wordCloud) {
		RunnableCloudBuilder.cloudWordQueue = cloudWordQueue;
		RunnableCloudBuilder.wordCloud = wordCloud;
	}

	public boolean isShutdown() {
		return shutdown;
	}

	@Override
	public void run() {
		while (!shutdown) {
			String currentWord = cloudWordQueue.getHeadOrAwait();
			wordCloud.addWordToCloud(currentWord);
			System.out.printf("[CLOUD] Added %s to cloud.\n", currentWord);
		}

	}

	public void setShutdown(final boolean shutdown) {
		this.shutdown = shutdown;
	}

}
