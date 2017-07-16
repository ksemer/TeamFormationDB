package inputHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class FileReader {
	private String inputPath;
	private Scanner inputReader;
	private ArrayList<String> data = new ArrayList<String>();
	
	public FileReader(String inputPath){
		this.inputPath=inputPath;
	}
	
	public void initReader(){
		File file = new File(inputPath);
		try 
		 { 
			 inputReader = new Scanner(new FileInputStream(file)); 
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.printf("File %s was not found or could not be opened.\n",inputPath); 
		 }
	}
	
	public void retrieveData(){
		while(inputReader.hasNextLine()){
			String line=inputReader.nextLine();
			data.add(line);
		}
		inputReader.close();
	}
	
	public ArrayList<String> getData(){
		return data;
	}
	
	
	public HashMap<Integer,ArrayList<Integer>> getCompatibility(){
		HashMap<Integer,ArrayList<Integer>> compatibility = new HashMap<Integer,ArrayList<Integer>>();
		while(inputReader.hasNextLine()){
			String line=inputReader.nextLine();
			String fields[] =line.split("\t");
			if(!compatibility.containsKey(Integer.parseInt(fields[0]))){
				ArrayList<Integer> tmp = new ArrayList<Integer>();
				tmp.add(Integer.parseInt(fields[1]));
				compatibility.put(Integer.parseInt(fields[0]),tmp);
			}
			else{
				compatibility.get(Integer.parseInt(fields[0])).add(Integer.parseInt(fields[1]));
			}
		}
		inputReader.close();
		return compatibility;
	}

}
