package skillsCompatibilityMatrix;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Percentage of Compatible skills class
 * 
 * @author ksemer
 *
 */
public class PercentageOfCompSkills {

	// dataset
	private static String dataset = "epinions";

	// main dir of compatibility lists
	private static String pathCompatibility = "compatibilitySkills/";

	/**
	 * Main
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		String compFile;

		compFile = "no_negative_paths";
		loadSkills(compFile);

		compFile = "more_positive_paths";
		loadSkills(compFile);

		compFile = "one_positive_path";
		loadSkills(compFile);

		compFile = "sbp_heuristic";
		loadSkills(compFile);

		compFile = "sbp";
		loadSkills(compFile);

		compFile = "no_negative_edge";
		loadSkills(compFile);
	}

	/**
	 * Given a file of a pair of compatible skills compute the percentage of
	 * compatible skills
	 * 
	 * @param compFile
	 * @return
	 * @throws IOException
	 */
	private static Map<String, List<Integer>> loadSkills(String compFile) throws IOException {
		System.out.println(compFile);
		String line = null;
		String[] token;
		Map<String, List<Integer>> skills_users = new HashMap<>();
		BufferedReader br = new BufferedReader(new FileReader(pathCompatibility + dataset + "/" + compFile + ".txt"));
		Set<String> pairs = new HashSet<>();
		Set<String> skills = new HashSet<>();
		String skill_pair, skill_pair_;

		while ((line = br.readLine()) != null) {
			token = line.trim().split("\t");
			skills.add(token[0]);
			skills.add(token[1]);

			skill_pair = token[0] + "--" + token[1];
			skill_pair_ = token[1] + "--" + token[0];

			if (token[0].equals("female") || token[1].equals("male"))
				continue;

			if (token[0].equals(token[1]))
				continue;

			if (!pairs.contains(skill_pair) && !pairs.contains(skill_pair_)) {
				pairs.add(skill_pair);
			}
		}
		br.close();

		System.out.println("Total skills: " + skills.size());
		System.out.println("Pairs size: " + pairs.size() + "\nPercentage of compSkills"
				+ (double) pairs.size() / (double) (skills.size() * ((double) (skills.size() - 1) / 2)));
		return skills_users;
	}
}