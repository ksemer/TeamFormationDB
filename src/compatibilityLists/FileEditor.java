package compatibilityLists;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
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

	public FileEditor(String inputPath, String outputPath) {
		this.inputPath = inputPath;
		this.outputPath = outputPath;
	}

	public FileEditor(String inputPath, String outputPath, String distancesOutputPath) {
		this.inputPath = inputPath;
		this.outputPath = outputPath;
		this.distancesOutputPath = distancesOutputPath;
	}

	public void processMorePositive() {
		initReader();
		initWriter();
		retrieveMorePositiveData();
	}

	public void processOnePositive() {
		initReader();
		initWriter();
		retrieveOnePositiveData();
	}

	public void processSbp() {
		initReader();
		initWriter();
		retrieveSbpData();
	}

	public void processNoNegative() {
		initReader();
		initWriter();
		initWriter1();
		retrieveNoNegativeData();
	}

	public void processPercentagePositive() {
		initReader();
		initWriter();
		retrievePositivePercentageData();
	}

	private void initWriter() {
		outputFile = new File(outputPath);
		try {
			outputWriter = new PrintWriter(new FileOutputStream(outputFile));
		} catch (FileNotFoundException e) {
			System.out.printf("Error opening the file %s.\n", outputPath);
		}
	}

	private void initWriter1() {
		outputFile1 = new File(distancesOutputPath);
		try {
			outputWriter1 = new PrintWriter(new FileOutputStream(outputFile1));
		} catch (FileNotFoundException e) {
			System.out.printf("Error opening the file %s.\n", distancesOutputPath);
		}
	}

	private void initReader() {
		File file = new File(inputPath);
		try {
			inputReader = new Scanner(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			System.out.printf("File %s was not found or could not be opened.\n", inputPath);
		}
	}

	private void retrieveNoNegativeData() {
		while (inputReader.hasNextLine()) {
			String strLine = inputReader.nextLine();
			String tokens[] = strLine.split("\t");

			if (Integer.parseInt(tokens[3]) == 0) {
				outputWriter.println(tokens[0] + "\t" + tokens[1] + "\t" + tokens[4]);
			}
			outputWriter1.println(tokens[0] + "\t" + tokens[1] + "\t" + tokens[4]);
		}
		inputReader.close();
		outputWriter.close();
		outputWriter1.close();
	}

	private void retrieveMorePositiveData() {
		while (inputReader.hasNextLine()) {
			String strLine = inputReader.nextLine();
			String tokens[] = strLine.split("\t");

			if (Integer.parseInt(tokens[2]) > Integer.parseInt(tokens[3])) {
				outputWriter.println(tokens[0] + "\t" + tokens[1] + "\t" + tokens[4]);
			}
		}
		inputReader.close();
		outputWriter.close();
	}

	private void retrieveSbpData() {
		while (inputReader.hasNextLine()) {
			String strLine = inputReader.nextLine();
			String tokens[] = strLine.split("\t");

			if (Integer.parseInt(tokens[2]) != 1000000000) {
				outputWriter.println(tokens[0] + "\t" + tokens[1] + "\t" + tokens[2]);
			}
		}
		inputReader.close();
		outputWriter.close();
	}

	private void retrieveOnePositiveData() {
		while (inputReader.hasNextLine()) {
			String strLine = inputReader.nextLine();
			String tokens[] = strLine.split("\t");

			if (Integer.parseInt(tokens[2]) > 0) {
				outputWriter.println(tokens[0] + "\t" + tokens[1] + "\t" + tokens[4]);
			}
		}
		inputReader.close();
		outputWriter.close();
	}

	private void retrievePositivePercentageData() {
		DecimalFormat decimalFormat = new DecimalFormat("#.###");

		while (inputReader.hasNextLine()) {
			String strLine = inputReader.nextLine();
			String tokens[] = strLine.split("\t");

			int pos = Integer.parseInt(tokens[2]);
			int neg = Integer.parseInt(tokens[3]);

			outputWriter
					.println(tokens[0] + "\t" + tokens[1] + "\t" + decimalFormat.format(((double) pos) / (pos + neg)));

		}
		inputReader.close();
		outputWriter.close();
	}
}
