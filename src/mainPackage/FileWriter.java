package mainPackage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileWriter {
	private String outputPath;
	private PrintWriter outputWriter;
	private File file;
	
	public FileWriter(String outputPath){
		this.outputPath=outputPath;
	}

	public void initWriter(){
		file = new File(outputPath);
		try 
		 { 
			outputWriter = new PrintWriter(new FileOutputStream(outputPath)); 
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.printf("Error opening the file %s.\n",outputPath);
		 }
	}
	
	public void initAppendWriter(){
		file = new File(outputPath);
		try 
		 { 
			outputWriter = new PrintWriter(new FileOutputStream(outputPath,true)); 
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.printf("Error opening the file %s.\n",outputPath);
		 }
	}
	
	public void appendLine(String line){
		outputWriter.println(line);
		outputWriter.flush();
		outputWriter.close();
	}
	
	public void writeData(ArrayList<String> data){
		for(int i=0;i<data.size();i++){
			outputWriter.println(data.get(i));
		}
		outputWriter.close();
	}
	
}
