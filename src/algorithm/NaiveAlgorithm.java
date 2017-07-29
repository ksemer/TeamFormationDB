package algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import inputHandler.DatabaseInput;
import inputHandler.Network;
import inputHandler.SkillInfo;
import mainPackage.Database;

public class NaiveAlgorithm {
	//inputFields
	private Network network;
	private SkillInfo skillInfo;

	//algorithmFields
	private ArrayList<String> initialTask = new ArrayList<String>();
	private HashMap<Integer,StarTeam> teams = new HashMap<Integer,StarTeam>();
	private String result = new String();
	
	private int compatible_mode=0;
	private String mode= new String();
	
	private boolean most_compatibles_mode;
	private boolean compatibility_mode;
	private int best_diameter=0;
	private String compatible_table;
	private DatabaseInput db = new DatabaseInput();
	
	private HashMap<Integer,Integer> compatiblesDistribution = new HashMap<Integer,Integer>();
	
	private HashMap<Integer,HashMap<Integer,Integer>> compatibles = new HashMap<Integer,HashMap<Integer,Integer>>();
	
	public NaiveAlgorithm(boolean most_compatibles_mode,HashMap<Integer,Integer> compatiblesDistribution,String mode,String dataset,int compatible_mode,ArrayList<String> initialTask, SkillInfo skillInfo){
		
		this.most_compatibles_mode=most_compatibles_mode;
		
		this.compatiblesDistribution=compatiblesDistribution;
		
		db.init(mode);
		
		this.mode=mode;
		
		this.compatible_mode=compatible_mode;
		
		if(compatible_mode==1){
			compatible_table=mode+"_"+dataset+"_no_negative_paths";
		}
		else if(compatible_mode==2){
			compatible_table=mode+"_"+dataset+"_more_positive_paths";
		}
		else if(compatible_mode==3){
			compatible_table=mode+"_"+dataset+"_one_positive_path";
		}
		else if(compatible_mode==0){
			compatible_table=mode+"_"+dataset+"_compatibles";
		}
		else{
			compatible_table=mode+"_"+dataset;
		}
		
		this.initialTask=initialTask;
		this.skillInfo=skillInfo;
		compatibility_mode=true;
		for(int i=0;i<initialTask.size()-1;i++){
			result+=initialTask.get(i)+",";
			//System.out.print(initialTask.get(i)+" , ");
		}
		result+=initialTask.get(initialTask.size()-1)+";";
		//System.out.println(initialTask.get(initialTask.size()-1));
	}
	
	public NaiveAlgorithm(boolean most_compatibles_mode,HashMap<Integer,Integer> compatiblesDistribution,String mode,String dataset, int compatible_mode, ArrayList<String> initialTask,Network network, SkillInfo skillInfo){
		
		this.most_compatibles_mode=most_compatibles_mode;
		
		this.compatiblesDistribution=compatiblesDistribution;
		
		db.init(mode);
		
		this.mode=mode;
				
		this.compatible_mode=compatible_mode;
		
		compatible_table=mode+"_"+dataset+"_distances";		
		
		this.initialTask=initialTask;
		this.network=network;
		this.skillInfo=skillInfo;
		compatibility_mode=false;
		for(int i=0;i<initialTask.size()-1;i++){
			result+=initialTask.get(i)+",";
			//System.out.print(initialTask.get(i)+" , ");
		}
		result+=initialTask.get(initialTask.size()-1)+";";
		//System.out.println(initialTask.get(initialTask.size()-1));
	}

	public void start(){
		algorithm();
	}
	
