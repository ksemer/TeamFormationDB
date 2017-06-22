package compatibilityMatrix;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class represents the Node objects
 * 
 * @author ksemer
 */
public class Node {

	// =================================================================
	private int id;
	private Map<Node, Integer> adjacencies;
	// =================================================================

	/**
	 * Constructor
	 * 
	 * @param id
	 */
	public Node(int id) {
		this.id = id;
		this.adjacencies = new HashMap<>();
	}

	/**
	 * Add new edge
	 * 
	 * @param node
	 * @param sign
	 */
	public void addEdge(Node node, int sign) {
		if (!adjacencies.containsKey(node))
			adjacencies.put(node, sign);
	}

	/**
	 * Return node's adjacency as a Map
	 * 
	 * @return
	 */
	public Map<Node, Integer> getAdjacencyAsMap() {
		return adjacencies;
	}

	/**
	 * Return edge object for neighbor node n
	 * 
	 * @param n
	 * @return
	 */
	public int getEdgeSign(Node n) {
		return adjacencies.get(n);
	}

	/**
	 * Returns node's id
	 * 
	 * @return
	 */
	public int getID() {
		return id;
	}

	/**
	 * Return node's adjacency
	 * 
	 * @return
	 * @return
	 */
	public Set<Node> getAdjacency() {
		return adjacencies.keySet();
	}
}