package org.crank.crud.criteria;

import java.util.Arrays;

public class Comparison extends Criterion {
	private String name;
	private Operator operator;
	private Object value;
	private boolean alias = false;
	
	public static Comparison between (final String name, final Object value1, final Object value2 ) {
		return new Between (name, value1, value2);
	}
	public static Comparison between (final String name, final boolean alias, final Object value1, final Object value2){
		return new Between (name, value1, value2, alias);
	}
	public static Comparison in (final String name, final Object value ) {
		return new Comparison (name, Operator.IN, value);
	}
	public static Comparison in (final String name, final boolean alias, final Object value) {
		return new Comparison (name, Operator.IN, value, alias);
	}
	public static Comparison in (final String name, final Object... value ) {
		return new Comparison (name, Operator.IN, Arrays.asList(value));
	}
	public static Comparison in (final String name, final boolean alias, final Object... value ) {
		return new Comparison (name, Operator.IN, Arrays.asList(value), alias);
	}
	public static Comparison eq (final String name, final Object value ) {
		return new Comparison (name, Operator.EQ, value);
	}
	public static Comparison eq (final String name, final boolean alias, final Object value ) {
		return new Comparison (name, Operator.EQ, value, alias);
	}
	public static Comparison ne (final String name, final Object value ) {
		return new Comparison (name, Operator.NE, value);
	}
	public static Comparison ne (final String name, final boolean alias, final Object value ) {
		return new Comparison (name, Operator.NE, value, alias);
	}
	public static Comparison gt (final String name, final Object value ) {
		return new Comparison (name, Operator.GT, value);
	}
	public static Comparison gt (final String name, final boolean alias, final Object value ) {
		return new Comparison (name, Operator.GT, value, alias);
	}
	public static Comparison lt (final String name, final Object value ) {
		return new Comparison (name, Operator.LT, value);
	}
	public static Comparison lt (final String name, final boolean alias, final Object value ) {
		return new Comparison (name, Operator.LT, value, alias);
	}
	public static Comparison ge (final String name, final Object value ) {
		return new Comparison (name, Operator.GE, value);
	}
	public static Comparison ge (final String name, final boolean alias, final Object value ) {
		return new Comparison (name, Operator.GE, value, alias);
	}
	public static Comparison le (final String name, final Object value ) {
		return new Comparison (name, Operator.LE, value);
	}
	public static Comparison le (final String name, final boolean alias, final Object value ) {
		return new Comparison (name, Operator.LE, value, alias);
	}
	public static Comparison like (final String name, final String value ) {
		return new Comparison (name, Operator.LIKE, value);
	}
	public static Comparison like (final String name, final boolean alias, final String value ) {
		return new Comparison (name, Operator.LIKE, value, alias);
	}
	public static Comparison startsLike (final String name, final String value ) {
		return new Comparison (name, Operator.LIKE_START, value);
	}
	public static Comparison startsLike (final String name, final boolean alias, final String value ) {
		return new Comparison (name, Operator.LIKE_START, value, alias);
	}
	public static Comparison endsLike (final String name, final String value ) {
		return new Comparison (name, Operator.LIKE_END, value);
	}
	public static Comparison endsLike (final String name, final boolean alias, final String value ) {
		return new Comparison (name, Operator.LIKE_END, value, alias);
	}
	public static Comparison containsLike (final String name, final String value ) {
		return new Comparison (name, Operator.LIKE_CONTAINS, value);
	}
	public static Comparison containsLike (final String name, final boolean alias, final String value ) {
		return new Comparison (name, Operator.LIKE_CONTAINS, value, alias);
	}

	public Comparison () {
		
	}
	
	public Comparison (final String aName, final Operator aOperator, final Object aValue) {
		this.name = aName;
		this.operator = aOperator;
		this.value = aValue;
	}
	
	public Comparison (final String aName, final Operator aOperator, final Object aValue, final boolean alias) {
		this.name = aName;
		this.operator = aOperator;
		this.value = aValue;
		this.alias = alias;
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
	
	public boolean isAlias(){
		return this.alias;
	}
	
	public String toString () {
		return name + "_" + operator + "_" + value;
	}
}
