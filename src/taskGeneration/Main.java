package taskGeneration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import inputHandler.InputManager;

public class Main {
	public static void main(String[] args){
		String dataset="zipf";
		String userPath="data/"+dataset+"/users.txt";
		String skillPath="data/"+dataset+"/skills.txt";
		
		InputManager manager = new InputManager(userPath,skillPath);
		manager.retrieveSkillInfo();
		
		HashMap<Integer,ArrayList<Task>> allTasks = new HashMap<Integer,ArrayList<Task>>();
		
		for(int i=0;i<50;i++){
		
			TaskGenerator tG = new TaskGenerator();
			ArrayList<String> initialTask = new ArrayList<String>();
			initialTask=tG.produceInitialTask(3, manager.getSkillInfo());
			
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
			secondTask=tG.produceAdditionalTask(2, manager.getSkillInfo(),initialTask);
			
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
			thirdTask=tG.produceAdditionalTask(5, manager.getSkillInfo(),secondTask);
			
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
			fourthTask=tG.produceAdditionalTask(10, manager.getSkillInfo(),thirdTask);
			
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
