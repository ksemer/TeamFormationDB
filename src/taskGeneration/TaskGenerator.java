package taskGeneration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import inputHandler.SkillInfo;

public class TaskGenerator {
	
	public TaskGenerator(){
		
	}
	
	public double getAvgVompatibility(HashMap<String,HashMap<String,Integer>> matrix){
		double avg=0.0;
		int sum=0;
		int count=0;
		
		for(String skill1: matrix.keySet()){
			for(String skill2: matrix.get(skill1).keySet()){
				sum+=matrix.get(skill1).get(skill2);
				count++;
			}
		}
		
		avg=(double)(sum/count);
		
		return avg;
	}
	
	public static ArrayList<String> produceHardInitialTask(int numOfTasks, SkillInfo skillInfo,double avg,HashMap<String,HashMap<String,Integer>> matrix){
		ArrayList<String> initialTask = new ArrayList<String>();
		
		Random rndGen = new Random();
		for(int i=0;i<numOfTasks;i++){
			String temp;
			boolean good=true;
			do{
				good=true;
				temp = skillInfo.getSkills().get(rndGen.nextInt(skillInfo.getSkills().size()));
				while(initialTask.contains(temp)){
					temp = skillInfo.getSkills().get(rndGen.nextInt(skillInfo.getSkills().size()));
				}
				for(int j=0;j<initialTask.size();j++){
					if(matrix.containsKey(initialTask.get(j))){
						if(matrix.get(initialTask.get(j)).containsKey(temp)){
							if(matrix.get(initialTask.get(j)).get(temp)>=avg){
								good=false;
							}
						}
					}
					if(matrix.containsKey(temp)){
						if(matrix.get(temp).containsKey(initialTask.get(j))){
							if(matrix.get(temp).get(initialTask.get(j))>=avg){
								good=false;
							}
						}
					}
				}
			}while(good==false);
			initialTask.add(temp);
		}
		
		return initialTask;
	}
	
	public static ArrayList<String> produceHardAdditionalTask(int numOfTasks, SkillInfo skillInfo, ArrayList<String> initial,double avg,HashMap<String,HashMap<String,Integer>> matrix){
		ArrayList<String> additionalTask = new ArrayList<String>();
		
		Random rndGen = new Random();
		
		for(int j=0;j<initial.size();j++){
			additionalTask.add(initial.get(j));
		}
		
		for(int i=0;i<numOfTasks;i++){
			String temp;
			boolean good=true;
			do{
				good=true;
				temp = skillInfo.getSkills().get(rndGen.nextInt(skillInfo.getSkills().size()));
				while(additionalTask.contains(temp)){
					temp = skillInfo.getSkills().get(rndGen.nextInt(skillInfo.getSkills().size()));
				}
				for(int j=0;j<additionalTask.size();j++){
					if(matrix.containsKey(additionalTask.get(j))){
						if(matrix.get(additionalTask.get(j)).containsKey(temp)){
							if(matrix.get(additionalTask.get(j)).get(temp)>=avg){
								good=false;
							}
						}
					}
					if(matrix.containsKey(temp)){
						if(matrix.get(temp).containsKey(additionalTask.get(j))){
							if(matrix.get(temp).get(additionalTask.get(j))>=avg){
								good=false;
							}
						}
					}
				}
			}while(good==false);
			additionalTask.add(temp);
		}
		
		return additionalTask;
	}
	
	public static ArrayList<String> produceEasyInitialTask(int numOfTasks, SkillInfo skillInfo,double avg,HashMap<String,HashMap<String,Integer>> matrix){
		ArrayList<String> initialTask = new ArrayList<String>();
		
		Random rndGen = new Random();
		for(int i=0;i<numOfTasks;i++){
			String temp;
			boolean good=true;
			do{
				good=true;
				temp = skillInfo.getSkills().get(rndGen.nextInt(skillInfo.getSkills().size()));
				while(initialTask.contains(temp)){
					temp = skillInfo.getSkills().get(rndGen.nextInt(skillInfo.getSkills().size()));
				}
				for(int j=0;j<initialTask.size();j++){
					if(matrix.containsKey(initialTask.get(j))){
						if(matrix.get(initialTask.get(j)).containsKey(temp)){
							if(matrix.get(initialTask.get(j)).get(temp)<=avg){
								good=false;
							}
						}
					}
					if(matrix.containsKey(temp)){
						if(matrix.get(temp).containsKey(initialTask.get(j))){
							if(matrix.get(temp).get(initialTask.get(j))<=avg){
								good=false;
							}
						}
					}
				}
			}while(good==false);
			initialTask.add(temp);
		}
		
		return initialTask;
	}
	
	public static ArrayList<String> produceEasyAdditionalTask(int numOfTasks, SkillInfo skillInfo, ArrayList<String> initial,double avg,HashMap<String,HashMap<String,Integer>> matrix){
		ArrayList<String> additionalTask = new ArrayList<String>();
		
		Random rndGen = new Random();
		
		for(int j=0;j<initial.size();j++){
			additionalTask.add(initial.get(j));
		}
		
		for(int i=0;i<numOfTasks;i++){
			String temp;
			boolean good=true;
			do{
				good=true;
				temp = skillInfo.getSkills().get(rndGen.nextInt(skillInfo.getSkills().size()));
				while(additionalTask.contains(temp)){
					temp = skillInfo.getSkills().get(rndGen.nextInt(skillInfo.getSkills().size()));
				}
				for(int j=0;j<additionalTask.size();j++){
					if(matrix.containsKey(additionalTask.get(j))){
						if(matrix.get(additionalTask.get(j)).containsKey(temp)){
							if(matrix.get(additionalTask.get(j)).get(temp)<=avg){
								good=false;
							}
						}
					}
					if(matrix.containsKey(temp)){
						if(matrix.get(temp).containsKey(additionalTask.get(j))){
							if(matrix.get(temp).get(additionalTask.get(j))<=avg){
								good=false;
							}
						}
					}
				}
			}while(good==false);
			additionalTask.add(temp);
		}
		
		return additionalTask;
	}
	
	public static ArrayList<String> produceRandomInitialTask(int numOfTasks, SkillInfo skillInfo){
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
	
	public static ArrayList<String> produceRandomAdditionalTask(int numOfTasks, SkillInfo skillInfo, ArrayList<String> initial){
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
