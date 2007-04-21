package org.crank.crud.criteria;

public class Comparison extends Criterion {
	private String name;
	private Operator operator;
	private Object value;
	
	public static Comparison eq (final String name, final Object value ) {
		return new Comparison (name, Operator.EQ, value);
	}
	public static Comparison ne (final String name, final Object value ) {
		return new Comparison (name, Operator.NE, value);
	}
	public static Comparison gt (final String name, final Object value ) {
		return new Comparison (name, Operator.GT, value);
	}
	public static Comparison lt (final String name, final Object value ) {
		return new Comparison (name, Operator.LT, value);
	}
	public static Comparison ge (final String name, final Object value ) {
		return new Comparison (name, Operator.GE, value);
	}
	public static Comparison le (final String name, final Object value ) {
		return new Comparison (name, Operator.LE, value);
	}

	public Comparison () {
		
	}
	
	public Comparison (final String aName, final Operator aOperator, final Object aValue) {
		this.name = aName;
		this.operator = aOperator;
		this.value = aValue;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Operator getOperator() {
		return operator;
	}
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	public String toString () {
		return name + "_" + operator + "_" + value;
	}
}
