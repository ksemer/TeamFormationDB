package compatibilityMatrix;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

/**
 * 
 * @author ksemer
 *
 */
public class StructuralBalancePathCompatibility {

	// Graph object
	private static Graph g;

	// INFINITY dinstance value
	public static final int INF = 1000000000;

	// Writer
	private static FileWriter w;

	/**
	 * Main
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Config.loadConfig();

		g = new Graph();
		g.load(Config.DATA_PATH);
		w = new FileWriter(Config.OUTPUT_PATH, false);

		int c = 0;
		List<Integer> nodes = new ArrayList<Integer>(g.getNodesAsMap().keySet());
		Collections.sort(nodes);

		// for each node in the graph
		for (int n : nodes) {
			c++;
			computeBSP(g.getNode(n));
			w.flush();
			System.out.println(c + "/" + g.size());
		}
		w.close();
	}

	/**
	 * Compute shortest positive and negative structurally balanced paths from
	 * the query node src
	 * 
	 * @param src
	 * @throws IOException
	 */
	private static void computeBSP(Node src) throws IOException {

		// for each u the length of positive balanced shortest path started from
		// src
		Map<Node, Integer> posDist = new HashMap<>(g.size());

		// for each u the length of negative balanced shortest path started from
		// src
		Map<Node, Integer> negDist = new HashMap<>(g.size());

		// Initialize all positive and negative balanced shortest path length to
		// INFINITY
		for (Node n : g.getNodes()) {
			posDist.put(n, INF);
			negDist.put(n, INF);
		}

		Queue<Path> q = new LinkedList<Path>();
		Path path = new Path(src), pathNew;

		// path starts with positive sign
		path.setSign(1);
		q.offer(path);

		// distance from itself 0
		posDist.put(src, 0);

		Node p, x;
		int sign;
		Integer directEdgeSign;

		while (!q.isEmpty()) {

			path = q.poll();
			p = path.getLastNode();

			// for each adjacent node of p
			for (Entry<Node, Integer> en : p.getAdjacencyAsMap().entrySet()) {

				// adjacent node
				x = en.getKey();

				// if max path length is enabled, prune paths that exceed the
				// limit
				if (Config.MAX_PATH_LENGTH != -1 && path.length() == Config.MAX_PATH_LENGTH)
					continue;

				// if adjacent node is not a part of the current path
				if (!path.contains(x)) {

					// create a new path extended previous by adding x
					pathNew = new Path(path);
					pathNew.add(x);

					// edge sign
					sign = en.getValue();

					// only when sign is negative we have a change in path sign
					if (sign == -1)
						pathNew.setSign((pathNew.getSign() == 1) ? -1 : 1);

					directEdgeSign = src.getAdjacencyAsMap().get(x);

					// for positive/negative paths check if they are
					// structurally balanced
					if ((directEdgeSign != null && directEdgeSign == 1)
							|| (pathNew.getSign() == 1 && directEdgeSign == null)) {
						if (pathNew.length() < posDist.get(x) && SBCheck(pathNew, src)) {
							posDist.put(x, pathNew.length());

							if (Config.RUN_HEURISTIC)
								q.offer(pathNew);
						}
					} else if (pathNew.length() < negDist.get(x) && SBCheck(pathNew, src)) {
						negDist.put(x, pathNew.length());

						if (Config.RUN_HEURISTIC)
							q.offer(pathNew);
					}

					if (!Config.RUN_HEURISTIC)
						q.offer(pathNew);
				}
			}

		}

		// for each node in the graph
		for (Node trg : g.getNodes()) {

			// write shortest path compatibility matrix for trg that were
			// not selected as source node and are connected to src
			if ((posDist.get(trg) != INF || negDist.get(trg) != INF) && trg.getID() > src.getID())
				w.write(src.getID() + "\t" + trg.getID() + "\t" + posDist.get(trg) + "\t" + negDist.get(trg) + "\n");
		}
	}

