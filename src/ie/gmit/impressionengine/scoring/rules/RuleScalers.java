package ie.gmit.impressionengine.scoring.rules;

/**
 * A utility class containing scalers for rules. By using simple values between
 * 0 and 1 for rules and any number for a scaler, we can easily make changes to
 * the overall scoring of a piece of data.
 * 
 */
public final class RuleScalers {

	public static final float IS_ADJECTIVE_SCALER = 50;
	public static final float LINK_CONTAINS_SEARCH_TERM_SCALER = 10;
	public static final float LINK_TEXT_CONTAINS_SEARCH_TERM_SCALER = 20;
	public static final float LOCAL_SEARCH_TERM_SCALER = 40;
	public static final float PREVIOUSLY_VISITED_HOSTNAME_SCALER = -10;

	// Private Constructor as this is a utility class
	private RuleScalers() {
		throw new UnsupportedOperationException(
				"RuleScalers is a utility class "
						+ "and should not be instantiated");
	}

}
