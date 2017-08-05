package taskGeneration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import inputHandler.FileReader;
import inputHandler.InputManager;

public class FeasibleHardMain {
	public static void main(String[] args){
		String dataset="wikipedia";
		String userPath="data/"+dataset+"/users.txt";
		String skillPath="data/"+dataset+"/skills.txt";
		
		String distributionPath="tasks/" + dataset + "/" +dataset + "_skill_distribution.txt";
		
		FileReader dReader = new FileReader(distributionPath);
		dReader.initReader();
		dReader.retrieveData();
		ArrayList<String> distData=dReader.getData();
		HashMap<String,Integer> skillDistribution = new HashMap<String,Integer>();
		
		int sum=0;
		
		for(int i=0;i<distData.size();i++){
			String fields[] = distData.get(i).split("\t");
			skillDistribution.put(fields[0],Integer.parseInt(fields[1]));
			sum+=Integer.parseInt(fields[1]);
		}
		
		double avg=(double)sum/(double)distData.size();
		
		String compatibleSkillPath="skillsCompatibilityMatrix/"+dataset+"/"+dataset+"_no_negative_paths.txt";
		
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
		
		
		InputManager manager = new InputManager(userPath,skillPath);
		manager.retrieveSkillInfo();
		
		HashMap<Integer,ArrayList<Task>> allTasks = new HashMap<Integer,ArrayList<Task>>();
		
		for(int i=0;i<50;i++){
		
			TaskGenerator tG = new TaskGenerator();
			ArrayList<String> initialTask = new ArrayList<String>();
			
			initialTask=tG.produceHardFeasibleInitialTask(3,manager.getSkillInfo(),avg,skillDistribution,skillsCompatibilityMatrix);
			
			//initialTask=tG.produceRandomInitialTask(3, manager.getSkillInfo());
			
			if(i==0){
				ArrayList<Task> tmp = new ArrayList<Task>();
				Task tmpTask = new Task(initialTask);
				tmp.add(tmpTask);
				allTasks.put(3, tmp);
			}
			else{
				Task tmpTask = new Task(initialTask);
				allTasks.get(3).add(tmpTask);
			}
					
			ArrayList<String> secondTask = new ArrayList<String>();
			
			secondTask=tG.produceHardFeasibleAdditionalTask(initialTask,2,manager.getSkillInfo(),avg,skillDistribution,skillsCompatibilityMatrix);
			
			//secondTask=tG.produceRandomAdditionalTask(2, manager.getSkillInfo(),initialTask);
			
			if(i==0){
				ArrayList<Task> tmp = new ArrayList<Task>();
				Task tmpTask = new Task(secondTask);
				tmp.add(tmpTask);
				allTasks.put(5, tmp);
			}
			else{
				Task tmpTask = new Task(secondTask);
				allTasks.get(5).add(tmpTask);
			}
			
			ArrayList<String> thirdTask = new ArrayList<String>();
			
			thirdTask=tG.produceHardFeasibleAdditionalTask(secondTask,5,manager.getSkillInfo(),avg,skillDistribution,skillsCompatibilityMatrix);
			
			//thirdTask=tG.produceRandomAdditionalTask(5, manager.getSkillInfo(),secondTask);
			
			if(i==0){
				ArrayList<Task> tmp = new ArrayList<Task>();
				Task tmpTask = new Task(thirdTask);
				tmp.add(tmpTask);
				allTasks.put(10, tmp);
			}
			else{
				Task tmpTask = new Task(thirdTask);
				allTasks.get(10).add(tmpTask);
			}
			
			ArrayList<String> fourthTask = new ArrayList<String>();
			
			fourthTask=tG.produceHardFeasibleAdditionalTask(thirdTask,10,manager.getSkillInfo(),avg,skillDistribution,skillsCompatibilityMatrix);
			
			//fourthTask=tG.produceRandomAdditionalTask(10, manager.getSkillInfo(),thirdTask);
			
			if(i==0){
				ArrayList<Task> tmp = new ArrayList<Task>();
				Task tmpTask = new Task(fourthTask);
				tmp.add(tmpTask);
				allTasks.put(20, tmp);
			}
			else{
				Task tmpTask = new Task(fourthTask);
				allTasks.get(20).add(tmpTask);
			}
		}	
		
		writeTasks(dataset,allTasks);
	}
	
	public static void writeTasks(String dataset,HashMap<Integer,ArrayList<Task>> allTasks){
		
		for(Integer num : allTasks.keySet()){
			String outputPath="tasks/" + dataset + "/" +dataset + "_" + num + ".txt";
			PrintWriter outputWriter;
			File file = new File(outputPath);
			try 
			 { 
				outputWriter = new PrintWriter(new FileOutputStream(outputPath)); 
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
