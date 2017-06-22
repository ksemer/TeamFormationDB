package compatibilityLists;

import java.util.ArrayList;

public class Main {
	public static void main(String[] args){
		
		String inputPath="slashdot\\slashdot_spc";
		
		//No negative & distances
		String outputPath="slashdot\\slashdot_no_negative_paths.txt";
		String distancesPath="slashdot\\slashdot_distances.txt";
		
		FileEditor fe = new FileEditor(inputPath,outputPath,distancesPath);
		fe.processNoNegative();
		
		//More positive
		/*String outputPath="slashdot\\slashdot_more_positive_paths.txt";
		FileEditor fe = new FileEditor(inputPath,outputPath);
		fe.processMorePositive();*/
		 
		
		//One positive
		/*String outputPath="slashdot\\slashdot_one_positive_path.txt";
		FileEditor fe = new FileEditor(inputPath,outputPath);
		fe.processOnePositive();*/
		 
		
		
	}

}
