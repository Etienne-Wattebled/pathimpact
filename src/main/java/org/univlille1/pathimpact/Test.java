package org.univlille1.pathimpact;

import java.util.LinkedList;
import java.util.ListIterator;

import org.univlille1.pathimpact.element.ElementItf;

import spoon.Launcher;

public class Test {
	public static void main(String args[]) {
		//new Launcher().run(new String[] {"-x","-g","-i","src/main/java"});
		//a(10);
		LinkedList<Integer> l = new LinkedList<Integer>();
		l.add(6);
		l.add(10);
		l.add(100);
		ListIterator<Integer> it = l.listIterator();
		System.out.println("E = " + it.next());
		System.out.println("E = " + it.next());
		it.remove();
		for (Integer i : l) {
			System.out.println(i);
		}
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
