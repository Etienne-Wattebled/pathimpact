package org.univlille1.pathimpact;

import spoon.Launcher;

public class Test {
	public static void main(String args[]) {
		//new Launcher().run(new String[] {"-x","-g","-i","src/main/java"});
		a(10);
	}
	public static void a(int a) {
		b(6);
		c(10);
	}
	public static void b(int a) {
		c(50);
	}
	public static void c(int a) {
		
	}
}
