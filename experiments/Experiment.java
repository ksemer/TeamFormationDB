import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class Experiment{
	public static void main(String[] args)throws IOException{
		
		if (args.length <= 0) {
			System.err.println("Need command to run");
			System.exit(-1);
		}

		System.out.print("Started at: ");
		System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
		
		int compatibility_mode=Integer.parseInt(args[4]);	
		String dataset = args[3];
		int numOfSkills= Integer.parseInt(args[2]);
		String resultPath;	

		if(compatibility_mode==1){
			resultPath="/home/formation/Desktop/results/"+dataset+"/"+dataset+"_"+numOfSkills+"_no_negative_paths.txt";

		}
		else if(compatibility_mode==2){
			resultPath="/home/formation/Desktop/results/"+dataset+"/"+dataset+"_"+numOfSkills+"_more_positive_paths.txt";
		}
		else if(compatibility_mode==3){
			resultPath="/home/formation/Desktop/results/"+dataset+"/"+dataset+"_"+numOfSkills+"_one_positive_path.txt";
		}
		else{
			resultPath="/home/formation/Desktop/results/"+dataset+"/"+dataset+"_"+numOfSkills+"_no_negative_edge.txt";
		}		

		String inputPath = "tasks/"+dataset+"/"+dataset+"_"+numOfSkills+".txt";
		Scanner inputReader;
		ArrayList<String> tasks = new ArrayList<String>();
		File file = new File(inputPath);
		try 
		 { 
			 inputReader = new Scanner(new FileInputStream(file)); 
			 while(inputReader.hasNextLine()){
				tasks.add(inputReader.nextLine());
			 }
			inputReader.close();
		 } 
		 catch(FileNotFoundException e) 
		 { 
			 System.out.printf("File %s was not found or could not be opened.\n",inputPath); 
		 }	 

		String[] newArgs = new String[6];
		for(int j=0;j<args.length;j++){
			newArgs[j]=args[j];
		}

		for(int i=0;i<tasks.size();i++){
			  Runtime runtime = Runtime.getRuntime();
			  newArgs[5]=tasks.get(i);
			  Process process = runtime.exec(newArgs);
			  InputStream is = process.getInputStream();
			  InputStreamReader isr = new InputStreamReader(is);
			  BufferedReader br = new BufferedReader(isr);
			  String line;
		 
			  //System.out.printf("Output of running %s is:", 
			 //	  Arrays.toString(args));
		 
			  while ((line = br.readLine()) != null) {
				FileWriter writer = new FileWriter(resultPath);
				writer.initAppendWriter();
				writer.appendLine(line);
			  }			
		}

		System.out.print("Finished at: ");
		System.out.println( new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) );
		
	}
}
