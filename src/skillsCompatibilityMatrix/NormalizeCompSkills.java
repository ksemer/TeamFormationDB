package skillsCompatibilityMatrix;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Normalize Compatible Skills
 * 
 * @author ksemer
 *
 */
public class NormalizeCompSkills {

	// dataset
	private static String dataset = "epinions";

	// main dir of compatibility lists
	private static String pathCompatibility = "compatibilitySkills/";

	// dataset's network folder
	private static String datasetsFolder = "data/";

	// skill -> list of users
	private static Map<String, List<Integer>> skills_users;

	// decimal format
	private static DecimalFormat dF = new DecimalFormat("#.###");

	/**
	 * Main
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		String compFile;

		skills_users = loadSkills();

		compFile = "more_positive_paths";
		normalize(compFile);

		compFile = "no_negative_paths";
		normalize(compFile);

		compFile = "one_positive_path";
		normalize(compFile);

		compFile = "sbp_heuristic";
		normalize(compFile);

		compFile = "sbp";
		normalize(compFile);

		compFile = "no_negative_edge";
		normalize(compFile);
	}

	/**
	 * Normalize
	 * 
	 * @param compFile
	 * @throws IOException
	 */
	private static void normalize(String compFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(pathCompatibility + dataset + "/" + compFile + ".txt"));
		String line = null;
		String[] token;
		int num_comp, combinations;
		List<Integer> skill1_users, skill2_users;

		FileWriter w = new FileWriter(pathCompatibility + dataset + "/" + compFile + "_norm.txt");

		while ((line = br.readLine()) != null) {
			token = line.split("\t");

			skill1_users = skills_users.get(token[0]);
			skill2_users = skills_users.get(token[1]);
			num_comp = Integer.parseInt(token[2]);

			combinations = skill1_users.size() * skill2_users.size();

			// normalization function
			w.write(token[0] + "\t" + token[1] + "\t" + dF.format(((double) num_comp) / combinations) + "\n");
		}
		br.close();
		w.close();
	}

	/**
	 * Load users per skill
	 * 
	 * @return
	 * 
	 * @throws IOException
	 */
	private static Map<String, List<Integer>> loadSkills() throws IOException {

		String line = null, skill;
		String[] token, users;
		int user;
		List<Integer> users_ = null;
		Map<String, List<Integer>> skills_users = new HashMap<>();
		BufferedReader br = new BufferedReader(new FileReader(datasetsFolder + dataset + "/skills.txt"));

		System.out.println("Skills loading....");

		while ((line = br.readLine()) != null) {
			token = line.split("\\s+");
			skill = "";

			for (int i = 0; i < token.length - 1; i++)
				skill += token[i] + " ";

			skill = skill.trim();
			users = token[token.length - 1].split(",");

			if ((users_ = skills_users.get(skill)) == null) {
				users_ = new ArrayList<>();
				skills_users.put(skill, users_);
			}

			for (String u : users) {
				user = Integer.parseInt(u);
				users_.add(user);
			}
		}
		br.close();

		System.out.println("Skills loaded....");
		return skills_users;
	}
}