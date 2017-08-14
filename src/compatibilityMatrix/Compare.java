package compatibilityMatrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Compare {
	public static final int INF = 1000000000;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("slashdot_sbp1"));
		BufferedReader br1 = new BufferedReader(new FileReader("slashdot_sbp"));

		String line = null, line1 = null;
		String[] token, token1;
		int misses = 0, total = 0, equal = 0;
		
		while((line = br.readLine()) != null) {
			line1 = br1.readLine();
			
			token = line.split("\t");
			token1 = line1.split("\t");
			
			if (!(token[0].equals(token1[0]) && token[1].equals(token1[1])))
				System.out.println("ERROR");
			
			if (token[2].equals("" + INF) && !token1[2].equals("" + INF))
				misses++;
			else if (token[2].equals(token1[2]))
				equal++;
			else
				System.out.println(line + "\t" + line1);
			
			total++;
		}
		br.close();
		br1.close();
		
		System.out.println(misses + "/" + total + "\t" + equal);
	}
}