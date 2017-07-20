package mainPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;

import algorithm.CompatibleSkillsAlgorithm;
import inputHandler.InputManager;

public class CompatibleSkillsMain {
	
	public static void main(String[] args){
		
		//********** PARAMETERS **********//*
		
		String dataset="got";
		
		boolean most_compatibles_mode=true;
		
		String resultPath="";
		
		if(most_compatibles_mode==true){
			resultPath="/home/formation/Desktop/results/"+dataset+"/skills/most_compatibles";
		}
		else{
			resultPath="/home/formation/Desktop/results/"+dataset+"/skills/min_diameter";
		}
		
		//hard:1, easy:2, random:3
		int task_mode=3;
		
		int nums_of_skills[]={3,5,10,20};
		
		//compatibility_modes:
		// 0 : sbp, 1 : no_negative_paths, 2 : more_positive_paths, 3 : one_positive_path, 4 : no_negative_edge, -1 : only for slashdot sbp not heuristic
		
		if(dataset.equals("got") || dataset.equals("slashdot")){
			int[] compatibility_modes ={-1,0,1,2,3,4};
			execute(resultPath,dataset,compatibility_modes,nums_of_skills,most_compatibles_mode,task_mode);
		}
		else{
			int[] compatibility_modes ={0,1,2,3,4};
			execute(resultPath,dataset,compatibility_modes,nums_of_skills,most_compatibles_mode,task_mode);
		}
		
		//********** ********** **********//*
		
	}
	
