package org.crank.crud.criteria;

import java.io.Serializable;

public enum Operator implements Serializable{
	EQ, NE, LE, GE, GT, LT, BETWEEN, IN, LIKE, LIKE_START, LIKE_END, LIKE_CONTAINS, IS_NULL, IS_NOT_NULL;
	private static String [] operators  = 
	{"=", "<>", "<=", ">=", ">", "<", "between", "in", "like", "like", "like", "like", "is null", "is not null"};
	
	public String getOperator () {
		return operators [this.ordinal()]	;
	}
}
