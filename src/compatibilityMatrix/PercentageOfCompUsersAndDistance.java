package compatibilityMatrix;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PercentageOfCompUsersAndDistance {

	private static String dataset = "epinions";

	// main dir of compatibility lists
	private static String pathCompatibility = "compatibilityLists/";

	// dataset's network folder
	private static String datasetsFolder = "data/";

	// giant component path
	private static String giantCompPath = datasetsFolder + dataset + "/bcc_users.txt";

	/**
	 * Main
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		distance("no_negative_paths.txt");
		distance("more_positive_paths.txt");
		distance("one_positive_path.txt");
		distance("sbp_heuristic.txt");
		distance("sbp.txt");
		distanceNoNegEdge();
	}

	/**
	 * Returns the average distance and the percentage of compatible users for a
	 * given compatibility
	 * 
	 * @param comp
	 * @throws IOException
	 */
	private static void distance(String comp) throws IOException {
		System.out.println(comp);
		BufferedReader br = new BufferedReader(new FileReader(pathCompatibility + dataset + "/" + comp));
		String line = null;
		String[] token = null;
		double distance = 0;
		int count = 0, user1, user2;
		Set<Integer> users = getUsersInGiantComponent();

		while ((line = br.readLine()) != null) {
			token = line.split("\t");
			user1 = Integer.parseInt(token[0]);
			user2 = Integer.parseInt(token[1]);

			if (!users.contains(user1) || !users.contains(user2))
				continue;

			count++;
			distance += Integer.parseInt(token[2]);
		}
		br.close();

		double perc = (double) count / (double) (users.size() * ((double) (users.size() - 1) / 2));
		System.out.println(distance / count + "\t" + count + "\t" + users.size() + "\t" + perc);
	}

	/**
	 * Returns the average distance and the percentage of compatible users for
	 * NNE compatibility
	 * 
	 * @throws IOException
	 */
	private static void distanceNoNegEdge() throws IOException {
		System.out.println("NoNegEdge");
		Graph g = new Graph();
		g.load(datasetsFolder + dataset + "/network.txt");

		BufferedReader br = new BufferedReader(new FileReader(pathCompatibility + dataset + "/distances.txt"));
		String line = null;
		String[] token = null;
		double distance = 0;
		int count = 0, user1, user2;
		Integer sign;
		Map<Node, Integer> neighbors;
		Set<Integer> users = getUsersInGiantComponent();

		while ((line = br.readLine()) != null) {
			token = line.split("\t");
			user1 = Integer.parseInt(token[0]);
			user2 = Integer.parseInt(token[1]);

			if (user1 == user2)
				continue;

			if (!users.contains(user1) || !users.contains(user2))
				continue;

			neighbors = g.getNode(user1).getAdjacencyAsMap();

			if ((sign = neighbors.get(g.getNode(user2))) != null && sign == -1)
				continue;

			count++;
			distance += Integer.parseInt(token[2]);
		}
		br.close();

		double perc = (double) count / (double) (users.size() * ((double) (users.size() - 1) / 2));
		System.out.println("Avg dist: " + distance / count + "\nUsers in giant: " + users.size()
				+ "\nPercentage of comp Users" + perc);
	}

	/**
	 * Return all users that are contained in giant component
	 * 
	 * @return
	 * @throws IOException
	 */
	private static Set<Integer> getUsersInGiantComponent() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(giantCompPath));
		String line = null;
		String[] token = null;
		Set<Integer> users = new HashSet<>();

		while ((line = br.readLine()) != null) {
			token = line.split("\t");

			users.add(Integer.parseInt(token[0]));
		}
		br.close();

		return users;
	}
}
