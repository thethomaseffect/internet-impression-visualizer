package ie.gmit.impressionengine.cloud;

import static org.junit.Assert.assertEquals;
import ie.gmit.impressionengine.crawler.WordStripper;

import org.junit.Test;

public class WordStripperTest {

	@Test
	public void testStripWord() {
		assertEquals("HEL£$OOOO", WordStripper.stripWord("   $%£HEL£$OOOO£$^"));
	}

	@Test
	public void testStripWordWithNoInvalidCharacters() {
		assertEquals("Test", WordStripper.stripWord("Test"));
	}

	public void testStripWordWithNullArgument() {
		assertEquals(null, WordStripper.stripWord(null));
	}

	@Test
	public void testStripWordWithWhitespace() {
		assertEquals("", WordStripper.stripWord("        "));
	}
}
