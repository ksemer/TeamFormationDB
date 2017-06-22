package compatibilityMatrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Graph class
 * 
 * @author ksemer
 */
public class Graph {

	// =================================================================
	private Map<Integer, Node> nodes;
	// =================================================================

	/**
	 * Constructor
	 * 
	 * @throws IOException
	 */
	public Graph() {
		nodes = new HashMap<Integer, Node>();
	}

	/**
	 * Add node in G
	 * 
	 * @param node
	 */
	public void addNode(int node) {
		if (nodes.get(node) == null) {
			nodes.put(node, new Node(node));
		}
	}

	/**
	 * Get node object with id = nodeID
	 * 
	 * @param nodeID
	 * @return
	 */
	public Node getNode(int nodeID) {
		return nodes.get(nodeID);
	}

	/**
	 * Add edge in LVG
	 * 
	 * @param src
	 * @param trg
	 * @param time
	 */
	public void addEdge(int src, int trg, int sign) {
		if (!nodes.get(src).getAdjacency().contains(trg))
			nodes.get(src).addEdge(nodes.get(trg), sign);
	}

	/**
	 * Return graph nodes
	 * 
	 * @return
	 */
	public Collection<Node> getNodes() {
		return nodes.values();
	}

	/**
	 * Return graph nodes as a map
	 * 
	 * @return
	 */
	public Map<Integer, Node> getNodesAsMap() {
		return this.nodes;
	}

	/**
	 * Graph Size
	 * 
	 * @return
	 */
	public int size() {
		return nodes.size();
	}

	/**
	 * Load Graph
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public void load(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = null;
		String edge[];
		int n1_id, n2_id, sign;

		System.out.println("Graph is loading....");

		while ((line = br.readLine()) != null) {
			edge = line.split("\t");

			n1_id = Integer.parseInt(edge[0]);
			n2_id = Integer.parseInt(edge[1]);

			addNode(n1_id);
			addNode(n2_id);
			sign = Integer.parseInt(edge[2]);

			if (n1_id == n2_id)
				continue;

			// src -> trg sign label
			addEdge(n1_id, n2_id, sign);

			// src -> trg sign label
			addEdge(n2_id, n1_id, sign);
		}
		br.close();

		System.out.println("Graph loaded....");
	}
}