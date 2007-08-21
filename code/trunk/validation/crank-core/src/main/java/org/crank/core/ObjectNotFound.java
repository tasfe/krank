package org.crank.core;

public class ObjectNotFound extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ObjectNotFound (String name, Exception ex) {
		super("Object not found " + name, ex);
	}
}
