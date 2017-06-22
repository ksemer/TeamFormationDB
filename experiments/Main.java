import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import algorithm.TeamFormationAlgorithm;
import inputHandler.InputManager;
import inputHandler.SkillInfo;

public class Main {

	public static void main(String[] args){
		
		/********** PARAMETERS **********/
	
		int numOfSkills=Integer.parseInt(args[0]);
		String dataset=args[1];
		// 1 : no_negative_paths, 2 : more_positive_paths, 3 : one_positive_path, 4 : no_negative_edge 
		int compatibility_mode=Integer.parseInt(args[2]);
		String task=args[3];
		
		/********** ********** **********/
		
		//System.out.print("Started at: ");
		//System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
		
		ArrayList<String> initialTask = new ArrayList<String>();
		String skills[] = task.split(",");
		for(int i=0;i<skills.length;i++){
			initialTask.add(skills[i]);
		}
	
		String pairValuesPath;
		
		if(compatibility_mode==1){
			pairValuesPath="/home/formation/Desktop/compatibilityLists/"+dataset+"/no_negative_paths.txt";
		}
		else if(compatibility_mode==2){
			pairValuesPath="/home/formation/Desktop/compatibilityLists/"+dataset+"/more_positive_paths.txt";
		}
		else if(compatibility_mode==3){
			pairValuesPath="/home/formation/Desktop/compatibilityLists/"+dataset+"/one_positive_path.txt";
		}
		else{
			pairValuesPath="/home/formation/Desktop/compatibilityLists/"+dataset+"/distances.txt";
		}
			
		String networkPath="data/"+dataset+"/network.txt"; 
		String userPath="data/"+dataset+"/users.txt";
		String skillPath="data/"+dataset+"/skills.txt";
			
		if(compatibility_mode<4){
			String result=runCompatibilityAlgorithm(initialTask,numOfSkills,pairValuesPath, networkPath, userPath, skillPath);
			System.out.println(result);
		}
		else{
			String result=runNoNegativeAlgorithm(initialTask,numOfSkills, pairValuesPath, networkPath, userPath, skillPath);
			System.out.println(result);
		}

		//System.out.print("Finished at: ");
		//System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
	}
	
	
	public static String runCompatibilityAlgorithm(ArrayList<String> initialTask,int numOfSkills,String pairValuesPath, String networkPath, String userPath, String skillPath){
		InputManager manager = new InputManager(pairValuesPath,networkPath,userPath,skillPath);
		manager.retrieveSkillInfo();
		
		
		manager.retrievePairInfo(initialTask);
		
		TeamFormationAlgorithm algorithm = new TeamFormationAlgorithm(initialTask,manager.getSkillInfo(),manager.getPairInfo(),manager.getCompatibleDistances());
		algorithm.start();
		
		return algorithm.getResult();
	}
	
	public static String runNoNegativeAlgorithm(ArrayList<String> initialTask,int numOfSkills,String pairValuesPath, String networkPath, String userPath, String skillPath){
		InputManager manager = new InputManager(pairValuesPath,networkPath,userPath,skillPath);
		manager.retrieveNetwork();
		manager.retrieveSkillInfo();
		
		manager.retrieveDistancesInfo(initialTask);
		
		TeamFormationAlgorithm algorithm = new TeamFormationAlgorithm(initialTask,manager.getNetwork(), manager.getSkillInfo(),manager.getCompatibleDistances());
		algorithm.start();
		
		return algorithm.getResult();
		
	}
}
