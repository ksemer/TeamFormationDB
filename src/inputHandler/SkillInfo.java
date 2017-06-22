package inputHandler;
import java.util.ArrayList;
import java.util.HashMap;

public class SkillInfo {
	private HashMap<Integer,ArrayList<String>> userSkills = new HashMap<Integer,ArrayList<String>>();
	private HashMap<String,ArrayList<Integer>> skillUsers = new HashMap<String,ArrayList<Integer>>();
	private ArrayList<String> skills = new ArrayList<String>();
	
	public SkillInfo(HashMap<Integer,ArrayList<String>> userSkills, HashMap<String,ArrayList<Integer>> skillDistribution, ArrayList<String> skills){
		this.userSkills=userSkills;
		this.skillUsers=skillDistribution;
		this.skills=skills;
	}
	
	public HashMap<Integer,ArrayList<String>> getUserSkills(){
		return userSkills;
	}
	
	public HashMap<String,ArrayList<Integer>> getSkillUsers(){
		return skillUsers;
	}
	
	public ArrayList<String> getSkills(){
		return skills;
	}

}
