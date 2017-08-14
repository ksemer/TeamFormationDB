package compatibilityMatrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;

/**
 * Display Avg and max distances for SPC and SBP
 * 
 * @author ksemer
 *
 */
public class DisplayDistances {

	private static String path_sbp = "epinions_sbp_heuristic";
	private static String path_spc = "epinions_spc";
	private static final int INF = 1000000000;
	private static final boolean INTERSECTION = true;

	public static void main(String[] args) throws IOException {

		Map<Integer, Map<Integer, Integer>> sbp = new HashMap<>();
		Map<Integer, Map<Integer, Integer>> spc = new HashMap<>();
		DecimalFormat dF = new DecimalFormat("#.###");

		read(spc, path_spc);
		read(sbp, path_sbp);

		int id1, id2, dist_sbp, dist_spc;
		int count = 0, count_inf = 0;
		double dist_sbp_A = 0, dist_spc_A = 0;

		for (Entry<Integer, Map<Integer, Integer>> entry : spc.entrySet()) {
			id1 = entry.getKey();

			for (Entry<Integer, Integer> entry1 : entry.getValue().entrySet()) {
				id2 = entry1.getKey();
				dist_spc = entry1.getValue();

				Map<Integer, Integer> sbp_ = sbp.get(id1);

				if (sbp_ != null && sbp_.containsKey(id2)) {
					dist_sbp = sbp_.get(id2);
				} else {
					sbp_ = sbp.get(id2);

					if (sbp_ != null && sbp_.containsKey(id1))
						dist_sbp = sbp_.get(id1);
					else
						continue;
				}

				if (dist_sbp == INF) {
					count_inf++;
					continue;
				}

				count++;
				dist_spc_A += dist_spc;
				dist_sbp_A += dist_sbp;
			}
		}

		System.out.println("Avg dist spc: " + dF.format(dist_spc_A / count));
		System.out.println("Avg dist sbp: " + dF.format(dist_sbp_A / count));
		System.out.println("#inf sbp: " + count_inf);
		System.out.println("#pairs: " + count);
		System.out.println("SBP size: " + sbp.size());
		System.out.println("SPC size: " + spc.size());
	}

	private static void read(Map<Integer, Map<Integer, Integer>> map, String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = null;
		String[] token;
		Map<Integer, Integer> m;
		boolean flag = false;

		System.out.println(path + " is loading");

		if (path.contains("spc"))
			flag = true;

		int id1, id2, dist, pos, max_spc = 0, max_sbp = 0;

		while ((line = br.readLine()) != null) {
			token = line.split("\t");
			id1 = Integer.parseInt(token[0]);
			id2 = Integer.parseInt(token[1]);

			if (flag) {
				dist = Integer.parseInt(token[4]);
				pos = Integer.parseInt(token[2]);

				if (max_spc < dist)
					max_spc = dist;

				if (INTERSECTION) {
					if (pos == 0)
						continue;
				} else if (pos != 0)
					continue;
			} else {
				dist = Integer.parseInt(token[2]);

				if (dist != INF && max_sbp < dist)
					max_sbp = dist;
			}

			if ((m = map.get(id1)) == null) {
				m = new HashMap<>();
				map.put(id1, m);
			}

			if (!m.containsKey(id2))
				m.put(id2, dist);
		}
		br.close();

		System.out.println(path + " is loaded");

		if (flag)
			System.out.println("Max dist spc: " + max_spc);
		else
			System.out.println("Max dist sbp: " + max_sbp);
	}
}