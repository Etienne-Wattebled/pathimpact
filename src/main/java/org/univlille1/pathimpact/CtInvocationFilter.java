package org.univlille1.pathimpact;

import spoon.reflect.code.CtInvocation;
import spoon.reflect.visitor.Filter;

public class CtInvocationFilter implements Filter<CtInvocation<?>> {
	@Override
	public boolean matches(CtInvocation<?> arg0) {
		return true;
	}
}
