package skillsCompatibilityMatrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import compatibilityMatrix.Graph;
import compatibilityMatrix.Node;

/**
 * For each pair of skills the # of times that are compatible
 * 
 * @author ksemer
 *
 */
public class CompatibleSkills {

	// dataset
	private static String dataset = "";

	// main dir of compatibility lists
	private static String pathCompatibility = "compatibilityLists/";

	// dataset's network folder
	private static String datasetsFolder = "data/";

	// user -> list of skills
	private static Map<Integer, List<String>> users_skills;

	/**
	 * Main
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		String compFile;

		users_skills = loadSkills();

		compFile = "more_positive_paths";
		runCompatibilitySkills(compFile);

		compFile = "no_negative_paths";
		runCompatibilitySkills(compFile);

		compFile = "one_positive_path";
		runCompatibilitySkills(compFile);

		compFile = "sbp_heuristic";
		runCompatibilitySkills(compFile);

		compFile = "percentage_positive_paths";
		runCompatibilitySkillsPosPercent(compFile);

		compFile = "sbp";
		runCompatibilitySkills(compFile);

		compFile = "no_negative_edge";
		runCompatibilitySkillsNoNegEdge(compFile);
	}

	/**
	 * Compute compatibility skills usign percentage of positive paths
	 * 
	 * @param dataset
	 * @param compFile
	 * @throws IOException
	 */
	private static void runCompatibilitySkillsPosPercent(String compFile) throws IOException {
		System.out.println("Computation for dataset: " + dataset + " and compfile: " + compFile + " has started...");

		DecimalFormat decimalFormat = new DecimalFormat("#.###");
		Map<String, Counter> comp = new HashMap<>();
		BufferedReader br = new BufferedReader(new FileReader(pathCompatibility + dataset + "/" + compFile + ".txt"));
		String line = null;
		String[] token;
		List<String> userA_skills, userB_skills;
		Counter c1, c2 = null;
		int countNull = 0, userA, userB;
		double percentage;

		while ((line = br.readLine()) != null) {
			token = line.split("\\s+");

			userA = Integer.parseInt(token[0]);
			userB = Integer.parseInt(token[1]);
			percentage = Double.parseDouble(token[2]);
			userA_skills = users_skills.get(userA);
			userB_skills = users_skills.get(userB);

			if (userA_skills == null || userB_skills == null) {
				countNull++;
				continue;
			}

			for (String skillA : userA_skills) {
				for (String skillB : userB_skills) {
					if ((c1 = comp.get(skillA + "," + skillB)) == null
							&& (c2 = comp.get(skillB + "," + skillA)) == null) {
						c1 = new Counter(0.0);
						comp.put(skillA + "," + skillB, c1);
					}

					if (c1 != null)
						c1.increase(percentage);
					else if (c2 != null)
						c2.increase(percentage);
				}
			}
		}
		br.close();
		System.out.println("Pairs with at least one user without any skill: " + countNull);

		FileWriter w = new FileWriter("compatibilitySkills_" + compFile + "_" + dataset + ".txt");
		String skills;

		for (Entry<String, Counter> entry : comp.entrySet()) {
			skills = entry.getKey().replace(",", "\t");
			w.write(skills + "\t" + decimalFormat.format(entry.getValue().getDoubleValue()) + "\n");
		}
		w.close();
		System.out.println("Finished....");
	}

