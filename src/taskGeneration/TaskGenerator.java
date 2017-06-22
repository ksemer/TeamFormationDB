package taskGeneration;

import java.util.ArrayList;
import java.util.Random;

import inputHandler.SkillInfo;

public class TaskGenerator {
	
	public TaskGenerator(){
		
	}
	
	public static ArrayList<String> produceInitialTask(int numOfTasks, SkillInfo skillInfo){
		ArrayList<String> initialTask = new ArrayList<String>();
		Random rndGen = new Random();
		for(int i=0;i<numOfTasks;i++){
			String temp;
			temp = skillInfo.getSkills().get(rndGen.nextInt(skillInfo.getSkills().size()));
			while(initialTask.contains(temp)){
				temp = skillInfo.getSkills().get(rndGen.nextInt(skillInfo.getSkills().size()));
			}
			initialTask.add(temp);
		}
		return initialTask;
	}
	
	public static ArrayList<String> produceAdditionalTask(int numOfTasks, SkillInfo skillInfo, ArrayList<String> initial){
		ArrayList<String> additionalTask = new ArrayList<String>();
		Random rndGen = new Random();
		for(int i=0;i<numOfTasks;i++){
			String temp;
			temp = skillInfo.getSkills().get(rndGen.nextInt(skillInfo.getSkills().size()));
			while(initial.contains(temp)){
				temp = skillInfo.getSkills().get(rndGen.nextInt(skillInfo.getSkills().size()));

			}
			additionalTask.add(temp);
		}
		for(int j=0;j<initial.size();j++){
			additionalTask.add(initial.get(j));
		}
		return additionalTask;
	}
}
