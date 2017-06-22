package compatibilityLists;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class FileEditor {
	private String inputPath;
	private Scanner inputReader;
	
	private String outputPath;
	private PrintWriter outputWriter = null;
	private File outputFile;
	
	private String distancesOutputPath;
	private PrintWriter outputWriter1 = null;
	private File outputFile1;
	
	public FileEditor(String inputPath, String outputPath){
		this.inputPath=inputPath;
		this.outputPath=outputPath;
	}
	
	public FileEditor(String inputPath, String outputPath, String distancesOutputPath){
		this.inputPath=inputPath;
		this.outputPath=outputPath;
		this.distancesOutputPath=distancesOutputPath;
	}
	
	
	public void processMorePositive(){
		initReader();
		initWriter();
		retrieveMorePositiveData();
	
	}
	
	public void processOnePositive(){
		initReader();
		initWriter();
		retrieveOnePositiveData();
	
	}
	
	public void processNoNegative(){
		initReader();
		initWriter();
		initWriter1();
		retrieveNoNegativeData();
	
	}
	
	public void initWriter(){
		outputFile = new File(outputPath);
		try 
		 { 
			outputWriter = new PrintWriter(new FileOutputStream(outputFile)); 
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.printf("Error opening the file %s.\n",outputPath);
		 }
	}
	
	public void initWriter1(){
		outputFile1 = new File(distancesOutputPath);
		try 
		 { 
			outputWriter1 = new PrintWriter(new FileOutputStream(outputFile1)); 
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.printf("Error opening the file %s.\n",distancesOutputPath);
		 }
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
	
	
	public void retrieveNoNegativeData(){
		while(inputReader.hasNextLine()){
			String strLine=inputReader.nextLine();
			String tokens[] = strLine.split("\t");
			if(Integer.parseInt(tokens[3])==0){
				outputWriter.println(tokens[0]+"\t"+tokens[1]+"\t"+tokens[4]);
			}
			outputWriter1.println(tokens[0]+"\t"+tokens[1]+"\t"+tokens[4]);
		}
		inputReader.close();
		outputWriter.close();
		outputWriter1.close();
	}
	
	public void retrieveMorePositiveData(){
		while(inputReader.hasNextLine()){
			String strLine=inputReader.nextLine();
			String tokens[] = strLine.split("\t");
			if(Integer.parseInt(tokens[2])>Integer.parseInt(tokens[3])){
				outputWriter.println(tokens[0]+"\t"+tokens[1]+"\t"+tokens[4]);
			}
		}
		inputReader.close();
		outputWriter.close();
	}
	
	public void retrieveOnePositiveData(){
		while(inputReader.hasNextLine()){
			String strLine=inputReader.nextLine();
			String tokens[] = strLine.split("\t");
			if(Integer.parseInt(tokens[2])>0){
				outputWriter.println(tokens[0]+"\t"+tokens[1]+"\t"+tokens[4]);
			}
		}
		inputReader.close();
		outputWriter.close();
	}

}
