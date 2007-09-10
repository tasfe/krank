package org.crank.core;

public class ObjectNotFound extends RuntimeException {
	public ObjectNotFound (String name, Exception ex) {
		super("Object not found " + name, ex);
	}
}
