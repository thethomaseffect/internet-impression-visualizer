package ie.gmit.impressionengine;

import ie.gmit.impressionengine.cloud.OpenCloudGenerator;
import ie.gmit.impressionengine.cloud.WordCloud;
import ie.gmit.impressionengine.crawler.ComparableLink;
import ie.gmit.impressionengine.crawler.WordSetLoader;
import ie.gmit.impressionengine.crawler.concurrentqueues.CloudWordQueue;
import ie.gmit.impressionengine.crawler.concurrentqueues.LinksQueue;
import ie.gmit.impressionengine.crawler.concurrentqueues.PageQueue;
import ie.gmit.impressionengine.crawler.workers.RunnableCloudBuilder;
import ie.gmit.impressionengine.crawler.workers.RunnablePageScorer;
import ie.gmit.impressionengine.crawler.workers.RunnableWebCrawler;
import ie.gmit.impressionengine.scoring.links.PreviouslyVisitedURLs;
import ie.gmit.impressionengine.searchparser.SearchResultLinksList;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Creates a word cloud from a provided search term by getting results from a
 * search engine and crawling it's children.
 * 
 * 
 */
public class MainInterface {

	private static String joinArgs(final String[] args) {
		StringBuilder joinedArgs = new StringBuilder();
		for (String arg : args) {
			joinedArgs.append(arg).append(' ');
		}
		return joinedArgs.toString().trim();
	}

	/**
	 * @param args
	 *            The search term to use for generating the cloud
	 */
	public static void main(final String[] args) {
		String SEARCH_TERM;
		if (args.length < 1) {
			System.out.println("Usage: ImpressionEngine <YOUR SEARCH TERM>");
			return;
		} else {
			SEARCH_TERM = joinArgs(args);
		}

		new SearchResultLinksList();
		// Get Bing Search Results as List
		List<String> searchResults = SearchResultLinksList
				.getResultLinkList(SEARCH_TERM);
		// Create concurrent objects
		PreviouslyVisitedURLs prevVisitedURLS = new PreviouslyVisitedURLs();
		WordCloud wordCloud = new WordCloud();
		// Create all necessary concurrent queues
		LinksQueue linksQueue = new LinksQueue(prevVisitedURLS);
		PageQueue pageQueue = new PageQueue();
		CloudWordQueue cloudWordQueue = new CloudWordQueue();

		// Add search results to links queue
		int searchScore = 10000;
		for (String url : searchResults) {
			linksQueue.add(new ComparableLink(searchScore, url, 0));
			searchScore--;
		}

		// Create Thread pools for runnables
		final int MAX_CRAWLER_THREADS = 10;
		ExecutorService crawlerExecutor = Executors
				.newFixedThreadPool(MAX_CRAWLER_THREADS);

		final int MAX_SCORER_THREADS = 20;
		ExecutorService scorerExecutor = Executors
				.newFixedThreadPool(MAX_SCORER_THREADS);

		final int MAX_CLOUD_BUILDER_THREADS = 20;
		ExecutorService cloudBuilderExecutor = Executors
				.newFixedThreadPool(MAX_CLOUD_BUILDER_THREADS);

		// Load Word sets for scoring
		WordSetLoader wordSetLoader = new WordSetLoader();
		Set<String> adjectiveWordSet = wordSetLoader
				.getWordSet("/res/adjectives.txt");
		Set<String> stopwordWordSet = wordSetLoader
				.getWordSet("/res/stopwords.txt");

		// Create Runnables
		RunnableWebCrawler webCrawler = new RunnableWebCrawler(linksQueue,
				pageQueue, prevVisitedURLS);
		RunnablePageScorer pageScorer = new RunnablePageScorer(SEARCH_TERM,
				pageQueue, linksQueue, cloudWordQueue, prevVisitedURLS,
				adjectiveWordSet, stopwordWordSet);
		RunnableCloudBuilder cloudBuilder = new RunnableCloudBuilder(
				cloudWordQueue, wordCloud);

		// Run Threads
		crawlerExecutor.execute(webCrawler);
		scorerExecutor.execute(pageScorer);
		cloudBuilderExecutor.execute(cloudBuilder);

		// Continue until n words have been gathered
		while (wordCloud.getSize() < 100) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				System.out.println("Error: Sleep thread was interrupted");
			}
		}
		// Shutdown threads and build visual cloud
		webCrawler.setShutdown(true);
		crawlerExecutor.shutdown();
		pageScorer.setShutdown(true);
		scorerExecutor.shutdown();
		cloudBuilder.setShutdown(true);
		cloudBuilderExecutor.shutdown();

		// Wait for all threads to finish
		while (!(crawlerExecutor.isTerminated()
				&& scorerExecutor.isTerminated() && cloudBuilderExecutor
					.isTerminated())) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				System.out.println("Error: Sleep thread was interrupted");
			}
		}
		List<String> mostFrequentWords = wordCloud.getMostFrequentWordsList();
		OpenCloudGenerator cloudGen = new OpenCloudGenerator(mostFrequentWords);
		cloudGen.showTagWindow();
	}
}
