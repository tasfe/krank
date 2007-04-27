package org.crank.crud.criteria;


public class VerifiedGroup extends Group {

	private Class baseType;
	
	public VerifiedGroup () {
	}


	public static Group and (Class clazz) {
		VerifiedGroup group =  new VerifiedGroup(); 
		group.junction = Junction.AND;
		group.baseType = clazz;
		return group;
	}
	

	public VerifiedGroup (final Class aBaseType) {
		this.baseType = aBaseType;
	}


	public Class getBaseType() {
		return baseType;
	}


	public void setBaseType(Class baseType) {
		this.baseType = baseType;
	}


}
