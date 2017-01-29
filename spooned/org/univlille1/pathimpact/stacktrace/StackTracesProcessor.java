

package org.univlille1.pathimpact.stacktrace;


public class StackTracesProcessor extends spoon.processing.AbstractProcessor<spoon.reflect.declaration.CtMethod<?>> {
    private java.util.Map<java.lang.String, spoon.reflect.declaration.CtMethod<?>> getCtMethod;

    private java.util.Map<spoon.reflect.declaration.CtMethod<?>, java.lang.String> getMethodKey;

    private java.util.Map<spoon.reflect.declaration.CtMethod<?>, java.util.List<spoon.reflect.declaration.CtMethod<?>>> linksBetweenMethods;

    private java.util.List<spoon.reflect.declaration.CtMethod<?>> mainMethods;

    public StackTracesProcessor() {
        linksBetweenMethods = new java.util.HashMap<spoon.reflect.declaration.CtMethod<?>, java.util.List<spoon.reflect.declaration.CtMethod<?>>>();
        mainMethods = new java.util.LinkedList<spoon.reflect.declaration.CtMethod<?>>();
        getCtMethod = new java.util.HashMap<java.lang.String, spoon.reflect.declaration.CtMethod<?>>();
        getMethodKey = new java.util.HashMap<spoon.reflect.declaration.CtMethod<?>, java.lang.String>();
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
        spoon.reflect.declaration.CtClass<?> ctClass = method.getParent(spoon.reflect.declaration.CtClass.class);
        java.lang.StringBuilder key = new java.lang.StringBuilder();
        key.append(ctClass.getSimpleName());
        key.append("#");
        key.append(method.getSimpleName());
        key.append("(");
        java.util.List<spoon.reflect.declaration.CtParameter<?>> parameters = method.getParameters();
        java.util.Iterator<spoon.reflect.declaration.CtParameter<?>> it = parameters.listIterator();
        if (it.hasNext()) {
            spoon.reflect.declaration.CtParameter<?> p = it.next();
            key.append(p.getType().toString());
            while (it.hasNext()) {
                key.append(", ");
                p = it.next();
                key.append(p.getType().toString());
            } 
        }
        key.append(")");
        java.lang.String methodKey = key.toString();
        java.lang.System.out.println(methodKey);
        getCtMethod.put(methodKey, method);
        getMethodKey.put(method, methodKey);
    }

    @java.lang.Override
    public void processingDone() {
        java.util.Collection<spoon.reflect.declaration.CtMethod<?>> methodes = getCtMethod.values();
        for (spoon.reflect.declaration.CtMethod<?> m : methodes) {
            java.util.List<spoon.reflect.code.CtInvocation<?>> invocations = m.getBody().getElements(new org.univlille1.pathimpact.CtInvocationFilter());
            java.lang.System.out.println(invocations.size());
            for (spoon.reflect.code.CtInvocation<?> i : invocations) {
                if (linksBetweenMethods.containsKey(m)) {
                    linksBetweenMethods.get(m).add(getCtMethod.get(i.getReferencedTypes()));
                }else {
                    java.util.List<spoon.reflect.declaration.CtMethod<?>> l = new java.util.LinkedList<spoon.reflect.declaration.CtMethod<?>>();
                    l.add(getCtMethod.get(i.getExecutable().toString()));
                    java.lang.System.out.println(i.getExecutable().toString());
                    linksBetweenMethods.put(m, l);
                }
            }
        }
    }

    public org.univlille1.pathimpact.stacktrace.StackTrace construireStackTrace() {
        if ((mainMethods.size()) == 1) {
            spoon.reflect.declaration.CtMethod<?> method = mainMethods.get(0);
            org.univlille1.pathimpact.stacktrace.StackTrace result = new org.univlille1.pathimpact.stacktrace.StackTrace();
            org.univlille1.pathimpact.stacktrace.StackTrace path = new org.univlille1.pathimpact.stacktrace.StackTrace();
            path.ajouterElement(new org.univlille1.pathimpact.stacktrace.Methode(getMethodKey.get(method)));
            construireStackTrace(result, method, path);
            return result;
        }else {
            java.lang.System.err.println("Erreur: Il ne doit y avoir qu'une seule méthode main.");
            java.lang.System.exit(0);
            return null;
        }
    }

    private void construireStackTrace(org.univlille1.pathimpact.stacktrace.StackTrace result, spoon.reflect.declaration.CtMethod<?> method, org.univlille1.pathimpact.stacktrace.StackTrace path) {
        java.util.List<spoon.reflect.declaration.CtMethod<?>> methods = linksBetweenMethods.get(method);
        if ((methods != null) && (!(methods.isEmpty()))) {
            for (spoon.reflect.declaration.CtMethod<?> m : methods) {
                path.ajouterElement(new org.univlille1.pathimpact.stacktrace.Methode(getMethodKey.get(m)));
                construireStackTrace(result, m, path);
                path.supprimerDernierElement();
            }
        }else {
            result.ajouterElements(path.getElements());
            int nb = path.size();
            for (int i = 1; i <= nb; i++) {
                result.ajouterElement(org.univlille1.pathimpact.stacktrace.Evenement.RETURN);
            }
            result.ajouterElement(org.univlille1.pathimpact.stacktrace.Evenement.END_OF_PROGRAM);
        }
    }
}

