package ie.gmit.impressionengine.scoring.rules;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class IsAdjectiveRule implements IRule<String> {

	private final ImmutableSet<String> ADJECTIVE_WORD_SET;

	public IsAdjectiveRule(final Set<String> ADJECTIVE_WORD_SET) {
		this.ADJECTIVE_WORD_SET = new ImmutableSet.Builder<String>().addAll(
				ADJECTIVE_WORD_SET).build();
	}

	@Override
	public float apply(final String WORD) {
		if (ADJECTIVE_WORD_SET.contains(WORD)) {
			return 1;
		}
		return 0;
	}

}
