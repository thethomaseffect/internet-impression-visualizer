package ie.gmit.impressionengine.scoring.rules;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LinkContainsSearchTermRuleTest {

	@Test
	public void testApply() {
		LinkContainsSearchTermRule rule = new LinkContainsSearchTermRule(
				"Computer Video Entertainment");
		assertEquals(0.66, rule.apply("http://www.computer.com/video"), 0.01);
	}

}
