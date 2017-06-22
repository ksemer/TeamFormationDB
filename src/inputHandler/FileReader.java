package inputHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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

}
