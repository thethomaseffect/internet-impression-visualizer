package ie.gmit.impressionengine.scoring.rules;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class IsStopWordRule implements IRule<String> {

	private final ImmutableSet<String> STOP_WORD_SET;

	public IsStopWordRule(final Set<String> STOP_WORD_SET) {
		this.STOP_WORD_SET = new ImmutableSet.Builder<String>().addAll(
				STOP_WORD_SET).build();
	}

	@Override
	public float apply(final String WORD) {
		if (STOP_WORD_SET.contains(WORD)) {
			return 1;
		}
		return 0;
	}
}
