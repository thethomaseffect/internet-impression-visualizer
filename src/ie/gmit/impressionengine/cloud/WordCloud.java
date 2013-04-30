package ie.gmit.impressionengine.cloud;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

/**
 * <code>WordCloud</code> creates all necessary components needed to construct
 * the word cloud and allows other objects to obtain a list of the top 20 most
 * frequent words ordered by frequency.
 */
public class WordCloud {

	private final Multiset<String> wordCloud = ConcurrentHashMultiset.create();

	public void addListToCloud(final List<String> wordsList) {
		wordCloud.addAll(wordsList);
	}

	public void addWordToCloud(final String word) {
		wordCloud.add(word);
	}

	public List<String> getMostFrequentWordsList() {
		List<String> mostFrequentWords = new ArrayList<String>(20);
		int counter = 0;
		for (String word : Multisets.copyHighestCountFirst(wordCloud)
				.elementSet()) {
			mostFrequentWords.add(word);
			// INFO: This looks a little ugly, but actually allows us all the
			// benefits of using an iterator along with allowing rare case of
			// lists smaller than 20 words to be returned.
			counter++;
			if (counter >= 20) {
				break;
			}

		}

		return mostFrequentWords;
	}

	public int getSize() {
		return wordCloud.size();
	}

}
