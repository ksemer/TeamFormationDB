package compatibilityMatrix;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Queue;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * ShortestPathCompatibility matrix class
 * 
 * @author ksemer
 *
 */
public class ShortestPathCompatibility {

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
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Config.loadConfig();

		g = new Graph();
		g.load(Config.DATA_PATH);
		w = new FileWriter(Config.OUTPUT_PATH);

		int c = 0;
		List<Integer> nodes = new ArrayList<Integer>(g.getNodesAsMap().keySet());
		Collections.sort(nodes);

		// for each node in the graph
		for (int n : nodes) {
			c++;
			computeSPC(n);
			System.out.println(c + "/" + g.size());
		}
		w.close();
	}

	/**
	 * Compute shortest path compatibility
	 * 
	 * @param src
	 * @throws IOException
	 */
	private static void computeSPC(int src) throws IOException {

		// for each u its distance from src
		Map<Integer, Counter> dist = new HashMap<>(g.size());

		// for each u the number of positive shortest paths started from src
		Map<Integer, Counter> nPos = new HashMap<>(g.size());

		// for each u the number of negative shortest paths started from src
		Map<Integer, Counter> nNeg = new HashMap<>(g.size());

		// Initialize all distances as INFINITE, all positive and negative
		// shortest path to 0
		for (int i : g.getNodesAsMap().keySet()) {
			dist.put(i, new Counter(INF));
			nPos.put(i, new Counter(0));
			nNeg.put(i, new Counter(0));
		}

		Queue<Integer> q = new LinkedList<Integer>();
		q.offer(src);

		// visited contains the same elements with q
		// its used only for fast check up for nodes that are already in q
		Set<Integer> visited = new HashSet<Integer>();
		visited.add(src);

		// Distance of source node from itself is always 0
		dist.get(src).set(0);

		// Positive shortest path of source node from itself is always 1
		nPos.get(src).set(1);

		int p, x, sign;

		while (!q.isEmpty()) {
			p = q.poll();
			visited.remove(p);

			// for each adjacent node of p
			for (Entry<Node, Integer> en : g.getNode(p).getAdjacencyAsMap().entrySet()) {

				// adjacent node
				x = en.getKey().getID();

				if (dist.get(p).getValue() + 1 <= dist.get(x).getValue()) {

					// if x is not in queue
					if (!visited.contains(x)) {
						q.offer(x);
						visited.add(x);
					}

					// update distance iff the new distance is shorter
					if (dist.get(p).getValue() + 1 < dist.get(x).getValue()) {
						if (nPos.get(x).getValue() != 0 || nNeg.get(x).getValue() != 0)
							System.exit(0);
						dist.get(x).set(dist.get(p).getValue() + 1);
					}

					// edge sign
					sign = en.getValue().intValue();

					// update positive and negative shortest paths
					if (sign == 1) {
						if (nPos.get(p).getValue() > 0)
							nPos.get(x).increase(nPos.get(p).getValue());
						else if (nNeg.get(p).getValue() > 0)
							nNeg.get(x).increase(nNeg.get(p).getValue());
					} else if (sign == -1) {
						if (nPos.get(p).getValue() > 0)
							nNeg.get(x).increase(nPos.get(p).getValue());
						else if (nNeg.get(p).getValue() > 0)
							nPos.get(x).increase(nNeg.get(p).getValue());
					}
				}
			}
		}

		// for each node in the graph
		for (int trg : g.getNodesAsMap().keySet()) {

			// write shortest path compatibility matrix
			// for trg that were not selected as source node and are connected
			// to src
			if (dist.get(trg).getValue() != INF && trg > src)
				w.write(src + "\t" + trg + "\t" + nPos.get(trg).getValue() + "\t" + nNeg.get(trg).getValue() + "\t"
						+ dist.get(trg).getValue() + "\n");
		}
	}
}