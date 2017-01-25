package org.univlille1.pathimpact.stacktrace;

public class Methode implements ElementStackTrace {
	public Methode(String nom) {
		this.nom = nom;
	}
	
	private String nom;
	
	@Override
	public String getId() {
		return nom;
	}

	@Override
	public boolean estUnEvenement() {
		return false;
	}

	@Override
	public boolean estUneMethode() {
		return true;
	}
	
	@Override
	public String toString() {
		return nom;
	}

}
