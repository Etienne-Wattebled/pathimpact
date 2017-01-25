package org.univlille1.pathimpact.stacktrace;

import java.util.LinkedList;
import java.util.List;

public class StackTrace {
	private List<ElementStackTrace> stack;
	
	public StackTrace() {
		stack = new LinkedList<ElementStackTrace>();
	}
	
	public void ajouterElement(ElementStackTrace e) {
		stack.add(e);
	}
	public void supprimerDernierElement() {
		stack.remove(stack.size()-1);
	}
	public void supprimerElement(int i) {
		stack.remove(i);
	}
	
	public ElementStackTrace getElement(int i) {
		return stack.get(i);
	}
	
	public void print() {
		for (ElementStackTrace e : stack) {
			System.out.println(e);
		}
	}
	public void ajouterElements(List<ElementStackTrace> l) {
		stack.addAll(l);
	}
	public List<ElementStackTrace> getElements() {
		return stack;
	}
	public int size() {
		return stack.size();
	}
}
