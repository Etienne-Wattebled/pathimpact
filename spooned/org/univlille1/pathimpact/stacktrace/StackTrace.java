

package org.univlille1.pathimpact.stacktrace;


public class StackTrace {
    private java.util.List<org.univlille1.pathimpact.stacktrace.ElementStackTrace> stack;

    public StackTrace() {
        stack = new java.util.LinkedList<org.univlille1.pathimpact.stacktrace.ElementStackTrace>();
    }

    public void ajouterElement(org.univlille1.pathimpact.stacktrace.ElementStackTrace e) {
        stack.add(e);
    }

    public void supprimerDernierElement() {
        stack.remove(((stack.size()) - 1));
    }

    public void supprimerElement(int i) {
        stack.remove(i);
    }

    public org.univlille1.pathimpact.stacktrace.ElementStackTrace getElement(int i) {
        return stack.get(i);
    }

    public void print() {
        for (org.univlille1.pathimpact.stacktrace.ElementStackTrace e : stack) {
            java.lang.System.out.println(e);
        }
    }

    public void ajouterElements(java.util.List<org.univlille1.pathimpact.stacktrace.ElementStackTrace> l) {
        stack.addAll(l);
    }

    public java.util.List<org.univlille1.pathimpact.stacktrace.ElementStackTrace> getElements() {
        return stack;
    }

    public int size() {
        return stack.size();
    }
}

