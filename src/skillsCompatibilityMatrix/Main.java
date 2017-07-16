package skillsCompatibilityMatrix;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import inputHandler.FileReader;
import mainPackage.Database;
import mainPackage.FileWriter;

public class Main {
	public static void main(String[] args) throws IOException{
		//for(int i=1;i<5;i++){
			execute("epinions",4);
			//System.gc();
		//}
	}
	public static void execute(String dataset,int compatibility_mode){
		System.out.print("Started at: ");
		System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
		
		//String dataset="wikipedia";
		//int compatibility_mode=0;
		
		String skillPath="data/"+dataset+"/skills.txt";
		
		FileReader reader = new FileReader(skillPath);
		reader.initReader();
		reader.retrieveData();
		ArrayList<String> data = reader.getData();
		HashMap<String,ArrayList<Integer>> skills = new HashMap<String,ArrayList<Integer>>();
		for(int i=0;i<data.size();i++){
			String tokens[] =data.get(i).split("\t");
			String users[]=tokens[1].split(",");
			ArrayList<Integer> userList = new ArrayList<Integer>();
			for(int j=0;j<users.length;j++){
				userList.add(Integer.parseInt(users[j]));
			}
			skills.put(tokens[0], userList);
		}
		
		/*Database db = new Database();
		db.init();*/
		
		//String table="";
		String comp_file="/home/formation/Desktop/compatibilityLists/"+dataset+"/";
		String outputPath="/home/formation/Desktop/skillsCompatibilityMatrix/"+dataset+"/"+dataset+"_";
		
		if(compatibility_mode==-1){
			//table="sbp_"+dataset;
			comp_file+="sbp.txt";
			outputPath+="sbp_not_heuristic.txt";
			
		}
		else if(compatibility_mode==0){
			//table="sbp_"+dataset+"_compatibles";
			comp_file+=dataset+"_sbp.txt";
			outputPath+="sbp.txt";
			
		}
		else if(compatibility_mode==1){
			comp_file+="no_negative_paths.txt";
			//table="spc_"+dataset+"_no_negative_paths";
			outputPath+="no_negative_paths.txt";
			
		}
		else if(compatibility_mode==2){
			//table="spc_"+dataset+"_more_positive_paths";
			comp_file+="more_positive_paths.txt";
			outputPath+="more_positive_paths.txt";
		}
		else if(compatibility_mode==3){
			//table="spc_"+dataset+"_one_positive_path";
			comp_file+="one_positive_path.txt";
			outputPath+="one_positive_path.txt";
		}
		else{
			//table="spc_"+dataset+"_no_negative_edge";
			comp_file+="distances.txt";
			outputPath+="no_negative_edge.txt";
		}
		
		//HashMap<String,HashMap<String,Integer>> compatibility = new HashMap<String,HashMap<String,Integer>>();
		
		System.out.print("Started reading from file at: ");
		System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
		
		FileReader reader1 = new FileReader(comp_file);
		reader1.initReader();
		
		HashMap<Integer,ArrayList<Integer>> compatibility = reader1.getCompatibility();
		
		System.out.print("Finished reading from file at: ");
		System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
		
		FileWriter writer = new FileWriter(outputPath);
		//writer.initAppendWriter();
		writer.initWriter();
		
		ArrayList<String> results = new ArrayList<String>();
		
		int counter=0;
		int user1=0;
		int user2=0;
		
		//System.out.println(compatibility.size());
		
		for(String skill1 : skills.keySet()){
			for(String skill2 : skills.keySet()){
				counter=0;
				if(!skill1.equals(skill2)){
					
					for(int i=0;i<skills.get(skill1).size();i++){
						user1=skills.get(skill1).get(i);
						for(int j=0;j<skills.get(skill2).size();j++){
							user2=skills.get(skill2).get(j);
							/*if(db.isCompatible(table, user1, user2)){
								counter++;
							}*/
							if(compatibility.containsKey(user1)){
								if(compatibility.get(user1).contains(user2)){
									counter++;
								}
							}
							if(compatibility.containsKey(user2)){
								if(compatibility.get(user2).contains(user1)){
									counter++;
								}
							}
						}
					}
				}
				
				//writer.appendLine(skill1+"\t"+skill2+"\t"+counter);
				results.add(skill1+"\t"+skill2+"\t"+counter);
				/*if(!compatibility.containsKey(skill1)){
					HashMap<String,Integer> tmp = new HashMap<String,Integer>();
					tmp.put(skill2, counter);
					compatibility.put(skill1, tmp);
				}
				else{
					compatibility.get(skill1).put(skill2,counter);
				}*/
			}
		}
		
		writer.writeData(results);
		
		/*FileWriter writer = new FileWriter(outputPath);
		writer.initWriter();
		writer.writeData(data);*/
		
		System.out.print("Finished at: ");
		System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
	}
}
