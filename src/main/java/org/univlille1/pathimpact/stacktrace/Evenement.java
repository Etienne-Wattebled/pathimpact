package org.univlille1.pathimpact.stacktrace;

public enum Evenement implements ElementStackTrace {
	RETURN("r"), END_OF_PROGRAM("x");
	
	private String id;
	
	Evenement(String evenement) {
		id = evenement;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public boolean estUnEvenement() {
		return true;
	}

	@Override
	public boolean estUneMethode() {
		return false;
	}
	
	@Override
	public String toString() {
		return id;
	}
}