	public static void execute(String path,String dataset,int[] compatibility_modes, int[] nums_of_skills,boolean most_compatibles_mode,int task_mode){
		String mode;
		ArrayList<String> time = new ArrayList<String>();
		for(int mp=0;mp<compatibility_modes.length;mp++){
			int compatibility_mode=compatibility_modes[mp];
			for(int kp=0;kp<nums_of_skills.length;kp++){
				int numOfSkills=nums_of_skills[kp];
				
				time.add("Starting : mode: "+compatibility_mode+", numOfSkills: "+numOfSkills+" at:"+ new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
		
				if(compatibility_mode>0){
					mode="spc";
				}
				else{
					mode="sbp";
				}
				
				String resultPath=path;
				String distributionPath;
				String compatibleSkillPath;
				
				String spl="\t";		
				
				System.out.print("Started at: ");
				System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
				
				System.out.println("Num of skills: "+numOfSkills+", Compatibility mode: "+compatibility_mode);
				
				//for(int j=1;j<6;j++){
					//System.out.println("Started random iteration : "+j);
				
			
					if(compatibility_mode==1){
						resultPath+="/"+dataset+"_"+numOfSkills+"_no_negative_paths.txt";
						//resultPath="/home/formation/Desktop/results/"+dataset+"/"+dataset+"_"+numOfSkills+"_no_negative_paths.txt";
						//resultPath="/home/formation/Desktop/results/"+dataset+"/random/"+j+"/"+dataset+"_"+numOfSkills+"_no_negative_paths.txt";
						distributionPath="compatibles_distribution/"+dataset+"/"+"no_negative_paths.txt";
						compatibleSkillPath="skillsCompatibilityMatrix/"+dataset+"/"+dataset+"_no_negative_paths.txt";
					}
					else if(compatibility_mode==2){
						resultPath+="/"+dataset+"_"+numOfSkills+"_more_positive_paths.txt";
						//resultPath="/home/formation/Desktop/results/"+dataset+"/"+dataset+"_"+numOfSkills+"_more_positive_paths.txt";
						//resultPath="/home/formation/Desktop/results/"+dataset+"/random/"+j+"/"+dataset+"_"+numOfSkills+"_more_positive_paths.txt";
						distributionPath="compatibles_distribution/"+dataset+"/"+"more_positive_paths.txt";
						compatibleSkillPath="skillsCompatibilityMatrix/"+dataset+"/"+dataset+"_more_positive_paths.txt";
					}
					else if(compatibility_mode==3){
						resultPath+="/"+dataset+"_"+numOfSkills+"_one_positive_path.txt";
						//resultPath="/home/formation/Desktop/results/"+dataset+"/"+dataset+"_"+numOfSkills+"_one_positive_path.txt";
						//resultPath="/home/formation/Desktop/results/"+dataset+"/random/"+j+"/"+dataset+"_"+numOfSkills+"_one_positive_path.txt";
						distributionPath="compatibles_distribution/"+dataset+"/"+"one_positive_path.txt";
						compatibleSkillPath="skillsCompatibilityMatrix/"+dataset+"/"+dataset+"_one_positive_path.txt";
					}
					else if(compatibility_mode==4){
						resultPath+="/"+dataset+"_"+numOfSkills+"_no_negative_edge.txt";
						//resultPath="/home/formation/Desktop/results/"+dataset+"/"+dataset+"_"+numOfSkills+"_no_negative_edge.txt";
						//resultPath="/home/formation/Desktop/results/"+dataset+"/random/"+j+"/"+dataset+"_"+numOfSkills+"_no_negative_edge.txt";
						distributionPath="compatibles_distribution/"+dataset+"/"+"no_negative_edge.txt";
						compatibleSkillPath="skillsCompatibilityMatrix/"+dataset+"/"+dataset+"_no_negative_edge.txt";
					}	
					else if(compatibility_mode==0){
						resultPath+="/"+dataset+"_"+numOfSkills+"_sbp.txt";
						//resultPath="/home/formation/Desktop/results/"+dataset+"/"+dataset+"_"+numOfSkills+"_sbp.txt";
						//resultPath="/home/formation/Desktop/results/"+dataset+"/random/"+j+"/"+dataset+"_"+numOfSkills+"_sbp.txt";
						distributionPath="compatibles_distribution/"+dataset+"/"+"sbp.txt";
						compatibleSkillPath="skillsCompatibilityMatrix/"+dataset+"/"+dataset+"_sbp.txt";
					}
					else{
						resultPath+="/"+dataset+"_"+numOfSkills+"_sbp_not_heuristic.txt";
						//resultPath="/home/formation/Desktop/results/"+dataset+"/"+dataset+"_"+numOfSkills+"_sbp_not_heuristic.txt";
						//resultPath="/home/formation/Desktop/results/"+dataset+"/random/"+j+"/"+dataset+"_"+numOfSkills+"_sbp_not_heuristic.txt";
						distributionPath="compatibles_distribution/"+dataset+"/"+"sbp_not_heuristic.txt";
						compatibleSkillPath="skillsCompatibilityMatrix/"+dataset+"/"+dataset+"_sbp_not_heuristic.txt";
					}
			
					String inputPath = "tasks/"+dataset+"/"+dataset+"_"+numOfSkills+".txt";
					//String inputPath = "tasks/"+dataset+"/rare>1_"+dataset+"_"+numOfSkills+".txt";
					
					Scanner inputReader;
					ArrayList<String> tasks = new ArrayList<String>();
					File file = new File(inputPath);
					try 
					 { 
						 inputReader = new Scanner(new FileInputStream(file)); 
						 while(inputReader.hasNextLine()){
							 String line=inputReader.nextLine();
							 //System.out.println(line);
							tasks.add(line);
						 }
						inputReader.close();
					 } 
					 catch(IOException e) 
					 { 
						 System.out.printf("File %s was not found or could not be opened.\n",inputPath);
					 }
					
					HashMap<String,HashMap<String,Integer>> skillsCompatibilityMatrix = new HashMap<String,HashMap<String,Integer>>();
					
					Scanner inputReader0;
					File file0 = new File(compatibleSkillPath);
					try 
					 { 
						inputReader0 = new Scanner(new FileInputStream(file0)); 
						 while(inputReader0.hasNextLine()){
							String tokens[]=inputReader0.nextLine().split("\t");
							if(!skillsCompatibilityMatrix.containsKey(tokens[0])){
								HashMap<String,Integer> tmp = new HashMap<String,Integer>();
								tmp.put(tokens[1], Integer.parseInt(tokens[2]));
								skillsCompatibilityMatrix.put(tokens[0], tmp);
							}
							else{
								skillsCompatibilityMatrix.get(tokens[0]).put(tokens[1],Integer.parseInt(tokens[2]));
							}
						 }
						 inputReader0.close();
					 } 
					 catch(FileNotFoundException e) 
					 { 
						 System.out.printf("File %s was not found or could not be opened.\n",compatibleSkillPath); 
					 }
					
					HashMap<Integer,Integer> compatiblesDistribution = new HashMap<Integer,Integer>();
					
					if(most_compatibles_mode==true){
						Scanner inputReader1;
						File file1 = new File(distributionPath);
						try 
						 { 
							 inputReader1 = new Scanner(new FileInputStream(file1)); 
							 while(inputReader1.hasNextLine()){
								String tokens[]=inputReader1.nextLine().split(spl);
								compatiblesDistribution.put(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
							 }
							inputReader1.close();
						 } 
						 catch(FileNotFoundException e) 
						 { 
							 System.out.printf("File %s was not found or could not be opened.\n",distributionPath); 
						 }
					}
					
					
					for(int i=0;i<tasks.size();i++){
					
						System.out.print("Starting iteration "+i+" at :");
						System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
							
						String networkPath="data/"+dataset+"/network.txt"; 
						
						String userPath="data/"+dataset+"/users.txt";
						String skillPath="data/"+dataset+"/skills.txt";
						
						
						/*String userPath="data/"+dataset+"/users_rare>1.txt";
						String skillPath="data/"+dataset+"/skills_rare>1.txt";*/
						
						
						String result;
						
						ArrayList<String> initialTask = new ArrayList<String>();
						String skills[] = tasks.get(i).split(",");
						for(int k=0;k<skills.length;k++){
							initialTask.add(skills[k]);
						}
							
						if(compatibility_mode<4){
							result=runCompatibilityAlgorithm(most_compatibles_mode,skillsCompatibilityMatrix,compatiblesDistribution,mode,initialTask,dataset,compatibility_mode,numOfSkills, networkPath, userPath, skillPath);
							
						}
						else{
							result=runNoNegativeAlgorithm(most_compatibles_mode,skillsCompatibilityMatrix,compatiblesDistribution,mode,initialTask,dataset,compatibility_mode,numOfSkills, networkPath, userPath, skillPath);
						}
						
						FileWriter writer = new FileWriter(resultPath);
						writer.initAppendWriter();
						writer.appendLine(result);
						
						//System.out.println(result);
						
						System.out.print("Finished iteration "+i+" at :");
						System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
					}	
				//}
				System.out.print("Finished at: ");
				System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
				
				time.add("Finished : mode: "+compatibility_mode+", numOfSkills: "+numOfSkills+" at:"+ new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
			}
		}
		String timePath=path+"/"+"time.txt";
		FileWriter timeWriter = new FileWriter(timePath);
		timeWriter.initWriter();
		timeWriter.writeData(time);
	}
	
	
	public static String runCompatibilityAlgorithm(boolean most_compatibles_mode,HashMap<String,HashMap<String,Integer>> skillsCompatibilityMatrix,HashMap<Integer,Integer> compatiblesDistribution,String mode,ArrayList<String> initialTask,String dataset,int compatibility_mode,int numOfSkills,String networkPath, String userPath, String skillPath){
		InputManager manager = new InputManager(networkPath,userPath,skillPath);
		manager.retrieveSkillInfo();
		
		//ArrayList<String> initialTask = produceTask(numOfSkills,manager.getSkillInfo());
				
		CompatibleSkillsAlgorithm algorithm = new CompatibleSkillsAlgorithm(most_compatibles_mode,skillsCompatibilityMatrix,compatiblesDistribution,mode,dataset,compatibility_mode,initialTask,manager.getSkillInfo());
		algorithm.start();
		
		return algorithm.getResult();
	}
	
	public static String runNoNegativeAlgorithm(boolean most_compatibles_mode,HashMap<String,HashMap<String,Integer>> skillsCompatibilityMatrix,HashMap<Integer,Integer> compatiblesDistribution,String mode,ArrayList<String> initialTask,String dataset,int compatibility_mode,int numOfSkills,String networkPath, String userPath, String skillPath){
		InputManager manager = new InputManager(networkPath,userPath,skillPath);
		manager.retrieveNetwork();
		manager.retrieveSkillInfo();
		
		//ArrayList<String> initialTask = produceTask(numOfSkills,manager.getSkillInfo());
				
		CompatibleSkillsAlgorithm algorithm = new CompatibleSkillsAlgorithm(most_compatibles_mode,skillsCompatibilityMatrix,compatiblesDistribution,mode,dataset,compatibility_mode,initialTask,manager.getNetwork(), manager.getSkillInfo());
		algorithm.start();
		
		return algorithm.getResult();
		
	}
	
	/*public static ArrayList<String> produceTask(int numOfTasks, SkillInfo skillInfo){
		ArrayList<String> initialTask = new ArrayList<String>();
		Random rndGen = new Random();
		for(int i=0;i<numOfTasks;i++){
			String temp = skillInfo.getSkills().get(rndGen.nextInt(skillInfo.getSkills().size()));
			initialTask.add(temp);
		}
		return initialTask;
	}*/
}
