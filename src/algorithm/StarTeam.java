package algorithm;

import java.util.ArrayList;

public class StarTeam {
	private ArrayList<String> coveredSkills = new ArrayList<String>();
	private ArrayList<Integer> team = new ArrayList<Integer>();
	
	public void addSkill(String skill){
		coveredSkills.add(skill);
	}
	
	public void addMember(int member){
		team.add(member);
	}
	
	public ArrayList<String> getCoveredSkills(){
		return coveredSkills;
	}
	
	public ArrayList<Integer> getTeam(){
		return team;
	}
	
	public void addSkills(ArrayList<String> skills){
		for(int i=0;i<skills.size();i++){
			coveredSkills.add(skills.get(i));
		}
	}
	
	public void addTeam(ArrayList<Integer> members){
		for(int i=0;i<members.size();i++){
			team.add(members.get(i));
		}
	}
}
