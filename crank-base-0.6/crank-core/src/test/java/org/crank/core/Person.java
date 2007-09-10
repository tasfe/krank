package org.crank.core;

import org.crank.annotations.ErrorHandler;

public class Person {
    @ErrorHandler
    long height;

	public long getHeight() {
		return height;
	}

	public void setHeight(long height) {
		this.height = height;
	}

}
