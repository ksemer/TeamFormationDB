package mainPackage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import algorithm.TeamFormationAlgorithm;
import inputHandler.InputManager;
import inputHandler.SkillInfo;

public class Main {

	public static void main(String[] args){
		
		//********** PARAMETERS **********//*
		int numOfSkills=Integer.parseInt(args[0]);
		String dataset=args[1];
		// 0 : sbp, 1 : no_negative_paths, 2 : more_positive_paths, 3 : one_positive_path, 4 : no_negative_edge, -1 : only for slashdot sbp not heuristic
		int compatibility_mode=Integer.parseInt(args[2]);
		//modes: spc, sbp
		String mode;
		//String task=args[3];
		boolean most_compatibles_mode=true;
		//********** ********** **********//*
		
		if(compatibility_mode>0){
			mode="spc";
		}
		else{
			mode="sbp";
		}
		
		String resultPath;
		String distributionPath;
		
		String spl="";		
		if(dataset.equals("epinions")){
			spl="\t";
		}
		else{
			spl=",";
		}
		
		System.out.print("Started at: ");
		System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
		
		System.out.println("Num of skills: "+numOfSkills+", Compatibility mode: "+compatibility_mode);
		
		if(compatibility_mode==1){
			resultPath="/home/formation/Desktop/results/"+dataset+"/"+dataset+"_"+numOfSkills+"_no_negative_paths.txt";
			distributionPath="compatibles_distribution/"+dataset+"/"+"no_negative_paths.txt";
		}
		else if(compatibility_mode==2){
			resultPath="/home/formation/Desktop/results/"+dataset+"/"+dataset+"_"+numOfSkills+"_more_positive_paths.txt";
			distributionPath="compatibles_distribution/"+dataset+"/"+"more_positive_paths.txt";
		}
		else if(compatibility_mode==3){
			resultPath="/home/formation/Desktop/results/"+dataset+"/"+dataset+"_"+numOfSkills+"_one_positive_path.txt";
			distributionPath="compatibles_distribution/"+dataset+"/"+"one_positive_path.txt";
		}
		else if(compatibility_mode==4){
			resultPath="/home/formation/Desktop/results/"+dataset+"/"+dataset+"_"+numOfSkills+"_no_negative_edge.txt";
			distributionPath="compatibles_distribution/"+dataset+"/"+"no_negative_edge.txt";
		}	
		else if(compatibility_mode==0){
			resultPath="/home/formation/Desktop/results/"+dataset+"/"+dataset+"_"+numOfSkills+"_sbp.txt";
			distributionPath="compatibles_distribution/"+dataset+"/"+"sbp.txt";
		}
		else{
			resultPath="/home/formation/Desktop/results/"+dataset+"/"+dataset+"_"+numOfSkills+"_sbp_not_heuristic.txt";
			distributionPath="compatibles_distribution/"+dataset+"/"+"sbp_not_heuristic.txt";
		}

		String inputPath = "tasks/"+dataset+"/"+dataset+"_"+numOfSkills+".txt";
		//String inputPath = "tasks/"+dataset+"/rare>1_"+dataset+"_"+numOfSkills+".txt";
		
		Scanner inputReader;
		ArrayList<String> tasks = new ArrayList<String>();
		File file = new File(inputPath);
		try 
		 { 
			 inputReader = new Scanner(new FileInputStream(file)); 
			 while(inputReader.hasNextLine()){
				tasks.add(inputReader.nextLine());
			 }
			inputReader.close();
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.printf("File %s was not found or could not be opened.\n",inputPath); 
		 }
		
		HashMap<Integer,Integer> compatiblesDistribution = new HashMap<Integer,Integer>();
		
		if(most_compatibles_mode==true){
			Scanner inputReader1;
			File file1 = new File(distributionPath);
			try 
			 { 
				 inputReader1 = new Scanner(new FileInputStream(file1)); 
				 while(inputReader1.hasNextLine()){
					String tokens[]=inputReader1.nextLine().split(spl);
					compatiblesDistribution.put(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
				 }
				inputReader1.close();
			 } 
			 catch(FileNotFoundException e) 
			 { 
				 System.out.printf("File %s was not found or could not be opened.\n",inputPath); 
			 }
		}
		
		
		for(int i=0;i<tasks.size();i++){
		
			System.out.print("Starting iteration "+i+" at :");
			System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
				
			String networkPath="data/"+dataset+"/network.txt"; 
			
			String userPath="data/"+dataset+"/users.txt";
			String skillPath="data/"+dataset+"/skills.txt";
			
			
			/*String userPath="data/"+dataset+"/users_rare>1.txt";
			String skillPath="data/"+dataset+"/skills_rare>1.txt";*/
			
			
			String result;
			
			ArrayList<String> initialTask = new ArrayList<String>();
			String skills[] = tasks.get(i).split(",");
			for(int j=0;j<skills.length;j++){
				initialTask.add(skills[j]);
			}
				
			if(compatibility_mode<4){
				result=runCompatibilityAlgorithm(compatiblesDistribution,mode,initialTask,dataset,compatibility_mode,numOfSkills, networkPath, userPath, skillPath);
				
			}
			else{
				result=runNoNegativeAlgorithm(compatiblesDistribution,mode,initialTask,dataset,compatibility_mode,numOfSkills, networkPath, userPath, skillPath);
			}
			
			FileWriter writer = new FileWriter(resultPath);
			writer.initAppendWriter();
			writer.appendLine(result);
			
			//System.out.println(result);
			
			System.out.print("Finished iteration "+i+" at :");
			System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
		}	
		
		System.out.print("Finished at: ");
		System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
	}
	
	
	public static String runCompatibilityAlgorithm(HashMap<Integer,Integer> compatiblesDistribution,String mode,ArrayList<String> initialTask,String dataset,int compatibility_mode,int numOfSkills,String networkPath, String userPath, String skillPath){
		InputManager manager = new InputManager(networkPath,userPath,skillPath);
		manager.retrieveSkillInfo();
		
		//ArrayList<String> initialTask = produceTask(numOfSkills,manager.getSkillInfo());
				
		TeamFormationAlgorithm algorithm = new TeamFormationAlgorithm(compatiblesDistribution,mode,dataset,compatibility_mode,initialTask,manager.getSkillInfo());
		algorithm.start();
		
		return algorithm.getResult();
	}
	
	public static String runNoNegativeAlgorithm(HashMap<Integer,Integer> compatiblesDistribution,String mode,ArrayList<String> initialTask,String dataset,int compatibility_mode,int numOfSkills,String networkPath, String userPath, String skillPath){
		InputManager manager = new InputManager(networkPath,userPath,skillPath);
		manager.retrieveNetwork();
		manager.retrieveSkillInfo();
		
		//ArrayList<String> initialTask = produceTask(numOfSkills,manager.getSkillInfo());
				
		TeamFormationAlgorithm algorithm = new TeamFormationAlgorithm(compatiblesDistribution,mode,dataset,compatibility_mode,initialTask,manager.getNetwork(), manager.getSkillInfo());
		algorithm.start();
		
		return algorithm.getResult();
		
	}
	
	/*public static ArrayList<String> produceTask(int numOfTasks, SkillInfo skillInfo){
		ArrayList<String> initialTask = new ArrayList<String>();
		Random rndGen = new Random();
		for(int i=0;i<numOfTasks;i++){
			String temp = skillInfo.getSkills().get(rndGen.nextInt(skillInfo.getSkills().size()));
			initialTask.add(temp);
		}
		return initialTask;
	}*/
}
