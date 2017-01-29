

package org.univlille1.pathimpact.element;


public abstract class AbstractElement implements org.univlille1.pathimpact.element.ElementItf {
    private java.lang.String nom;

    public AbstractElement(java.lang.String nom) {
        this.nom = nom;
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

