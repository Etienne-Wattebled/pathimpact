package org.univlille1.pathimpact;

import java.io.File;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.univlille1.pathimpact.element.ElementItf;
import org.univlille1.pathimpact.grammaire.Grammaire;
import org.univlille1.pathimpact.sequitur.Sequitur;
import org.univlille1.pathimpact.stacktrace.builder.StackTracesProcessor;

import spoon.Launcher;

public class App {
	public final static String srcMainJava = new StringBuilder().append("src").append(File.separator).append("main")
			.append(File.separator).append("java").toString();

	public static void main(String args[]) throws InterruptedException {
		Options options = new Options();
		options.addOption("projectPath",true,"Chemin du projet dans le dossier input. ( exemple: input/test )");
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("projectPath")) {
				Launcher l = new Launcher();
				StackTracesProcessor p = new StackTracesProcessor();
				l.addProcessor(p);
				l.run(new String[] {
					"-i",new StringBuilder().append(cmd.getOptionValue("projectPath")).append(File.separator).append(srcMainJava).toString(),
					"--output-type", "nooutput",
					"--no-copy-resources"
				});
				List<ElementItf> st = p.getStackTrace();
				Sequitur sequitur = new Sequitur(st);
				Grammaire grammaire = sequitur.executerSequitur();
				grammaire.print();
			}
		} catch (ParseException pe) {
			erreur(options);
		}
	}
	public static void erreur(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("List of parameters", options);
		System.exit(0);
	}
}
