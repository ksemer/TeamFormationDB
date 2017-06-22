package inputHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class InputManager {
	private String networkPath = new String();
	private String userPath = new String();
	private String skillPath = new String();
	private ArrayList<String> skillsList = new ArrayList<String>();
	private HashMap<Integer,ArrayList<String>> userInfo = new HashMap<Integer,ArrayList<String>>();
	private HashMap<String,ArrayList<Integer>> skillInfo = new HashMap<String,ArrayList<Integer>>();
	private Network network;
	private SkillInfo skills;
	
	public InputManager(String networkPath, String userPath, String skillPath){
		this.networkPath=networkPath;
		this.userPath=userPath;
		this.skillPath=skillPath;
	}
	
	public InputManager(String userPath, String skillPath){
		this.userPath=userPath;
		this.skillPath=skillPath;
	}
	
	public Network getNetwork(){
		return network;
	}
	
	public void retrieveNetwork(){
		FileReader reader = new FileReader(networkPath);
		reader.initReader();
		reader.retrieveData();
		ArrayList<String> lines = reader.getData();
		HashMap<String,Integer> edges = new HashMap<String,Integer>();
		ArrayList<Integer> nodes= new ArrayList<Integer>();
		for(int i=0;i<lines.size();i++){
			String tokens[] = lines.get(i).split("\t");
			if(!nodes.contains(Integer.parseInt(tokens[0]))){
				nodes.add(Integer.parseInt(tokens[0]));
			}
			if(!nodes.contains(Integer.parseInt(tokens[1]))){
				nodes.add(Integer.parseInt(tokens[1]));
			}
			String edge = tokens[0]+","+tokens[1];
			edges.put(edge, Integer.parseInt(tokens[2]));
		}
		network = new Network(edges,nodes);
	}
	
	public SkillInfo getSkillInfo(){
		return skills;
	}
	
	public void retrieveSkillInfo(){
		getUserInfo();
		getSkillUsers();
		skills = new SkillInfo(userInfo, skillInfo, skillsList);		
	}
	
	public void getUserInfo(){	
		FileReader reader = new FileReader(userPath);
		reader.initReader();
		reader.retrieveData();
		ArrayList<String> lines = reader.getData();
		for(int i=0;i<lines.size();i++){
			String fields[]= lines.get(i).split("\t");
			String skills[]=fields[1].split(",");
			ArrayList<String> skillList = new ArrayList<String>();
			for(int j=0;j<skills.length;j++){
				skillList.add(skills[j]);
			}
			userInfo.put(Integer.parseInt(fields[0]), skillList);
		}	
	}
	
	public void getSkillUsers(){		
		FileReader reader = new FileReader(skillPath);
		reader.initReader();
		reader.retrieveData();
		ArrayList<String> lines = reader.getData();
		for(int i=0;i<lines.size();i++){
			String fields[] = lines.get(i).split("\t");
			String tokens[]=fields[1].split(",");
			ArrayList<Integer> userList = new ArrayList<Integer>();
			for(int j=0;j<tokens.length;j++){
				userList.add(Integer.parseInt(tokens[j]));
			}
			skillInfo.put(fields[0], userList);
			skillsList.add(fields[0]);
		}
	}
}
