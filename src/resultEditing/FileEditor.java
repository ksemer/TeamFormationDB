import java.util.ArrayList;

public class FileEditor {
	private ArrayList<String> data = new ArrayList<String>();
	private ArrayList<String> newData = new ArrayList<String>();
	private int taskSize=0;
	private int numOfLines=0;
	private int sumOfTeams=0;
	private int noTeams=0;
	private int sumOfSize=0;
	private int sumOfDistance=0;
	
	public FileEditor(){}
	
	public FileEditor(ArrayList<String> data){
		this.data=data;
	}
	
	public void edit(){
		numOfLines=data.size();
		for(int i=0;i<data.size();i++){
			String tokens[]=data.get(i).split(";");
			taskSize=tokens[0].split(",").length;
			if(Integer.parseInt(tokens[1])==0){
				noTeams++;
				
			}
			else{
				sumOfTeams+=Integer.parseInt(tokens[1]);
				sumOfSize+=Integer.parseInt(tokens[3]);
				sumOfDistance+=Integer.parseInt(tokens[4]);
			}
		}
		
	}
	
	public int getNumOfLines(){
		return numOfLines;
	}
	
	public int getTaskSize(){
		return taskSize;
	}
	
	public int getNoTeams(){
		return noTeams;
	}
	
	public double getAverageTeamSize(){
		return (double)(sumOfSize)/(numOfLines-noTeams);
	}
	
	public double getAverageTeamsFound(){
		return (double)(sumOfTeams)/(numOfLines-noTeams);
	}
	
	public double getAverageTeamDistance(){
		return (double)(sumOfDistance)/(numOfLines-noTeams);
	}
	
	public ArrayList<String> getData(){
		return newData;
	}

}
