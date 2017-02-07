package org.univlille1.pathimpact.stacktrace;

import org.univlille1.pathimpact.element.ElementItf;
import org.univlille1.pathimpact.stacktrace.builder.StackTracesProcessor;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemErrRule;

import spoon.Launcher;

public class StackTraceTest {
	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	@Rule
	public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

	private Launcher launcher;
	private StackTracesProcessor stackTracesProcessor;

	@Before
	public void setUp() {
		launcher = new Launcher();
		stackTracesProcessor = new StackTracesProcessor();
		launcher.addProcessor(stackTracesProcessor);
	}

	@Test
	public void testNoMain() {
		exit.expectSystemExitWithStatus(0);
		try {
			launcher.run(new String[] { "-i", "src/test/resources/TestProject2", "--output-type", "nooutput",
					"--no-copy-resources" });
		} finally {
			assertEquals("Erreur: Il ne doit y avoir qu'une seule méthode main.\n",
					systemErrRule.getLogWithNormalizedLineSeparator());
		}
	}

	@Test
	public void testTwoMains() {
		exit.expectSystemExitWithStatus(0);
		try {
			launcher.run(new String[] { "-i", "src/test/resources/TestProject3", "--output-type", "nooutput",
					"--no-copy-resources" });
		} finally {
			assertEquals("Erreur: Il ne doit y avoir qu'une seule méthode main.\n",
					systemErrRule.getLogWithNormalizedLineSeparator());
		}
	}

	@Test
	public void testStackTrace() {
		launcher.run(new String[] { "-i", "src/test/resources/TestProject", "--output-type", "nooutput",
				"--no-copy-resources" });
		List<ElementItf> l = stackTracesProcessor.getStackTrace();
		String expected[] = new String[] { "pathimpact.Test#main(java.lang.String[])", "pathimpact.Test#m()",
				"pathimpact.Test#m3()", "r", "r", "r", "x", "pathimpact.Test#main(java.lang.String[])",
				"pathimpact.Test#m2()", "pathimpact.Test#m4()", "r", "r", "r", "x",
				"pathimpact.Test#main(java.lang.String[])", "pathimpact.Test#m5()", "r", "r", "x" };
		int i = 0;
		for (ElementItf e : l) {
			assertEquals(expected[i], e.getNom());
			i = i + 1;
		}
	}

	@Test
	public void testStackTraceRecursivite() {
		launcher.run(new String[] { 
				"-i", "src/test/resources/TestProject4",
				"--output-type", "nooutput",
				"--no-copy-resources"
		});
		List<ElementItf> l = stackTracesProcessor.getStackTrace();
		String expected[] = new String[] {
			"pathimpact.Test#main(java.lang.String[])","pathimpact.Test#m1()","pathimpact.Test#m1()","r","r","r","x",
			"pathimpact.Test#main(java.lang.String[])","pathimpact.Test#m()","pathimpact.Test#m2()","pathimpact.Test#m3()","pathimpact.Test#m()","r","r","r","r","r","x",
			"pathimpact.Test#main(java.lang.String[])","pathimpact.Test#m()","pathimpact.Test#m4()","r","r","r","x"
		};
		int i = 0;
		for (ElementItf e : l) {
			assertEquals(expected[i], e.getNom());
			i = i + 1;
		}
	}
}
