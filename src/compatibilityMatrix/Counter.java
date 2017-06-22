package compatibilityMatrix;

/**
 * Counter class
 * 
 * @author ksemer
 */
public class Counter {
	private int counter;

	/**
	 * Constructor
	 */
	public Counter(int val) {
		counter = val;
	}

	/**
	 * Add c to counter
	 * 
	 * @param c
	 */
	public void increase(int c) {
		counter += c;
	}

	/**
	 * Set the counter to val
	 * 
	 * @param val
	 */
	public void set(int val) {
		counter = val;
	}

	/**
	 * Return counter
	 * 
	 * @return
	 */
	public int getValue() {
		return counter;
	}
}