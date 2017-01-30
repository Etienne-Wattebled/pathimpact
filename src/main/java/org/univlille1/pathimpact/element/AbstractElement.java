package org.univlille1.pathimpact.element;

public abstract class AbstractElement implements ElementItf {
	private String nom;
	
	public AbstractElement(String nom) {
		this.nom = nom;
	}
	
	@Override
	public String getNom() {
		return nom;
	}
	
	@Override
	public String toString() {
		return nom;
	}
	
	@Override
	public void print() {
		System.out.println(nom);
	}
}
