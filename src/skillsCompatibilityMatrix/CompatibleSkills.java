package skillsCompatibilityMatrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import compatibilityMatrix.Counter;

/**
 * For each pair of skills the # of times that are compatible
 * 
 * @author ksemer
 *
 */
public class CompatibleSkills {

	private static String dataset = "wikipedia";
	private static String pathCompatibility = "compatibilityLists/" + dataset + "more_positive_paths.txt";
	private static String pathSkills = "data/" + dataset + "/skills.txt";
	private static String output = "compatible_" + dataset;
	private static Map<Integer, List<String>> users_skills;

	/**
	 * Main
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		users_skills = new HashMap<>();
		loadSkills();

		Map<String, Counter> comp = new HashMap<>();
		BufferedReader br = new BufferedReader(new FileReader(pathCompatibility));
		String line = null;
		String[] token;
		List<String> userA_skills, userB_skills;
		Counter c1, c2 = null;
		int countNull = 0, count = 0;

		while ((line = br.readLine()) != null) {
			token = line.split("\\s+");
			System.out.println(++count);

			userA_skills = users_skills.get(Integer.parseInt(token[0]));
			userB_skills = users_skills.get(Integer.parseInt(token[1]));

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

		FileWriter w = new FileWriter(output);
		String skills;

		for (Entry<String, Counter> entry : comp.entrySet()) {
			skills = entry.getKey().replace(",", "\t");
			w.write(skills + "\t" + entry.getValue().getValue() + "\n");
		}
		w.close();
		System.out.println("Finished....");
	}

	/**
	 * Load skills per user
	 * 
	 * @throws IOException
	 */
	private static void loadSkills() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(pathSkills));
		String line = null, skill;
		String[] token, users;
		int user;
		List<String> skills = null;
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
	}
}
