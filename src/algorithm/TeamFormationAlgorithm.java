package algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import inputHandler.DatabaseInput;
import inputHandler.Network;
import inputHandler.SkillInfo;
import mainPackage.Database;

public class TeamFormationAlgorithm {
	//inputFields
	private Network network;
	private SkillInfo skillInfo;

	//algorithmFields
	private ArrayList<String> initialTask = new ArrayList<String>();
	private ArrayList<String> task = new ArrayList<String>();
	private String rarestSkill = new String();
	private HashMap<Integer,StarTeam> teams = new HashMap<Integer,StarTeam>();
	private HashMap<Integer,ArrayList<StarTeam>> exhaustiveTeams = new HashMap<Integer,ArrayList<StarTeam>>();
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
	
	public TeamFormationAlgorithm(boolean most_compatibles_mode,HashMap<Integer,Integer> compatiblesDistribution,String mode,String dataset,int compatible_mode,ArrayList<String> initialTask, SkillInfo skillInfo){
		
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
	
	public TeamFormationAlgorithm(boolean most_compatibles_mode,HashMap<Integer,Integer> compatiblesDistribution,String mode,String dataset, int compatible_mode, ArrayList<String> initialTask,Network network, SkillInfo skillInfo){
		
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
		rankSkills();
		algorithm();
		//exhaustiveAlgorithm();
	}
	
	public void rankSkills(){
		while(initialTask.size()>0){
			String t =findRarestSkill();
			initialTask.remove(t);
			task.add(t);
		}
		rarestSkill=task.get(0);
	}
	
	public String findRarestSkill(){
		String rare = initialTask.get(0);
		int min=skillInfo.getSkillUsers().get(rare).size();
		for(int i=1;i<initialTask.size();i++){
			if(skillInfo.getSkillUsers().get(initialTask.get(i)).size()<min){
				min=skillInfo.getSkillUsers().get(initialTask.get(i)).size();
				rare=initialTask.get(i);
			}
		}
		return rare;
	}
	
	public void algorithm(){
		//System.out.println(skillInfo.getSkillUsers().get(rarestSkill).size());
		for(int i=0;i<skillInfo.getSkillUsers().get(rarestSkill).size();i++){
			int rareUser = skillInfo.getSkillUsers().get(rarestSkill).get(i);
			//System.out.println("Starting for rare user: "+rareUser);
			//create star team
			StarTeam star = new StarTeam();
			star.addMember(rareUser);
			
			//cover rareUser's skill
			ArrayList<String> taskSkills = getUserTaskSkills(rareUser);
			for(int k=0;k<taskSkills.size();k++){
				if(!star.getCoveredSkills().contains(taskSkills.get(k))){
					star.addSkill(taskSkills.get(k));
				}	
			}
			
			for(int j=1;j<task.size();j++){
				if(!star.getCoveredSkills().contains(task.get(j))){
					ArrayList<Integer> users = new ArrayList<Integer>();
					for(int m=0;m<skillInfo.getSkillUsers().get(task.get(j)).size();m++){
						users.add(skillInfo.getSkillUsers().get(task.get(j)).get(m));
					}
					
					//choose best candidate
					int user;
					if(compatibility_mode==true){
						if(most_compatibles_mode==true){
							user=getMostCompatiblesCompatibleCandidate(users,star);
						}
						else{
							user=getBestCompatibleCandidate(users,star);
						}
						//user=getRandomCompatibleCandidate(users,star);
						
					}
					else{
						if(most_compatibles_mode==true){
							user=getMostCompatiblesNoNegativeCandidate(users,star);
						}
						else{
							user=getBestNoNegativeCandidate(users,star);
						}
						//user=getRandomNoNegativeCandidate(users,star);
						
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
						teams.put(rareUser, null);
						break;
					}
				}
			}
			if(!teams.containsKey(rareUser)){
				teams.put(rareUser, star);
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
				
				getBestTeam();
				result+=teams.get(key).getTeam().size()+";"+best_diameter;
			}
		}
		else if(teams.keySet().size()>1){
			
			StarTeam best=getBestTeam();
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
	
	public StarTeam getBestTeam(){
		
		System.out.println("Finding best team.....");
		
		int total_max=-1;
		int node=-1;
		for(Integer starNode : teams.keySet()){
						
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
			//System.out.println("Max distance of "+starNode+"'s team: "+max);
			if(total_max==-1){
				total_max=max;
				node=starNode;
			}
			else if(total_max>max){
				total_max=max;
				node=starNode;
			}
			else if(total_max==max){
				int tmp=handleTies(node,starNode);
				if(tmp==starNode){
					total_max=max;
					node=starNode;
				}
			}
		}
		
		best_diameter=total_max;
		return teams.get(node);
	}
	
	public ArrayList<String> getUserTaskSkills(int user){
		ArrayList<String> taskSkills = new ArrayList<String>();
		for(int i=0;i<skillInfo.getUserSkills().get(user).size();i++){
			if(task.contains(skillInfo.getUserSkills().get(user).get(i))){
				taskSkills.add(skillInfo.getUserSkills().get(user).get(i));
			}
		}
		return taskSkills;
	}
	
	public int getMostCompatiblesNoNegativeCandidate(ArrayList<Integer> candidates, StarTeam team){
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
		
		int max=-1;
		for(int i=0;i<compatibleList.size();i++){
			//int tmp=db.getNumOfCompatibles(compatible_table, compatibleList.get(i));
			int tmp=0;
			if(compatiblesDistribution.containsKey(compatibleList.get(i))){
				tmp=compatiblesDistribution.get(compatibleList.get(i));
			}
			if(max<tmp){
				max=tmp;
				compatible=compatibleList.get(i);
			}
		}
		
		return compatible;
	}
	
	public int getMostCompatiblesCompatibleCandidate(ArrayList<Integer> candidates, StarTeam team){
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
		
		int max=-1;
		for(int i=0;i<compatibleList.size();i++){
			//int tmp=db.getNumOfCompatibles(compatible_table, compatibleList.get(i));
			int tmp=0;
			if(compatiblesDistribution.containsKey(compatibleList.get(i))){
				tmp=compatiblesDistribution.get(compatibleList.get(i));
			}
			if(max<tmp){
				max=tmp;
				compatible=compatibleList.get(i);
			}
		}
		
		return compatible;
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
	
	public int getBestNoNegativeCandidate(ArrayList<Integer> candidates, StarTeam team){
		ArrayList<Integer> notCompatible = new ArrayList<Integer>();
		int compatible=-1;
		int max_c=-1;
				
		for(int i=0;i<team.getTeam().size();i++){
			if(!compatibles.containsKey(team.getTeam().get(i))){
				compatibles.put(team.getTeam().get(i), db.getCompatibles(compatible_table, team.getTeam().get(i)));
			}
		}
		
		for(int i=0;i<candidates.size();i++){
			int max=0;
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
						notCompatible.add(candidates.get(i));
					}
					else{
						if(max<compatibles.get(current_team_member).get(candidates.get(i))){
							max=compatibles.get(current_team_member).get(candidates.get(i));
						}
					}
				}
			}
			if(!notCompatible.contains(candidates.get(i))){
				if(max_c==-1){
					max_c=max;
					compatible= candidates.get(i);
				}
				else if(max_c>max){
					max_c=max;
					compatible= candidates.get(i);
				}
				else if(max==max_c){
					int tmp=handleTies(compatible, candidates.get(i));
					if(tmp==candidates.get(i)){
						max_c=max;
						compatible=candidates.get(i);
					}
				}
			}
		}
		
		return compatible;
	}
	
	
	public int getBestCompatibleCandidate(ArrayList<Integer> candidates, StarTeam team){
		ArrayList<Integer> notCompatible = new ArrayList<Integer>();
		int compatible=-1;
		int max_c=-1;
				
		for(int i=0;i<team.getTeam().size();i++){
			if(!compatibles.containsKey(team.getTeam().get(i))){
				compatibles.put(team.getTeam().get(i), db.getCompatibles(compatible_table, team.getTeam().get(i)));
			}
		}		
		
		for(int i=0;i<candidates.size();i++){
			int max=0;
			for(int j=0;j<team.getTeam().size();j++){
				int current_team_member = team.getTeam().get(j);
				
				if(!compatibles.get(current_team_member).containsKey(candidates.get(i))){
					notCompatible.add(candidates.get(i));
				}
				else{
					if(max<compatibles.get(current_team_member).get(candidates.get(i))){
						max=compatibles.get(current_team_member).get(candidates.get(i));
					}
				}
			}
			if(!notCompatible.contains(candidates.get(i))){
				if(max_c==-1){
					max_c=max;
					compatible= candidates.get(i);
				}
				else if(max_c>max){
					max_c=max;
					compatible= candidates.get(i);
				}
				else if(max==max_c){
					int tmp=handleTies(compatible, candidates.get(i));
					if(tmp==candidates.get(i)){
						max_c=max;
						compatible=candidates.get(i);
					}
				}
			}
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
	
	//EXHAUSTIVE
	
	/*public StarTeam getBestExhaustiveTeam(){
		
		System.out.println("Finding exhaustive best team.....");
		
		int total_max=-1;
		StarTeam bestTeam = new StarTeam();
		for(Integer starNode : exhaustiveTeams.keySet()){
			for(int k=0;k<exhaustiveTeams.get(starNode).size();k++){	
				StarTeam currentTeam = exhaustiveTeams.get(starNode).get(k);
				for(int i=0;i<currentTeam.getTeam().size();i++){
					if(!compatibles.containsKey(currentTeam.getTeam().get(i))){
						compatibles.put(currentTeam.getTeam().get(i), db.getCompatibles(compatible_table, currentTeam.getTeam().get(i)));
					}
				}	
				
				int max=0;
				for(int i=0;i<currentTeam.getTeam().size();i++){
					for(int j=0;j<currentTeam.getTeam().size();j++){
						int node1= currentTeam.getTeam().get(i);
						int node2= currentTeam.getTeam().get(j);
						
						if(compatibles.get(node1).containsKey(node2)){
							if(max<compatibles.get(node1).get(node2)){
								max=compatibles.get(node1).get(node2);
							}
						}
					}
				}
				//System.out.println("Max distance of "+starNode+"'s team: "+max);
				if(total_max==-1){
					total_max=max;
					bestTeam=currentTeam;
				}
				else if(total_max>max){
					total_max=max;
					bestTeam=currentTeam;
				}
				else if(total_max==max){
					boolean change = handleExhaustiveTeamTies(bestTeam,currentTeam);
					if(change==true){
						total_max=max;
						bestTeam=currentTeam;
					}
				}
			}
		}
		
		best_diameter=total_max;
		return bestTeam;
	}*/
	
	//returns true if bestTeam must change, or false otherwise
	/*public boolean handleExhaustiveTeamTies(StarTeam bestTeam,StarTeam currentTeam){
		int sum1=0;
		for(int i=0;i<bestTeam.getTeam().size();i++){
			sum1+=bestTeam.getTeam().get(i);
		}
		int sum2=0;
		for(int i=0;i<currentTeam.getTeam().size();i++){
			sum2+=currentTeam.getTeam().get(i);
		}
		if(sum2<sum1){
			return true;
		}
		else{
			return false;
		}
		
	}*/
	
	/*
	 public ArrayList<Integer> getNoNegativeCompatibleCandidates(ArrayList<Integer> candidates, StarTeam team){
		ArrayList<Integer> notCompatible = new ArrayList<Integer>();
		ArrayList<Integer> compatibleList = new ArrayList<Integer>();
				
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
						notCompatible.add(candidates.get(i));
					}
					else{
						compatibleList.add(candidates.get(i));
					}
				}
			}
		}
		
		return compatibleList;
	}
	
	public ArrayList<Integer> getCompatibleCandidates(ArrayList<Integer> candidates, StarTeam team){
		ArrayList<Integer> notCompatible = new ArrayList<Integer>();
		ArrayList<Integer> compatibleList= new ArrayList<Integer>();
				
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
		
		return compatibleList;
	}
	 
	 */
	
}
