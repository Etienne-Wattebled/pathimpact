

package org.univlille1.pathimpact.stacktrace;


public class Methode implements org.univlille1.pathimpact.stacktrace.ElementStackTrace {
    public Methode(java.lang.String nom) {
        this.nom = nom;
    }

    private java.lang.String nom;

    @java.lang.Override
    public java.lang.String getId() {
        return nom;
    }

    @java.lang.Override
    public boolean estUnEvenement() {
        return false;
    }

    @java.lang.Override
    public boolean estUneMethode() {
        return true;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return nom;
    }
}

