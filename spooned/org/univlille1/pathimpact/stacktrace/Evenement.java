

package org.univlille1.pathimpact.stacktrace;


public enum Evenement implements org.univlille1.pathimpact.stacktrace.ElementStackTrace {
RETURN("r"), END_OF_PROGRAM("x");
    private java.lang.String id;

    Evenement(java.lang.String evenement) {
        id = evenement;
    }

    @java.lang.Override
    public java.lang.String getId() {
        return id;
    }

    @java.lang.Override
    public boolean estUnEvenement() {
        return true;
    }

    @java.lang.Override
    public boolean estUneMethode() {
        return false;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return id;
    }
}

