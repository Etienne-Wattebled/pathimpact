

package org.univlille1.pathimpact.element;


public enum Evenement implements org.univlille1.pathimpact.element.ElementItf {
RETURN("r"), END_OF_PROGRAM("x");
    private java.lang.String nom;

    Evenement(java.lang.String evenement) {
        nom = evenement;
    }

    @java.lang.Override
    public java.lang.String getNom() {
        return nom;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return nom;
    }
}

