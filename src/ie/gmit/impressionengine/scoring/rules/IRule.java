package ie.gmit.impressionengine.scoring.rules;

/**
 * 
 * A Rule takes some value, evaluates it's conditions and returns a floating
 * point number between 0 and 1.
 */
public interface IRule<T> {
	/**
	 * Evaluates the conditions of the rule on the provided value and returns a
	 * <code>float</code> in the range of 0 and 1.
	 * 
	 * @param val
	 *            The value to be evaluated.
	 * @return The evaluated score of the provided value.
	 */
	float apply(T val);
}
