package org.univlille1.pathimpact;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.univlille1.pathimpact.stacktrace.StackTrace;
import org.univlille1.pathimpact.stacktrace.StackTracesProcessor;

import spoon.Launcher;

public class App {
	public final static String srcMainJava = new StringBuilder().append("src").append(File.separator).append("main")
			.append(File.separator).append("java").toString();

	public static void main(String args[]) throws InterruptedException {
		Options options = new Options();
		options.addOption("projectName",true,"Nom du répertoire du projet dans le dossier input.");
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("projectName")) {
				
				Launcher l = new Launcher();
				StackTracesProcessor p = new StackTracesProcessor();
				l.addProcessor(p);
				l.run(new String[] {
					"-i",new StringBuilder().append("input").append(File.separator).append(cmd.getOptionValue("projectName")).append(File.separator).append(srcMainJava).toString(),
					"--output-type", "nooutput",
					"--no-copy-resources"
				});
				StackTrace st = p.construireStackTrace();
				st.print();
			} else {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("list of parameters", options);
				System.exit(0);
			}
		} catch (ParseException pe) {
			
		}
	}
}
