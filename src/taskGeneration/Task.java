package taskGeneration;

import java.util.ArrayList;

public class Task {
	private ArrayList<String> task = new ArrayList<String>();
	
	public Task(ArrayList<String> task){
		this.task=task;
	}

	public String toString(){
		String taskString = "";
		for(int i=0;i<task.size()-1;i++){
			taskString+=task.get(i)+",";
		}
		taskString+=task.get(task.size()-1);
		return taskString;
	}
	
}