	public void algorithm(){
		//System.out.println(skillInfo.getSkillUsers().get(rarestSkill).size());
		Random rndGen = new Random();
		String firstSkill= initialTask.get(rndGen.nextInt(initialTask.size()));
		for(int i=0;i<skillInfo.getSkillUsers().get(firstSkill).size();i++){
			int firstUser = skillInfo.getSkillUsers().get(firstSkill).get(i);
			//System.out.println("Starting for rare user: "+firstUser);
			//create star team
			StarTeam star = new StarTeam();
			star.addMember(firstUser);
			
			//cover firstUser's skill
			ArrayList<String> taskSkills = getUserTaskSkills(firstUser);
			for(int k=0;k<taskSkills.size();k++){
				if(!star.getCoveredSkills().contains(taskSkills.get(k))){
					star.addSkill(taskSkills.get(k));
				}	
			}
			
			for(int j=0;j<initialTask.size();j++){
				
				String current_skill=initialTask.get(j);
				
				if(!star.getCoveredSkills().contains(current_skill)){
					ArrayList<Integer> users = new ArrayList<Integer>();
					for(int m=0;m<skillInfo.getSkillUsers().get(current_skill).size();m++){
						users.add(skillInfo.getSkillUsers().get(current_skill).get(m));
					}
					
					//choose best candidate
					int user;
					if(compatibility_mode==true){
						user=getRandomCompatibleCandidate(users,star);
						
					}
					else{
						user=getRandomNoNegativeCandidate(users,star);	
					}
					
					if(user!=-1){
						//add to team
						star.addMember(user);
						
						//add his skills in team
						ArrayList<String> skillsCovered = getUserTaskSkills(user);
						for(int k=0;k<skillsCovered.size();k++){
							if(!star.getCoveredSkills().contains(skillsCovered.get(k))){
								star.addSkill(skillsCovered.get(k));
							}	
						}
					}
					else{
						teams.put(firstUser, null);
						break;
					}
				}
			}
			if(!teams.containsKey(firstUser)){
				teams.put(firstUser, star);
			}
		}
		ArrayList<Integer> toRemove = new ArrayList<Integer>();
		for(Integer rareNode : teams.keySet()){
			if(teams.get(rareNode)==null){
				toRemove.add(rareNode);
			}
		}
		for(int i=0;i<toRemove.size();i++){
			teams.remove(toRemove.get(i));
		}
		
		result+=teams.keySet().size()+";";
		
		if(teams.keySet().size()==1){
			//System.out.println("Found 1 team:");
			for(Integer key : teams.keySet()){
				//System.out.println("Starnode's "+key+" team: ");
				for(int i=0;i<teams.get(key).getTeam().size()-1;i++){
					result+=teams.get(key).getTeam().get(i)+",";
					//System.out.print(teams.get(key).getTeam().get(i)+" , ");
				}
				result+=teams.get(key).getTeam().get(teams.get(key).getTeam().size()-1)+";";
				//System.out.println(teams.get(key).getTeam().get(teams.get(key).getTeam().size()-1));
				
				getRandomTeam();
				result+=teams.get(key).getTeam().size()+";"+best_diameter;
			}
		}
		else if(teams.keySet().size()>1){
			
			StarTeam best=getRandomTeam();
			//System.out.println("Best team:");
			for(int i=0;i<best.getTeam().size()-1;i++){
				result+=best.getTeam().get(i)+",";
				//System.out.print(best.getTeam().get(i)+"   ,   ");
			}
			result+=best.getTeam().get(best.getTeam().size()-1)+";";
			//System.out.println(best.getTeam().get(best.getTeam().size()-1));
			result+=best.getTeam().size()+";"+best_diameter;
		}
		else{
			result+="0;-;-;-";
			//System.out.println("No team found!");
		}
	}
	
	public StarTeam getRandomTeam(){
		
		System.out.println("Finding random team.....");
		
		Random rndGen = new Random();
		int star=rndGen.nextInt(teams.keySet().size());
		int starNode=0;
		//System.out.println(starNode);
		int count=0;
		for(Integer key: teams.keySet()){
			if(count==star){
				starNode=key;
				break;
			}
			count++;
		}
						
		for(int i=0;i<teams.get(starNode).getTeam().size();i++){
			if(!compatibles.containsKey(teams.get(starNode).getTeam().get(i))){
				compatibles.put(teams.get(starNode).getTeam().get(i), db.getCompatibles(compatible_table, teams.get(starNode).getTeam().get(i)));
			}
		}	
		
		int max=0;
		for(int i=0;i<teams.get(starNode).getTeam().size();i++){
			for(int j=0;j<teams.get(starNode).getTeam().size();j++){
				int node1= teams.get(starNode).getTeam().get(i);
				int node2= teams.get(starNode).getTeam().get(j);
				
				if(compatibles.get(node1).containsKey(node2)){
					if(max<compatibles.get(node1).get(node2)){
						max=compatibles.get(node1).get(node2);
					}
				}
			}
		}
		
		best_diameter=max;
		return teams.get(starNode);
	}
	
