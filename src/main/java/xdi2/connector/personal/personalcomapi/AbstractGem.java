package xdi2.connector.personal.personalcomapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGem {

	private File file;

	public AbstractGem(File file) {

		this.file = file;
	}

	protected String loadLine(int lineNr) throws IOException {

		String line;

		String curLine;
		List<String> lines = new ArrayList<String> ();

		BufferedReader reader = new BufferedReader(new FileReader(this.file));
		while ((curLine = reader.readLine()) != null) lines.add(curLine);
		reader.close();

		if (lineNr >= lines.size()) return "";

		line = lines.get(lineNr);

		return line;
	}

	protected void saveLine(int newLineNr, String newLine) throws IOException {

		int curLineNr = 0;
		String curLine;
		List<String> lines = new ArrayList<String> ();

		BufferedReader reader = new BufferedReader(new FileReader(this.file));
		while ((curLine = reader.readLine()) != null) {

			if (newLineNr == curLineNr) 
				lines.add(newLine);
			else
				lines.add(curLine);
			
			curLineNr++;
		}
		reader.close();

		FileWriter writer = new FileWriter(this.file);
		for (String line : lines) writer.write(line + "\n");
		writer.close();
	}
}
