package org.univlille1.pathimpact.stacktrace;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.univlille1.pathimpact.CtInvocationFilter;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtArrayTypeReference;
import spoon.reflect.reference.CtTypeReference;

public class StackTracesProcessor extends AbstractProcessor<CtMethod<?>> {
	
	private Map<String,CtMethod<?>> getCtMethod;
	private Map<CtMethod<?>,String> getMethodKey;
	
	private Map<CtMethod<?>,List<CtMethod<?>>> linksBetweenMethods;
	
	private List<CtMethod<?>> mainMethods;

	public StackTracesProcessor() {
		linksBetweenMethods = new HashMap<CtMethod<?>,List<CtMethod<?>>>();
		mainMethods = new LinkedList<CtMethod<?>>();
		getCtMethod = new HashMap<String,CtMethod<?>>();
		getMethodKey = new HashMap<CtMethod<?>,String>();
	}

	@Override
	public void process(CtMethod<?> method) {
		if (method.getSimpleName().equals("main") && method.hasModifier(ModifierKind.STATIC)) {
			List<CtParameter<?>> parameters = method.getParameters();
			if ((parameters != null) && (parameters.size() == 1)) {
				CtParameter<?> parameter = parameters.get(0);
				CtTypeReference<?> type = parameter.getType();
				if (type instanceof CtArrayTypeReference) {
					mainMethods.add(method);
				}
			}
		}
		CtClass<?> ctClass = method.getParent(CtClass.class);
		StringBuilder key = new StringBuilder();
		key.append(ctClass.getSimpleName());
		key.append("#");
		key.append(method.getSimpleName());
		key.append("(");
		List<CtParameter<?>> parameters = method.getParameters();
		Iterator<CtParameter<?>> it = parameters.listIterator();
		
		if (it.hasNext()) {
			CtParameter<?> p = it.next();
			key.append(p.getType().toString());
			while(it.hasNext()) {
				key.append(", ");
				p = it.next();
				key.append(p.getType().toString());
			}
		}
		
		key.append(")");
		String methodKey = key.toString();
		getCtMethod.put(methodKey,method);
		getMethodKey.put(method,methodKey);
	}
	
	@Override
	public void processingDone() {
		Collection<CtMethod<?>> methodes = getCtMethod.values();
		for (CtMethod<?> m : methodes) {
			List<CtInvocation<?>> invocations = m.getBody().getElements(new CtInvocationFilter());
			for (CtInvocation<?> i : invocations) {
				List<CtMethod<?>> l = null;
				if (linksBetweenMethods.containsKey(m)) {
					l = linksBetweenMethods.get(m);
				} else {
					l = new LinkedList<CtMethod<?>>();
					linksBetweenMethods.put(m,l);
				}
				l.add(getCtMethod.get(i.getExecutable().toString()));
			}
		}
	}
	
	public StackTrace construireStackTrace() {
		if (mainMethods.size() == 1) {
			CtMethod<?> method = mainMethods.get(0);
			StackTrace result = new StackTrace();
			StackTrace path = new StackTrace();
			path.ajouterElement(new Methode(getMethodKey.get(method)));
			construireStackTrace(result, method, path);
			return result;
		} else {
			System.err.println("Erreur: Il ne doit y avoir qu'une seule méthode main.");
			System.exit(0);
			return null;
		}
	}
	private void construireStackTrace(StackTrace result, CtMethod<?> method, StackTrace path) {
		List<CtMethod<?>> methods = linksBetweenMethods.get(method);
		if (methods != null && !methods.isEmpty()) {
			for (CtMethod<?> m : methods) {
				path.ajouterElement(new Methode(getMethodKey.get(m)));
				construireStackTrace(result,m,path);
				path.supprimerDernierElement();
			}
		} else {
			result.ajouterElements(path.getElements());
			int nb = path.size();
			for (int i =1;i<=nb;i++) {
				result.ajouterElement(Evenement.RETURN);
			}
			result.ajouterElement(Evenement.END_OF_PROGRAM);
		}
	}
}