	public ArrayList<String> getUserTaskSkills(int user){
		ArrayList<String> taskSkills = new ArrayList<String>();
		for(int i=0;i<skillInfo.getUserSkills().get(user).size();i++){
			if(initialTask.contains(skillInfo.getUserSkills().get(user).get(i))){
				taskSkills.add(skillInfo.getUserSkills().get(user).get(i));
			}
		}
		return taskSkills;
	}
	
	
	public int getRandomNoNegativeCandidate(ArrayList<Integer> candidates, StarTeam team){
		ArrayList<Integer> notCompatible = new ArrayList<Integer>();
		ArrayList<Integer> compatibleList = new ArrayList<Integer>();
		int compatible=-1;
				
		for(int i=0;i<team.getTeam().size();i++){
			if(!compatibles.containsKey(team.getTeam().get(i))){
				compatibles.put(team.getTeam().get(i), db.getCompatibles(compatible_table, team.getTeam().get(i)));
			}
		}
		
		for(int i=0;i<candidates.size();i++){
			boolean not_compatible=false;
			for(int j=0;j<team.getTeam().size();j++){
				int current_team_member = team.getTeam().get(j);
				if(network.getEdges().containsKey(candidates.get(i)+","+team.getTeam().get(j))){
					if(network.getEdges().get(candidates.get(i)+","+team.getTeam().get(j))<0){
						not_compatible=true;
						notCompatible.add(candidates.get(i));
					}
				}
				if(network.getEdges().containsKey(team.getTeam().get(j)+","+candidates.get(i))){
					if(network.getEdges().get(team.getTeam().get(j)+","+candidates.get(i))<0){
						not_compatible=true;
						notCompatible.add(candidates.get(i));
					}
				}
				if(not_compatible==false){
					if(!compatibles.get(current_team_member).containsKey(candidates.get(i))){
						not_compatible=true;
						notCompatible.add(candidates.get(i));
					}
				}
			}
			if(not_compatible==false){
				compatibleList.add(candidates.get(i));
			}
		}
		
		if(compatibleList.size()>0){
			Random rndGen = new Random();
			compatible=compatibleList.get(rndGen.nextInt(compatibleList.size()));
		}
		
		return compatible;
	}
	
	public int getRandomCompatibleCandidate(ArrayList<Integer> candidates, StarTeam team){
		ArrayList<Integer> notCompatible = new ArrayList<Integer>();
		ArrayList<Integer> compatibleList= new ArrayList<Integer>();
		int compatible=-1;
		
				
		for(int i=0;i<team.getTeam().size();i++){
			if(!compatibles.containsKey(team.getTeam().get(i))){
				compatibles.put(team.getTeam().get(i), db.getCompatibles(compatible_table, team.getTeam().get(i)));
			}
		}		
		
		for(int i=0;i<candidates.size();i++){
			for(int j=0;j<team.getTeam().size();j++){
				int current_team_member = team.getTeam().get(j);
				
				if(!compatibles.get(current_team_member).containsKey(candidates.get(i))){
					notCompatible.add(candidates.get(i));
				}
			}
			if(!notCompatible.contains(candidates.get(i))){
				compatibleList.add(candidates.get(i));
			}
		}
		
		if(compatibleList.size()>0){
			Random rndGen = new Random();
			compatible=compatibleList.get(rndGen.nextInt(compatibleList.size()));
		}
		
		return compatible;
	}
	
	public int handleTies(int compatible1, int compatible2){
		if(compatible1<compatible2){
			return compatible1;
		}
		else{
			return compatible2;
		}
	}
	
	public String getResult(){
		return result;
	}
}
