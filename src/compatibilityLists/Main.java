package compatibilityLists;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Main {
	public static void main(String[] args){
		
		System.out.print("Started at: ");
		System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
		
		//String inputPath="/home/formation/Desktop/wikipedia_spc";
		
		//No negative & distances
		/*String outputPath="/home/formation/Desktop/compatibilityLists/wikipedia/no_negative_paths.txt";
		String distancesPath="/home/formation/Desktop/compatibilityLists/wikipedia/distances.txt";
		FileEditor fe = new FileEditor(inputPath,outputPath,distancesPath);
		fe.processNoNegative();*/	
		
		//More positive
		/*String outputPath="/home/formation/Desktop/compatibilityLists/zipf/more_positive_paths.txt";
		FileEditor fe = new FileEditor(inputPath,outputPath);
		fe.processMorePositive();*/
		 
		
		//One positive
		/*String outputPath="/home/formation/Desktop/compatibilityLists/wikipedia/one_positive_path.txt";
		FileEditor fe = new FileEditor(inputPath,outputPath);
		fe.processOnePositive();
		 
		System.out.print("Finished at: ");
		System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );*/
		
		
		//sbp
		String inputPath="/home/formation/Desktop/slashdot_sbp";
		String outputPath="/home/formation/Desktop/compatibilityLists/slashdot/sbp.txt";
		FileEditor fe = new FileEditor(inputPath,outputPath);
		fe.processSbp();
		
		System.out.print("Finished at: ");
		System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
	 
		
		
	}

}
