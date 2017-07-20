import java.util.ArrayList;

public class Main {
	private static ArrayList<String> results = new ArrayList<String>();
	private static ArrayList<String> latexResults = new ArrayList<String>();
	public static void main(String[] args){
		String dataset="wikipedia";
		int[] numOfSkills={3,5,10,20};
		
		if(dataset.equals("got") || dataset.equals("slashdot")){
			int[] compatibility_modes={1,2,3,0,-1,4};
			execute(dataset,compatibility_modes,numOfSkills);
		}
		else{
			int[] compatibility_modes={1,2,3,0,4};
			execute(dataset,compatibility_modes,numOfSkills);
		}
	}
	
	public static void execute(String dataset, int[] compatibility_modes,int[] numOfSkills){
		
		String inputPath="";
		
		String outputPath="resultsToEdit\\"+dataset+"_results.txt";
		
		FileWriter writer = new FileWriter(outputPath);
		writer.initWriter();
		
		String outputPath1="resultsToEdit\\"+dataset+"_latex_results.txt";
		
		FileWriter writer1 = new FileWriter(outputPath1);
		writer1.initWriter();
		
		
		for(int i=0;i<compatibility_modes.length;i++){
			int compatibility_mode = compatibility_modes[i];
			if(compatibility_mode==1){
				results.add("NO NEGATIVE PATHS:");	
				latexResults.add("NO NEGATIVE PATHS:");
				for(int j=0;j<numOfSkills.length;j++){
					inputPath="resultsToEdit\\"+dataset+"_"+numOfSkills[j]+"_no_negative_paths.txt";
					results.add("");
					results.add("Task size "+ numOfSkills[j]+" : ");
					getResults(dataset,numOfSkills[j],inputPath);
				}
				results.add("");
			}
			else if(compatibility_mode==2){
				results.add("MORE POSITIVE PATHS:");
				latexResults.add("MORE POSITIVE PATHS:");
				for(int j=0;j<numOfSkills.length;j++){
					inputPath="resultsToEdit\\"+dataset+"_"+numOfSkills[j]+"_more_positive_paths.txt";
					results.add("");
					results.add("Task size "+ numOfSkills[j]+" : ");
					getResults(dataset,numOfSkills[j],inputPath);
				}
				results.add("");
			}
			else if(compatibility_mode==3){
				results.add("ONE POSITIVE PATH:");
				latexResults.add("ONE POSITIVE PATH:");
				for(int j=0;j<numOfSkills.length;j++){
					inputPath="resultsToEdit\\"+dataset+"_"+numOfSkills[j]+"_one_positive_path.txt";
					results.add("");
					results.add("Task size "+ numOfSkills[j]+" : ");
					getResults(dataset,numOfSkills[j],inputPath);
				}
				results.add("");
			}
			else if(compatibility_mode==4){
				results.add("NO NEGATIVE EDGE:");
				latexResults.add("NO NEGATIVE EDGE:");
				for(int j=0;j<numOfSkills.length;j++){
					inputPath="resultsToEdit\\"+dataset+"_"+numOfSkills[j]+"_no_negative_edge.txt";
					results.add("");
					results.add("Task size "+ numOfSkills[j]+" : ");
					getResults(dataset,numOfSkills[j],inputPath);
				}	
				results.add("");
			}
			else if(compatibility_mode==0){
				results.add("SBP HEURISTIC:");
				latexResults.add("SBP HEURISTIC:");
				for(int j=0;j<numOfSkills.length;j++){
					inputPath="resultsToEdit\\"+dataset+"_"+numOfSkills[j]+"_sbp.txt";
					results.add("");
					results.add("Task size "+ numOfSkills[j]+" : ");
					getResults(dataset,numOfSkills[j],inputPath);
				}	
				results.add("");
			}
			else{
				results.add("SBP:");
				latexResults.add("SBP:");
				for(int j=0;j<numOfSkills.length;j++){
					inputPath="resultsToEdit\\"+dataset+"_"+numOfSkills[j]+"_sbp_not_heuristic.txt";
					results.add("");
					results.add("Task size "+ numOfSkills[j]+" : ");
					getResults(dataset,numOfSkills[j],inputPath);
				}	
				results.add("");
			}
				
		}
		
		writer1.writeData(latexResults);
		writer.writeData(results);
			
	}
	public static void getResults(String dataset, int numOfSkills, String inputPath){
		FileReader reader = new FileReader(inputPath);
		reader.initReader();
		reader.retrieveData();
		
		FileEditor editor = new FileEditor(reader.getData());
		editor.edit();
		
		latexResults.add(editor.getTaskSize()+" & "+editor.getNumOfLines()+" & "+(editor.getNumOfLines()-editor.getNoTeams())+" & "+editor.getAverageTeamsFound()+" & "+editor.getAverageTeamSize()+" & "+editor.getAverageTeamDistance()+"\\\\");
		
		results.add("Results of experiment with number of tasks: "+editor.getTaskSize()+" and iterations: "+editor.getNumOfLines());
		results.add("The algorithm found a team: " +(editor.getNumOfLines()-editor.getNoTeams()) + " out of "+editor.getNumOfLines()+" times");
		results.add("The average number of candidate teams was: " +editor.getAverageTeamsFound());
		results.add("The average team size was: "+ editor.getAverageTeamSize());
		results.add("The average team diameter was: "+editor.getAverageTeamDistance());
	}
}
