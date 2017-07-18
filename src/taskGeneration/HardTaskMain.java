package taskGeneration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import inputHandler.InputManager;

public class HardTaskMain {

	public static void main(String[] args){
		String dataset="slashdot";
		int compatibility_mode=1;
		String compatibleSkillPath="";
		
		System.out.println("Dataset: "+dataset);
		System.out.println("Comp_mode: "+compatibility_mode);
		
		if(compatibility_mode==1){
			compatibleSkillPath="skillsCompatibilityMatrix/"+dataset+"/"+dataset+"_no_negative_paths.txt";
		}
		else if(compatibility_mode==2){
			compatibleSkillPath="skillsCompatibilityMatrix/"+dataset+"/"+dataset+"_more_positive_paths.txt";
		}
		else if(compatibility_mode==3){
			compatibleSkillPath="skillsCompatibilityMatrix/"+dataset+"/"+dataset+"_one_positive_path.txt";
		}
		else if(compatibility_mode==4){
			compatibleSkillPath="skillsCompatibilityMatrix/"+dataset+"/"+dataset+"_no_negative_edge.txt";
		}	
		else if(compatibility_mode==0){
			compatibleSkillPath="skillsCompatibilityMatrix/"+dataset+"/"+dataset+"_sbp.txt";
		}
		else{
			compatibleSkillPath="skillsCompatibilityMatrix/"+dataset+"/"+dataset+"_sbp_not_heuristic.txt";
		}
	
		HashMap<String,HashMap<String,Integer>> skillsCompatibilityMatrix = new HashMap<String,HashMap<String,Integer>>();
		
		Scanner inputReader0;
		File file0 = new File(compatibleSkillPath);
		try 
		 { 
			inputReader0 = new Scanner(new FileInputStream(file0)); 
			 while(inputReader0.hasNextLine()){
				String tokens[]=inputReader0.nextLine().split("\t");
				if(!skillsCompatibilityMatrix.containsKey(tokens[0])){
					HashMap<String,Integer> tmp = new HashMap<String,Integer>();
					tmp.put(tokens[1], Integer.parseInt(tokens[2]));
					skillsCompatibilityMatrix.put(tokens[0], tmp);
				}
				else{
					skillsCompatibilityMatrix.get(tokens[0]).put(tokens[1],Integer.parseInt(tokens[2]));
				}
			 }
			 inputReader0.close();
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.println("File not found!");
		 }
			 
		
		TaskGenerator tG = new TaskGenerator();
		
		System.out.println("Getting avg_compatibility");
		
		double avg=tG.getAvgVompatibility(skillsCompatibilityMatrix);
		System.out.println("avg: "+avg);
		
		System.out.println("Got avg_compatibility");
		
		String userPath="data/"+dataset+"/users.txt";
		String skillPath="data/"+dataset+"/skills.txt";
		
		InputManager manager = new InputManager(userPath,skillPath);
		manager.retrieveSkillInfo();
		
		HashMap<Integer,ArrayList<Task>> allTasks = new HashMap<Integer,ArrayList<Task>>();
		
		System.out.println("Producing tasks");
		
		for(int i=0;i<50;i++){
		
			tG = new TaskGenerator();
			ArrayList<String> initialTask = new ArrayList<String>();
			initialTask=tG.produceHardInitialTask(3, manager.getSkillInfo(),avg,skillsCompatibilityMatrix);
			
			//if(i==0){
				ArrayList<Task> tmp = new ArrayList<Task>();
				Task tmpTask = new Task(initialTask);
				tmp.add(tmpTask);
				allTasks.put(3, tmp);
			//}
			/*else{
				Task tmpTask = new Task(initialTask);
				allTasks.get(3).add(tmpTask);
			}*/
			
			//System.out.println("Finished first tasks");
					
			ArrayList<String> secondTask = new ArrayList<String>();
			secondTask=tG.produceHardAdditionalTask(2, manager.getSkillInfo(),initialTask,avg,skillsCompatibilityMatrix);
			
			//if(i==0){
				 tmp = new ArrayList<Task>();
				 tmpTask = new Task(secondTask);
				tmp.add(tmpTask);
				allTasks.put(5, tmp);
			/*}
			else{
				Task tmpTask = new Task(secondTask);
				allTasks.get(5).add(tmpTask);
			}*/
			
			//System.out.println("Finished second tasks");
			
			ArrayList<String> thirdTask = new ArrayList<String>();
			thirdTask=tG.produceHardAdditionalTask(5, manager.getSkillInfo(),secondTask,avg,skillsCompatibilityMatrix);
			
			//if(i==0){
				 tmp = new ArrayList<Task>();
				 tmpTask = new Task(thirdTask);
				tmp.add(tmpTask);
				allTasks.put(10, tmp);
			/*}
			else{
				Task tmpTask = new Task(thirdTask);
				allTasks.get(10).add(tmpTask);
			}*/
			
			//System.out.println("Finished third tasks");
			
			ArrayList<String> fourthTask = new ArrayList<String>();
			fourthTask=tG.produceHardAdditionalTask(10, manager.getSkillInfo(),thirdTask,avg,skillsCompatibilityMatrix);
			
			//if(i==0){
				 tmp = new ArrayList<Task>();
				 tmpTask = new Task(fourthTask);
				tmp.add(tmpTask);
				allTasks.put(20, tmp);
			/*}
			else{
				Task tmpTask = new Task(fourthTask);
				allTasks.get(20).add(tmpTask);
			}*/
			System.out.println("Finished task iteration: "+i);
			
			writeTasks(dataset,allTasks,compatibility_mode);
			allTasks= new HashMap<Integer,ArrayList<Task>>();
		}	
		
		System.out.println("Produced tasks");
		
		
		
	}
	
	public static void writeTasks(String dataset,HashMap<Integer,ArrayList<Task>> allTasks,int compatibility_mode){
		
		for(Integer num : allTasks.keySet()){
			String outputPath="tasks/" + dataset + "/hard/" +dataset + "_" + num + "_"+compatibility_mode+".txt";
			PrintWriter outputWriter;
			File file = new File(outputPath);
			try 
			 { 
				outputWriter = new PrintWriter(new FileOutputStream(outputPath,true)); 
				for(int i=0;i<allTasks.get(num).size();i++){
					outputWriter.println(allTasks.get(num).get(i));
				}
				outputWriter.close();
			 } 
			 catch(FileNotFoundException e) 
			 { 
				 System.out.printf("Error opening the file %s.\n",outputPath);
			 }
		}
		
	}
	
}
