package org.univlille1.pathimpact.element;

public enum Evenement implements ElementItf {
	RETURN("r"), END_OF_PROGRAM("x");
	
	private String nom;
	
	Evenement(String evenement) {
		nom = evenement;
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
