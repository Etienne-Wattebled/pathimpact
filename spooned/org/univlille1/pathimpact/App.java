

package org.univlille1.pathimpact;


public class App {
    public static final java.lang.String srcMainJava = new java.lang.StringBuilder().append("src").append(java.io.File.separator).append("main").append(java.io.File.separator).append("java").toString();

    public static void main(java.lang.String[] args) throws java.lang.InterruptedException {
        org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();
        options.addOption("projectName", true, "Nom du répertoire du projet dans le dossier input.");
        org.apache.commons.cli.CommandLineParser parser = new org.apache.commons.cli.DefaultParser();
        try {
            org.apache.commons.cli.CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("projectName")) {
                spoon.Launcher l = new spoon.Launcher();
                org.univlille1.pathimpact.stacktrace.builder.StackTracesProcessor p = new org.univlille1.pathimpact.stacktrace.builder.StackTracesProcessor();
                l.addProcessor(p);
                l.run(new java.lang.String[]{ "-i" , new java.lang.StringBuilder().append("input").append(java.io.File.separator).append(cmd.getOptionValue("projectName")).append(java.io.File.separator).append(org.univlille1.pathimpact.App.srcMainJava).toString() , "--output-type" , "nooutput" , "--no-copy-resources" });
                java.util.List<org.univlille1.pathimpact.element.ElementItf> st = p.getStackTrace();
                for (org.univlille1.pathimpact.element.ElementItf e : st) {
                    java.lang.System.out.println(e.getNom());
                }
            }else {
                org.apache.commons.cli.HelpFormatter formatter = new org.apache.commons.cli.HelpFormatter();
                formatter.printHelp("List of parameters", options);
                java.lang.System.exit(0);
            }
        } catch (org.apache.commons.cli.ParseException pe) {
        }
    }
}

