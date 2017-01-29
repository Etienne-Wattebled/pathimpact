

package org.univlille1.pathimpact.stacktrace.builder;


public class StackTracesProcessor extends spoon.processing.AbstractProcessor<spoon.reflect.declaration.CtMethod<?>> {
    private java.util.Map<spoon.reflect.declaration.CtMethod<?>, java.util.List<spoon.reflect.declaration.CtMethod<?>>> linksBetweenMethods;

    private java.util.List<spoon.reflect.declaration.CtMethod<?>> mainMethods;

    private java.util.List<org.univlille1.pathimpact.element.ElementItf> stackTrace;

    private java.util.Map<java.lang.String, org.univlille1.pathimpact.element.Methode> mapMethodes;

    public StackTracesProcessor() {
        linksBetweenMethods = new java.util.HashMap<spoon.reflect.declaration.CtMethod<?>, java.util.List<spoon.reflect.declaration.CtMethod<?>>>();
        mainMethods = new java.util.LinkedList<spoon.reflect.declaration.CtMethod<?>>();
        stackTrace = new java.util.LinkedList<org.univlille1.pathimpact.element.ElementItf>();
        mapMethodes = new java.util.HashMap<java.lang.String, org.univlille1.pathimpact.element.Methode>();
    }

    @java.lang.Override
    public void process(spoon.reflect.declaration.CtMethod<?> method) {
        if ((method.getSimpleName().equals("main")) && (method.hasModifier(spoon.reflect.declaration.ModifierKind.STATIC))) {
            java.util.List<spoon.reflect.declaration.CtParameter<?>> parameters = method.getParameters();
            if ((parameters != null) && ((parameters.size()) == 1)) {
                spoon.reflect.declaration.CtParameter<?> parameter = parameters.get(0);
                spoon.reflect.reference.CtTypeReference<?> type = parameter.getType();
                if (type instanceof spoon.reflect.reference.CtArrayTypeReference) {
                    mainMethods.add(method);
                }
            }
        }
        java.util.List<spoon.reflect.code.CtInvocation<?>> invocations = method.getBody().getElements(new org.univlille1.pathimpact.filter.CtInvocationFilter());
        for (spoon.reflect.code.CtInvocation<?> invoc : invocations) {
            spoon.reflect.declaration.CtExecutable<?> c = invoc.getExecutable().getDeclaration();
            if (c instanceof spoon.reflect.declaration.CtMethod<?>) {
                spoon.reflect.declaration.CtMethod<?> m = ((spoon.reflect.declaration.CtMethod<?>) (c));
                java.util.List<spoon.reflect.declaration.CtMethod<?>> l = null;
                if (linksBetweenMethods.containsKey(method)) {
                    l = linksBetweenMethods.get(method);
                }else {
                    l = new java.util.LinkedList<spoon.reflect.declaration.CtMethod<?>>();
                    linksBetweenMethods.put(method, l);
                }
                l.add(m);
            }
        }
    }

    @java.lang.Override
    public void processingDone() {
        if ((mainMethods.size()) == 1) {
            spoon.reflect.declaration.CtMethod<?> method = mainMethods.get(0);
            java.util.LinkedList<org.univlille1.pathimpact.element.ElementItf> path = new java.util.LinkedList<org.univlille1.pathimpact.element.ElementItf>();
            path.add(new org.univlille1.pathimpact.element.Methode(method.getReference().toString()));
            construireStackTrace(method, path);
        }else {
            java.lang.System.err.println("Erreur: Il ne doit y avoir qu'une seule méthode main.");
            java.lang.System.exit(0);
        }
    }

    private void construireStackTrace(spoon.reflect.declaration.CtMethod<?> method, java.util.LinkedList<org.univlille1.pathimpact.element.ElementItf> path) {
        java.util.List<spoon.reflect.declaration.CtMethod<?>> methods = linksBetweenMethods.get(method);
        if ((methods != null) && (!(methods.isEmpty()))) {
            for (spoon.reflect.declaration.CtMethod<?> m : methods) {
                java.lang.String ref = m.getReference().toString();
                org.univlille1.pathimpact.element.Methode methode = mapMethodes.get(ref);
                if (methode == null) {
                    methode = new org.univlille1.pathimpact.element.Methode(ref);
                    mapMethodes.put(ref, methode);
                }
                path.add(methode);
                construireStackTrace(m, path);
                path.removeLast();
            }
        }else {
            stackTrace.addAll(path);
            int nb = path.size();
            for (int i = 1; i <= nb; i++) {
                stackTrace.add(org.univlille1.pathimpact.element.Evenement.RETURN);
            }
            stackTrace.add(org.univlille1.pathimpact.element.Evenement.END_OF_PROGRAM);
        }
    }

    public java.util.List<org.univlille1.pathimpact.element.ElementItf> getStackTrace() {
        return stackTrace;
    }
}