	/**
	 * Compute compatibility skills
	 * 
	 * @param compFile
	 * @throws IOException
	 */
	private static void runCompatibilitySkills(String compFile) throws IOException {

		System.out.println("Computation for dataset: " + dataset + " and compfile: " + compFile + " has started...");

		Map<String, Counter> comp = new HashMap<>();
		BufferedReader br = new BufferedReader(new FileReader(pathCompatibility + dataset + "/" + compFile + ".txt"));
		String line = null;
		String[] token;
		List<String> userA_skills, userB_skills;
		Counter c1, c2 = null;
		int countNull = 0, userA, userB;

		while ((line = br.readLine()) != null) {
			token = line.split("\\s+");

			userA = Integer.parseInt(token[0]);
			userB = Integer.parseInt(token[1]);
			userA_skills = users_skills.get(userA);
			userB_skills = users_skills.get(userB);

			if (userA_skills == null || userB_skills == null) {
				countNull++;
				continue;
			}

			for (String skillA : userA_skills) {
				for (String skillB : userB_skills) {
					if ((c1 = comp.get(skillA + "," + skillB)) == null
							&& (c2 = comp.get(skillB + "," + skillA)) == null) {
						c1 = new Counter(0);
						comp.put(skillA + "," + skillB, c1);
					}

					if (c1 != null)
						c1.increase(1);
					else if (c2 != null)
						c2.increase(1);
				}
			}
		}
		br.close();
		System.out.println("Pairs with at least one user without any skill: " + countNull);

		FileWriter w = new FileWriter("compatibilitySkills_" + compFile + "_" + dataset + ".txt");
		String skills;

		for (Entry<String, Counter> entry : comp.entrySet()) {
			skills = entry.getKey().replace(",", "\t");
			w.write(skills + "\t" + entry.getValue().getIntValue() + "\n");
		}
		w.close();
		System.out.println("Finished....");
	}

	/**
	 * Compute compatibility skills for no negative edge
	 * 
	 * @param compFile
	 * @throws IOException
	 */
	private static void runCompatibilitySkillsNoNegEdge(String compFile) throws IOException {

		System.out.println("Computation for dataset: " + dataset + " and compfile: " + compFile + " has started...");

		Map<String, Counter> comp = new HashMap<>();
		List<String> userA_skills, userB_skills;
		Counter c1, c2 = null;
		int countNull = 0, userA, userB;
		Graph g = new Graph();
		g.load(datasetsFolder + dataset + "/network.txt");

		List<Integer> nodes = new ArrayList<Integer>(g.getNodesAsMap().keySet());
		Collections.sort(nodes);
		Map<Node, Integer> neighbors;
		Integer sign;

		// for each node in the graph
		for (int i = 0; i < nodes.size(); i++) {
			userA = nodes.get(i);
			neighbors = g.getNode(userA).getAdjacencyAsMap();

			for (int j = i + 1; j < nodes.size(); j++) {
				userB = nodes.get(j);

				if ((sign = neighbors.get(g.getNode(userB))) != null && sign == -1)
					continue;

				userA_skills = users_skills.get(userA);
				userB_skills = users_skills.get(userB);

				if (userA_skills == null || userB_skills == null) {
					countNull++;
					continue;
				}

				for (String skillA : userA_skills) {
					for (String skillB : userB_skills) {
						if ((c1 = comp.get(skillA + "," + skillB)) == null
								&& (c2 = comp.get(skillB + "," + skillA)) == null) {
							c1 = new Counter(0);
							comp.put(skillA + "," + skillB, c1);
						}

						if (c1 != null)
							c1.increase(1);
						else if (c2 != null)
							c2.increase(1);
					}
				}
			}
		}

		System.out.println("Pairs with at least one user without any skill: " + countNull);

		FileWriter w = new FileWriter("compatibilitySkills_" + compFile + "_" + dataset + ".txt");
		String skills;

		for (Entry<String, Counter> entry : comp.entrySet()) {
			skills = entry.getKey().replace(",", "\t");
			w.write(skills + "\t" + entry.getValue().getIntValue() + "\n");
		}
		w.close();
		System.out.println("Finished....");
	}

	/**
	 * Load skills per user
	 * 
	 * @return
	 * 
	 * @throws IOException
	 */
	private static Map<Integer, List<String>> loadSkills() throws IOException {

		String line = null, skill;
		String[] token, users;
		int user;
		List<String> skills = null;
		Map<Integer, List<String>> users_skills = new HashMap<>();
		BufferedReader br = new BufferedReader(new FileReader(datasetsFolder + dataset + "/skills.txt"));

		System.out.println("Skills loading....");

		while ((line = br.readLine()) != null) {
			token = line.split("\\s+");
			skill = "";

			for (int i = 0; i < token.length - 1; i++)
				skill += token[i] + " ";

			skill = skill.trim();
			users = token[token.length - 1].split(",");

			for (String u : users) {
				user = Integer.parseInt(u);

				if ((skills = users_skills.get(user)) == null) {
					skills = new ArrayList<>();
					users_skills.put(user, skills);
				}

				skills.add(skill);
			}
		}
		br.close();

		System.out.println("Skills loaded....");
		return users_skills;
	}
}
