package compatibilityMatrix;

import java.util.HashSet;
import java.util.Set;

/**
 * Path class
 * 
 * @author ksemer
 *
 */
public class Path {

	// nodes in the path
	private Set<Node> nodes;

	// last node of the path
	private Node lastNode;

	// path sign
	private int sign;

	/**
	 * Constructor
	 * 
	 * @param n
	 */
	public Path(Node n) {
		nodes = new HashSet<>();
		nodes.add(n);
		lastNode = n;
	}

	/**
	 * Constructor (copy path p to this)
	 * 
	 * @param p
	 */
	public Path(Path p) {
		nodes = new HashSet<>();
		nodes.addAll(p.getNodes());
		sign = p.getSign();
		lastNode = p.getLastNode();
	}

	/**
	 * Return path's nodes
	 * 
	 * @return
	 */
	public Set<Node> getNodes() {
		return nodes;
	}

	/**
	 * Return last node
	 * 
	 * @return
	 */
	public Node getLastNode() {
		return lastNode;
	}

	/**
	 * Return if n is contained in path
	 * 
	 * @param n
	 * @return
	 */
	public boolean contains(Node n) {
		if (nodes.contains(n))
			return true;
		return false;
	}

	/**
	 * Add new node in the path
	 * 
	 * @param n
	 */
	public void add(Node n) {
		nodes.add(n);
		lastNode = n;
	}

	/**
	 * Set path's sign to s
	 * 
	 * @param s
	 */
	public void setSign(int s) {
		sign = s;
	}

	/**
	 * Return path's sign
	 * 
	 * @return
	 */
	public int getSign() {
		return sign;
	}

	/**
	 * Return path's length
	 * 
	 * @return
	 */
	public int length() {
		return nodes.size() - 1;
	}
}