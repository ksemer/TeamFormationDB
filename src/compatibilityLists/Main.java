package compatibilityLists;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Main {
	public static void main(String[] args){
		String dataset="got";
		
		
		//System.out.print("Started at: ");
		//System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
		
		String inputPath="/home/formation/Desktop/"+dataset+"_spc";
		
		//No negative & distances
		String outputPath="/home/formation/Desktop/compatibilityLists/"+dataset+"/no_negative_paths.txt";
		String distancesPath="/home/formation/Desktop/compatibilityLists/"+dataset+"/distances.txt";
		FileEditor fe = new FileEditor(inputPath,outputPath,distancesPath);
		fe.processNoNegative();	
		
		//More positive
		outputPath="/home/formation/Desktop/compatibilityLists/"+dataset+"/more_positive_paths.txt";
		fe = new FileEditor(inputPath,outputPath);
		fe.processMorePositive();
		 
		
		//One positive
		outputPath="/home/formation/Desktop/compatibilityLists/"+dataset+"/one_positive_path.txt";
		fe = new FileEditor(inputPath,outputPath);
		fe.processOnePositive();
		 
		
		//sbp
		inputPath="/home/formation/Desktop/"+dataset+"_sbp";
		outputPath="/home/formation/Desktop/compatibilityLists/"+dataset+"/sbp.txt";
		fe = new FileEditor(inputPath,outputPath);
		fe.processSbp();
		
		//sbp heuristic
		inputPath="/home/formation/Desktop/"+dataset+"_sbp_heuristic";
		outputPath="/home/formation/Desktop/compatibilityLists/"+dataset+"/sbp_heuristic.txt";
		fe = new FileEditor(inputPath,outputPath);
		fe.processSbp();
		
		//System.out.print("Finished at: ");
		//System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
	 
		
		
	}

}
