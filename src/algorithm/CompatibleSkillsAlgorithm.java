package algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import inputHandler.DatabaseInput;
import inputHandler.Network;
import inputHandler.SkillInfo;

public class CompatibleSkillsAlgorithm {

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
	private HashMap<String,HashMap<String,Integer>> skillsCompatibilityMatrix = new HashMap<String,HashMap<String,Integer>>();
	
	private HashMap<String,Integer> skillScores;
	private String lessSkill1="";
	private String lessSkill2="";
	
	public CompatibleSkillsAlgorithm(boolean most_compatibles_mode,HashMap<String,HashMap<String,Integer>> skillsCompatibilityMatrix,HashMap<Integer,Integer> compatiblesDistribution,String mode,String dataset,int compatible_mode,ArrayList<String> initialTask, SkillInfo skillInfo){
		
		this.most_compatibles_mode=most_compatibles_mode;
		
		this.skillsCompatibilityMatrix=skillsCompatibilityMatrix;
		
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
	
	public CompatibleSkillsAlgorithm(boolean most_compatibles_mode,HashMap<String,HashMap<String,Integer>> skillsCompatibilityMatrix,HashMap<Integer,Integer> compatiblesDistribution,String mode,String dataset, int compatible_mode, ArrayList<String> initialTask,Network network, SkillInfo skillInfo){
		
		
		this.most_compatibles_mode=most_compatibles_mode;
		
		this.skillsCompatibilityMatrix=skillsCompatibilityMatrix;
		
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
		getLessCompatiblePair();
		rankSkills();
		algorithm();
		//exhaustiveAlgorithm();
	}
	
	public void getLessCompatiblePair(){
		//HashMap<String,Integer> skillScores = new HashMap<String,Integer>();
		int min=-1;
		for(int i=0;i<initialTask.size()-1;i++){
			String skill1=initialTask.get(i);
			int score=0;
			for(int j=i+1;j<initialTask.size();j++){
				String skill2=initialTask.get(j);
				/*System.out.println(skill1);
				System.out.println(skill2);*/
				if(i==0 && j==1){
					lessSkill1=skill1;
					lessSkill2=skill2;
					
					/*if(skillsCompatibilityMatrix.containsKey(skill1)){
						if(skillsCompatibilityMatrix.get(skill1).containsKey(skill2)){
							min=skillsCompatibilityMatrix.get(skill1).get(skill2);
						}
						else{
							min=0;
						}
					}
					if(skillsCompatibilityMatrix.containsKey(skill2)){
						if(skillsCompatibilityMatrix.get(skill2).containsKey(skill1)){
							min=skillsCompatibilityMatrix.get(skill2).get(skill1);
						}
						else{
							min=0;
						}
					}*/
					
					try{
						min=skillsCompatibilityMatrix.get(skill1).get(skill2);
					}
					catch(NullPointerException e){
						try{
							min=skillsCompatibilityMatrix.get(skill2).get(skill1);
						}
						catch(NullPointerException e1){
							min=0;
						}
					}
					
					
					/*Integer tmp1=skillsCompatibilityMatrix.get(skill1).get(skill2);
					Integer tmp2=skillsCompatibilityMatrix.get(skill2).get(skill1);
					if(tmp1!=null){
						min=tmp1;
					}
					else if(tmp2!=null){
						min=tmp2;
					}
					else{
						min=0;
					}*/
				}
				else{
					/*if(skillsCompatibilityMatrix.containsKey(skill1)){
						if(skillsCompatibilityMatrix.get(skill1).containsKey(skill2)){
							if(skillsCompatibilityMatrix.get(skill1).get(skill2)<min){
								lessSkill1=skill1;
								lessSkill2=skill2;
								min=skillsCompatibilityMatrix.get(skill1).get(skill2);
							}
						}
					}
					if(skillsCompatibilityMatrix.containsKey(skill2)){
						if(skillsCompatibilityMatrix.get(skill2).containsKey(skill1)){	
							if(skillsCompatibilityMatrix.get(skill2).get(skill1)<min){
								lessSkill1=skill1;
								lessSkill2=skill2;
								min=skillsCompatibilityMatrix.get(skill2).get(skill1);
							}
						}
					}*/
					
					
					/*Integer tmp1=skillsCompatibilityMatrix.get(skill1).get(skill2);
					Integer tmp2=skillsCompatibilityMatrix.get(skill2).get(skill1);
					if(tmp1!=null){
						if(tmp1<min){
							lessSkill1=skill1;
							lessSkill2=skill2;
							min=tmp1;
						}
					}
					else if(tmp2!=null){
						if(tmp2<min){
							lessSkill1=skill1;
							lessSkill2=skill2;
							min=tmp2;
						}
					}*/
					
					try{
						if(skillsCompatibilityMatrix.get(skill1).get(skill2)<min){
							lessSkill1=skill1;
							lessSkill2=skill2;
							min=skillsCompatibilityMatrix.get(skill1).get(skill2);
						}
					}
					catch(NullPointerException e){
						try{
							if(skillsCompatibilityMatrix.get(skill2).get(skill1)<min){
								lessSkill1=skill1;
								lessSkill2=skill2;
								min=skillsCompatibilityMatrix.get(skill2).get(skill1);
							}
						}
						catch(NullPointerException e1){}
					}
				}
			}
		}
		
	}
	
	public void rankSkills(){
		skillScores = new HashMap<String,Integer>();
		for(int i=0;i<initialTask.size();i++){
			String skill1=initialTask.get(i);
			int score=0;
			for(int j=0;j<initialTask.size();j++){
				String skill2=initialTask.get(j);
				if(i!=j){
					/*if(skillsCompatibilityMatrix.containsKey(skill1)){
						if(skillsCompatibilityMatrix.get(skill1).containsKey(skill2)){
							score+=skillsCompatibilityMatrix.get(skill1).get(skill2);
						}
					}
					if(skillsCompatibilityMatrix.containsKey(skill2)){
						if(skillsCompatibilityMatrix.get(skill2).containsKey(skill1)){
							score+=skillsCompatibilityMatrix.get(skill2).get(skill1);
						}
					}*/
					/*Integer tmp1=skillsCompatibilityMatrix.get(skill1).get(skill2);
					Integer tmp2=skillsCompatibilityMatrix.get(skill2).get(skill1);
					if(tmp1!=null){
						score+=tmp1;
					}
					else if(tmp2!=null){
						score+=tmp2;
					}*/

				    try{
						score+=skillsCompatibilityMatrix.get(skill1).get(skill2);
					}
					catch(NullPointerException e){
						try{
							score+=skillsCompatibilityMatrix.get(skill2).get(skill1);
						}
						catch(NullPointerException e1){}
					}
					
				}
			}
			skillScores.put(skill1,score);
		}
	}
	
	public String getLessCompatibleSkill(ArrayList<String> candidateSkills){
		int min=-1;
		String lessComp="";
		for(int i=0;i<candidateSkills.size();i++){
			if(i==0){
				lessComp=candidateSkills.get(i);
				min=skillScores.get(candidateSkills.get(i));
			}
			else if(skillScores.get(candidateSkills.get(i))<min){
				lessComp=candidateSkills.get(i);
				min=skillScores.get(candidateSkills.get(i));
			}
		}
		return lessComp;
	}
	
	public void algorithm(){
		//System.out.println(skillInfo.getSkillUsers().get(rarestSkill).size());
		
		ArrayList<String> skillsToCompare = new ArrayList<String>();
		skillsToCompare.add(lessSkill1);
		skillsToCompare.add(lessSkill2);
		//System.out.println(lessSkill1);
		//System.out.println(lessSkill2);
		String firstSkill=getLessCompatibleSkill(skillsToCompare);
		for(int i=0;i<skillInfo.getSkillUsers().get(firstSkill).size();i++){
			ArrayList<String> skillsLeft = new ArrayList<String>();
			for(int h=0;h<initialTask.size();h++){
				skillsLeft.add(initialTask.get(h));
			}
			int rareUser = skillInfo.getSkillUsers().get(firstSkill).get(i);
			skillsLeft.remove(firstSkill);
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
			
			for(int j=1;j<initialTask.size();j++){
				String currentSkill="";
				if(j==1){
					currentSkill=lessSkill2;
					skillsLeft.remove(lessSkill2);
				}
				else{
					currentSkill=getLessCompatibleSkill(skillsLeft);
					skillsLeft.remove(currentSkill);
				}
				if(!star.getCoveredSkills().contains(currentSkill)){
					ArrayList<Integer> users = new ArrayList<Integer>();
					//System.out.println(j+": "+currentSkill);
					for(int m=0;m<skillInfo.getSkillUsers().get(currentSkill).size();m++){
						users.add(skillInfo.getSkillUsers().get(currentSkill).get(m));
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
						//
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
			if(initialTask.contains(skillInfo.getUserSkills().get(user).get(i))){
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
						notCompatible.add(candidates.get(i));
					}
					else{
						compatibleList.add(candidates.get(i));
					}
				}
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
						notCompatible.add(candidates.get(i));
					}
					else{
						compatibleList.add(candidates.get(i));
					}
				}
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

}
