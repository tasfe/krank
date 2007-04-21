package org.crank.crud.criteria;

import java.io.Serializable;

public enum Operator implements Serializable{
	EQ, NE, LE, GE, GT, LT;
	private static String [] operators  = 
	{"=", "<>", "<=", ">=", ">", "<"};
	
	public String getOperator () {
		return operators [this.ordinal()]	;
	}
}