	/**
	 * Calls first phase of network balance check and performs the second phase
	 *
	 * @param p
	 * @return
	 */
	private static boolean SBCheck(Path p, Node src) {

		Graph rG = createReducedGraph(p, src);

		// if is null then graph is unbalanced
		if (rG == null)
			return false;

		Queue<Node> q = new LinkedList<Node>();

		// stores for each node its bfs search depth
		Map<Node, Counter> d = new HashMap<Node, Counter>();
		Set<Node> visited = new HashSet<Node>();
		Node n;
		Counter depth, depth_;

		// start randomly from a node
		n = rG.getNodes().iterator().next();
		q.offer(n);
		d.put(n, new Counter(0));

		while (!q.isEmpty()) {

			n = q.poll();
			depth = d.get(n);

			// for all adjacent nodes
			for (Node trg : n.getAdjacency()) {

				// there is an edge connecting two nodes in the same layer of
				// the bfs search
				if (d.containsKey(trg) && d.get(n).getValue() == d.get(trg).getValue())
					return false;

				if (!visited.contains(trg)) {
					q.offer(trg);

					// update bfs search depth
					if ((depth_ = d.get(trg)) == null) {
						depth_ = new Counter(0);
						d.put(trg, depth_);
					}

					depth_.increase(depth.getValue() + 1);
					visited.add(trg);
				}
			}
		}
		return true;
	}

	/**
	 * Create and return the reduced graph iff the intra nodes of supernodes are
	 * balanced
	 * 
	 * @param p
	 * @return
	 */
	private static Graph createReducedGraph(Path p, Node src_) {
		Graph g_ = new Graph();
		Node trg;
		int sign;

		// for all nodes in the path
		for (Node src : p.getNodes()) {
			g_.addNode(src.getID());

			for (Entry<Node, Integer> en : src.getAdjacencyAsMap().entrySet()) {

				// adjacent node
				trg = en.getKey();

				// iff the adjacent node is also part of the path
				if (p.getNodes().contains(trg)) {

					// edge sign
					sign = en.getValue().intValue();

					// add node
					g_.addNode(trg.getID());

					// add edge
					g_.addEdge(src.getID(), trg.getID(), sign);
					g_.addEdge(trg.getID(), src.getID(), sign);
				}
			}
		}

		trg = p.getLastNode();

		// connect src to target iff the edge src -> trg does not exist
		if (g_.getNode(src_.getID()).getAdjacencyAsMap().get(trg) == null) {

			// add edge
			g_.addEdge(src_.getID(), trg.getID(), p.getSign());
			g_.addEdge(trg.getID(), src_.getID(), p.getSign());
		}

		// run quick union algorithm
		WQuickUnionPC wqupc = new WQuickUnionPC(g_.getNodes());
		Map<Integer, Set<Node>> superNodes = new HashMap<>();
		Set<Node> nodes;
		int sId;

		// construction of superNodes
		for (Node n : g_.getNodes()) {

			// component id
			sId = wqupc.getComponentID(n);

			// add all nodes that are contained to same component to a set
			if ((nodes = superNodes.get(sId)) == null) {
				nodes = new HashSet<>();
				superNodes.put(sId, nodes);
			}

			nodes.add(n);
		}

		// return the converted reduced graph
		return convertSuperToReducedGraph(superNodes, wqupc);
	}

	/**
	 * Convert graph to reduce graph and check for the first balance constraint
	 * 
	 * @param superNodes
	 * @param wqupc
	 * @return
	 */
	private static Graph convertSuperToReducedGraph(Map<Integer, Set<Node>> superNodes, WQuickUnionPC wqupc) {
		Graph rG = new Graph();
		Set<Node> component;
		Node trg;
		int cId, cId_, negativeEdges;

		// for all components
		for (Entry<Integer, Set<Node>> entry : superNodes.entrySet()) {

			// component id
			cId = entry.getKey();

			// intra nodes of cId component
			component = entry.getValue();

			// keep track of the number of negative edges
			negativeEdges = 0;

			// create a supernode
			rG.addNode(cId);

			// for all nodes in component
			for (Node src : component) {

				// for all adjacent nodes
				for (Entry<Node, Integer> en : src.getAdjacencyAsMap().entrySet()) {

					// trg node
					trg = en.getKey();

					// trg nodes' component id
					cId_ = wqupc.getComponentID(trg);

					// connect supernodes
					if (cId != cId_) {

						// create cId_ supernode
						rG.addNode(cId_);

						// create edges
						rG.addEdge(cId, cId_, -1);
						rG.addEdge(cId_, cId, -1);

					}

					// if sign is negative and trg node are within the same
					// component
					if (en.getValue() == -1 && component.contains(trg))
						negativeEdges++;
				}
			}

			// since is undirected we have count an edge twice
			negativeEdges /= 2;

			// if there are odd negative edges the graph is unbalanced and
			// return null
			if ((negativeEdges % 2) != 0)
				return null;
		}
		return rG;
	}

}
