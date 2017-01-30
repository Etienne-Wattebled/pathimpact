package org.univlille1.pathimpact;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.univlille1.pathimpact.element.ElementItf;
import org.univlille1.pathimpact.grammaire.Grammaire;
import org.univlille1.pathimpact.grammaire.Grammaire.Regle;
import org.univlille1.pathimpact.stacktrace.builder.StackTracesProcessor;

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
				List<ElementItf> st = p.getStackTrace();
				
				for (ElementItf element : st) {
					System.out.print(element.getNom() + " ");
				}
				System.out.println("");
				
				Grammaire grammaire = new Grammaire();
				ElementItf before = st.get(0);
				ElementItf last = before;
				ElementItf last2 = null;
				grammaire.ajouterElementDansS(before);
				st.remove(0);
				for (ElementItf element : st) {
					LinkedList<ElementItf> lR = new LinkedList<ElementItf>();
					lR.add(last);
					lR.add(element);
					grammaire.ajouterElementDansS(element);
					Regle r = grammaire.getRegleQuiProduit(lR);
					if (r != null) {
						grammaire.appliquerRegleSurS(r);
					} else {
						int nb = 2;
						while (nb >= 2) {
							ListIterator<ElementItf> it = grammaire.getS().getListIteratorEnd();
							if (it.hasPrevious()) {
								last = it.previous();
							} else {
								break;
							}
							if (it.hasPrevious()) {
								last2 = it.previous();
							} else {
								break;
							}
							lR = new LinkedList<ElementItf>();
							lR.add(last2);
							lR.add(last);
							r = grammaire.ajouterRegle(lR);
							nb = grammaire.appliquerRegleSurS(r);
							grammaire.simplierRegles();
						}
					}
					last = grammaire.getS().getLastElement();
				}
				grammaire.print();
			} else {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("List of parameters", options);
				System.exit(0);
			}
		} catch (ParseException pe) {
			
		}
	}
}
