package ie.gmit.impressionengine.crawler;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A link is a <code>Comparable</code> object that contains a score, link and
 * depth the link was found at.
 * 
 */
public class ComparableLink implements Comparable<ComparableLink> {

	private final int score;

	private final String link;

	private final int depth;

	/**
	 * Creates a new <code>Link</code> object with a score, link and depth.
	 * 
	 * @param score
	 *            Score of link.
	 * @param link
	 *            The link itself.
	 * @param depth
	 *            The depth this link was found at during the search.
	 */
	public ComparableLink(final int score, final String link, final int depth) {
		checkNotNull(link, "Error: Link cannot be null");
		checkArgument(depth >= 0,
				"Error: Depth must be greater or equal to zero.");
		this.score = score;
		this.link = link;
		this.depth = depth;
	}

	@Override
	public int compareTo(final ComparableLink link) {
		return this.score - link.score;
	}

	public int getDepth() {
		return depth;
	}

	public String getLink() {
		return link;
	}

	public int getScore() {
		return score;
	}

}
