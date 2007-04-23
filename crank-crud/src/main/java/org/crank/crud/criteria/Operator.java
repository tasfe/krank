package org.crank.crud.criteria;

import java.io.Serializable;

public enum Operator implements Serializable{
	EQ, NE, LE, GE, GT, LT, BETWEEN, LIKE, LIKE_START, LIKE_END, LIKE_CONTAINS;
	private static String [] operators  = 
	{"=", "<>", "<=", ">=", ">", "<", "between", "like", "like", "like", "like"};
	
	public String getOperator () {
		return operators [this.ordinal()]	;
	}
}
