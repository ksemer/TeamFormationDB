package skillsCompatibilityMatrix;
/**
 * Counter class
 * 
 * @author ksemer
 */
public class Counter {
	private int counter = -1;
	private double counter_d = -1;

	/**
	 * Constructor
	 */
	public Counter(int val) {
		counter = val;
	}
	
	/**
	 * Constructor
	 */
	public Counter(double val) {
		counter_d = val;
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
	 * Add c to counter 
	 * @param c
	 */
	public void increase(double c) {
		counter_d += c;	
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
	public int getIntValue() {
		return counter;
	}
	
	/**
	 * Return counter
	 * 
	 * @return
	 */
	public double getDoubleValue() {
		return counter_d;
	}
}