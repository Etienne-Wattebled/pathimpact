

package org.univlille1.pathimpact.grammaire;


public class Grammaire {
    protected org.univlille1.pathimpact.grammaire.Grammaire.S s;

    protected java.util.List<org.univlille1.pathimpact.grammaire.Grammaire.Regle> regles;

    protected int prochainIdRegle;

    protected com.google.common.collect.Table<org.univlille1.pathimpact.grammaire.Grammaire.Regle, org.univlille1.pathimpact.element.ElementItf, java.lang.Integer> utilisationsRegles;

    public Grammaire() {
        reset();
    }

    protected void reset() {
        this.s = new org.univlille1.pathimpact.grammaire.Grammaire.S();
        this.regles = new java.util.LinkedList<org.univlille1.pathimpact.grammaire.Grammaire.Regle>();
        this.prochainIdRegle = 0;
        this.utilisationsRegles = com.google.common.collect.HashBasedTable.create();
    }

    public org.univlille1.pathimpact.grammaire.Grammaire.Regle ajouterRegle(java.util.List<org.univlille1.pathimpact.element.ElementItf> elements) {
        java.lang.String id = java.lang.String.valueOf(prochainIdRegle);
        prochainIdRegle = (prochainIdRegle) + 1;
        org.univlille1.pathimpact.grammaire.Grammaire.Regle regle = new org.univlille1.pathimpact.grammaire.Grammaire.Regle(elements, id);
        regles.add(regle);
        return regle;
    }

    public void supprimerRegle(org.univlille1.pathimpact.grammaire.Grammaire.Regle regle) {
        regles.remove(regle);
        java.util.Map<org.univlille1.pathimpact.element.ElementItf, java.lang.Integer> m = utilisationsRegles.row(regle);
        java.util.Map<org.univlille1.pathimpact.grammaire.Grammaire.Regle, java.util.Map<org.univlille1.pathimpact.element.ElementItf, java.lang.Integer>> m2 = utilisationsRegles.rowMap();
        m2.remove(regle);
        java.util.Set<org.univlille1.pathimpact.element.ElementItf> elements = m.keySet();
        for (org.univlille1.pathimpact.element.ElementItf e : elements) {
            if (e instanceof org.univlille1.pathimpact.grammaire.Grammaire.Regle) {
                ((org.univlille1.pathimpact.grammaire.Grammaire.Regle) (e)).supprimerElement(regle);
            }
            if (e instanceof org.univlille1.pathimpact.grammaire.Grammaire.S) {
                ((org.univlille1.pathimpact.grammaire.Grammaire.S) (e)).supprimerElement(regle);
            }
        }
    }

    public void appliquerRegle(org.univlille1.pathimpact.grammaire.Grammaire.Regle regleAAppliquer, org.univlille1.pathimpact.grammaire.Grammaire.Regle regle) {
        int nb = regleAAppliquer.appliquer(regle.getElements());
    }

    public void appliquerRegleSurS(org.univlille1.pathimpact.grammaire.Grammaire.Regle regle) {
        int nb = regle.appliquer(s.getElements());
    }

    public int getNbRegles() {
        return regles.size();
    }

    public org.univlille1.pathimpact.grammaire.Grammaire.S getS() {
        return s;
    }

    public java.util.List<org.univlille1.pathimpact.grammaire.Grammaire.Regle> getRegles() {
        return regles;
    }

    public void ajouterElementDansS(org.univlille1.pathimpact.element.ElementItf element) {
        s.ajouterElement(element);
    }

    public void supprimerElementDansS(org.univlille1.pathimpact.element.ElementItf element) {
        s.supprimerElement(element);
    }

    public org.univlille1.pathimpact.grammaire.Grammaire.Regle getRegleQuiProduit(java.util.List<org.univlille1.pathimpact.element.ElementItf> elements) {
        for (org.univlille1.pathimpact.grammaire.Grammaire.Regle regle : regles) {
            if (regle.produit(elements)) {
                return regle;
            }
        }
        return null;
    }

    public void print() {
        java.lang.System.out.println("S =");
        java.util.Iterator<org.univlille1.pathimpact.element.ElementItf> itE = s.listIterator();
        while (itE.hasNext()) {
            org.univlille1.pathimpact.element.ElementItf element = itE.next();
            java.lang.System.out.print(element.getNom());
            java.lang.System.out.print(" ");
        } 
        java.lang.System.out.println("R = {");
        java.util.Iterator<org.univlille1.pathimpact.grammaire.Grammaire.Regle> itR = regles.listIterator();
        while (itR.hasNext()) {
            org.univlille1.pathimpact.grammaire.Grammaire.Regle regle = itR.next();
            regle.print();
        } 
        java.lang.System.out.println("}");
    }

    public class Regle extends org.univlille1.pathimpact.element.AbstractElement {
        protected java.util.List<org.univlille1.pathimpact.element.ElementItf> elements;

        protected Regle(java.util.List<org.univlille1.pathimpact.element.ElementItf> elements, java.lang.String nom) {
            super(nom);
            this.elements = elements;
        }

        public java.util.List<org.univlille1.pathimpact.element.ElementItf> getElements() {
            return this.elements;
        }

