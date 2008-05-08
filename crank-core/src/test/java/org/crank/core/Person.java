package org.crank.core;

import org.crank.annotations.ErrorHandler;

public class Person {
    @ErrorHandler
    long height;

    long weight;

    public long getHeight() {
		return height;
	}

	public void setHeight(long height) {
		this.height = height;
	}

    public long getWeight() {
        return weight;
    }

    @ErrorHandler
    public void setWeight(long weight) {
        this.weight = weight;
    }
}
