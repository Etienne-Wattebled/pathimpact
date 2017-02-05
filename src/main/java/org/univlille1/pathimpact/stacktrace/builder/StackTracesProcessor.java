package org.univlille1.pathimpact.stacktrace.builder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.univlille1.pathimpact.element.ElementItf;
import org.univlille1.pathimpact.element.Evenement;
import org.univlille1.pathimpact.element.Methode;
import org.univlille1.pathimpact.filter.CtInvocationFilter;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtArrayTypeReference;
import spoon.reflect.reference.CtTypeReference;

public class StackTracesProcessor extends AbstractProcessor<CtMethod<?>> {

	private Map<CtMethod<?>, List<CtMethod<?>>> linksBetweenMethods;

	private List<CtMethod<?>> mainMethods;

	private List<ElementItf> stackTrace;

	private Map<String, Methode> mapMethodes;

	public StackTracesProcessor() {
		linksBetweenMethods = new HashMap<CtMethod<?>, List<CtMethod<?>>>();
		mainMethods = new LinkedList<CtMethod<?>>();
		stackTrace = new LinkedList<ElementItf>();
		mapMethodes = new HashMap<String, Methode>();
	}

	@Override
	public boolean isToBeProcessed(CtMethod<?> candidate) {
		if (candidate.getBody() == null) {
			return false;
		}
		return true;
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

		List<CtInvocation<?>> invocations = method.getBody().getElements(new CtInvocationFilter());
		for (CtInvocation<?> invoc : invocations) {
			CtExecutable<?> c = invoc.getExecutable().getDeclaration();
			if (c instanceof CtMethod<?>) {
				CtMethod<?> m = (CtMethod<?>) c;
				List<CtMethod<?>> l = null;
				if (linksBetweenMethods.containsKey(method)) {
					l = linksBetweenMethods.get(method);
				} else {
					l = new LinkedList<CtMethod<?>>();
					linksBetweenMethods.put(method, l);
				}
				l.add(m);
			}
		}
	}

	@Override
	public void processingDone() {
		if (mainMethods.size() == 1) {
			CtMethod<?> method = mainMethods.get(0);
			LinkedList<ElementItf> path = new LinkedList<ElementItf>();
			mapMethodes.put(method.getReference().toString(),new Methode(method.getReference().toString()));
			path.add(mapMethodes.get(method.getReference().toString()));
			construireStackTrace(method, path);
		} else {
			System.err.println("Erreur: Il ne doit y avoir qu'une seule méthode main.");
			System.exit(0);
		}
	}

	private void construireStackTrace(CtMethod<?> method, LinkedList<ElementItf> path) {
		List<CtMethod<?>> methods = linksBetweenMethods.get(method);
		if (methods != null && !methods.isEmpty()) {
			for (CtMethod<?> m : methods) {
				String ref = m.getReference().toString();
				Methode methode = mapMethodes.get(ref);
				if (methode == null) {
					methode = new Methode(ref);
					mapMethodes.put(ref, methode);
				}
				boolean boucle = path.contains(methode);
				
				path.add(methode);
				if (boucle) {
					// Boucle détectée, on stoppe la stacktrace
					finStacktrace(path);
				} else {
					construireStackTrace(m, path);
				}
				path.removeLast();
			}
		} else {
			// Il s'agit d'une feuille, on stoppe la stacktrace
			finStacktrace(path);
		}
	}

	private void finStacktrace(LinkedList<ElementItf> path) {
		stackTrace.addAll(path);
		int nb = path.size();
		for (int i = 1; i <= nb; i++) {
			stackTrace.add(Evenement.RETURN);
		}
		stackTrace.add(Evenement.END_OF_PROGRAM);
	}

	public List<ElementItf> getStackTrace() {
		return stackTrace;
	}
}