        public int getNbElements() {
            return elements.size();
        }

        public org.univlille1.pathimpact.element.ElementItf getElement(int i) {
            return elements.get(i);
        }

        protected void ajouterElement(org.univlille1.pathimpact.element.ElementItf element) {
            elements.add(element);
        }

        protected void supprimerElement(org.univlille1.pathimpact.element.ElementItf element) {
            elements.removeIf(new org.univlille1.pathimpact.grammaire.Grammaire.PredicateSameInstance(element));
        }

        protected void supprimerElements(java.util.List<org.univlille1.pathimpact.element.ElementItf> elements) {
            this.elements.removeAll(elements);
        }

        public void print() {
            java.lang.System.out.print(getNom());
            java.lang.System.out.print(" =>");
            java.util.Iterator<org.univlille1.pathimpact.element.ElementItf> it = elements.listIterator();
            while (it.hasNext()) {
                java.lang.System.out.print(" ");
                java.lang.System.out.print(it.next());
            } 
        }

        public boolean contientElement(org.univlille1.pathimpact.element.ElementItf element) {
            return elements.contains(element);
        }

        public boolean produit(java.util.List<org.univlille1.pathimpact.element.ElementItf> elements) {
            if ((this.elements.size()) != (elements.size())) {
                return false;
            }
            java.util.Iterator<org.univlille1.pathimpact.element.ElementItf> it1 = this.elements.listIterator();
            java.util.Iterator<org.univlille1.pathimpact.element.ElementItf> it2 = elements.listIterator();
            while ((it1.hasNext()) && (it2.hasNext())) {
                org.univlille1.pathimpact.element.ElementItf e1 = it1.next();
                org.univlille1.pathimpact.element.ElementItf e2 = it2.next();
                if (e1 != e2) {
                    return false;
                }
            } 
            return true;
        }

        protected int desappliquer(java.util.List<org.univlille1.pathimpact.element.ElementItf> elements) {
            int nb = 0;
            java.util.ListIterator<org.univlille1.pathimpact.element.ElementItf> it = elements.listIterator();
            org.univlille1.pathimpact.element.ElementItf element = null;
            while (it.hasNext()) {
                element = it.next();
                if (element == (this)) {
                    it.remove();
                    for (org.univlille1.pathimpact.element.ElementItf e : elements) {
                        it.add(e);
                    }
                    nb = nb + 1;
                }
            } 
            return nb;
        }

        protected int appliquer(java.util.List<org.univlille1.pathimpact.element.ElementItf> elements) {
            if ((this.elements.size()) > (elements.size())) {
                return 0;
            }
            int nb = 0;
            java.util.ListIterator<org.univlille1.pathimpact.element.ElementItf> itE = elements.listIterator();
            java.util.ListIterator<org.univlille1.pathimpact.element.ElementItf> itR = null;
            if ((itE.hasNext()) && (!(this.elements.isEmpty()))) {
                org.univlille1.pathimpact.element.ElementItf elementR = null;
                org.univlille1.pathimpact.element.ElementItf elementE = null;
                while (itE.hasNext()) {
                    itR = this.elements.listIterator();
                    elementR = itR.next();
                    elementE = itE.next();
                    int n = 0;
                    int rollback = itE.previousIndex();
                    while (((elementR == elementE) && (itE.hasNext())) && (itR.hasNext())) {
                        n = n + 1;
                        elementE = itE.next();
                        elementR = itR.next();
                    } 
                    if (elementR == elementE) {
                        n = n + 1;
                    }
                    boolean ok = n == (this.elements.size());
                    if (n != 0) {
                        while ((itE.previousIndex()) != rollback) {
                            if (ok) {
                                itE.remove();
                            }else {
                                itE.previous();
                            }
                        } 
                        if (ok) {
                            itE.add(this);
                            nb = nb + 1;
                        }
                    }
                } 
            }
            return nb;
        }
    }

    protected class S extends org.univlille1.pathimpact.element.AbstractElement {
        private java.util.List<org.univlille1.pathimpact.element.ElementItf> elements;

        protected S() {
            super("S");
        }

        protected void ajouterElement(org.univlille1.pathimpact.element.ElementItf element) {
            elements.add(element);
        }

        protected void supprimerElement(org.univlille1.pathimpact.element.ElementItf element) {
            elements.removeIf(new org.univlille1.pathimpact.grammaire.Grammaire.PredicateSameInstance(element));
        }

        protected java.util.List<org.univlille1.pathimpact.element.ElementItf> getElements() {
            return elements;
        }

        protected java.util.ListIterator<org.univlille1.pathimpact.element.ElementItf> listIterator() {
            return elements.listIterator();
        }
    }

    protected class PredicateSameInstance implements java.util.function.Predicate<org.univlille1.pathimpact.element.ElementItf> {
        private org.univlille1.pathimpact.element.ElementItf element;

        public PredicateSameInstance(org.univlille1.pathimpact.element.ElementItf element) {
            this.element = element;
        }

        @java.lang.Override
        public boolean test(org.univlille1.pathimpact.element.ElementItf e) {
            return e == (element);
        }
    }
}

