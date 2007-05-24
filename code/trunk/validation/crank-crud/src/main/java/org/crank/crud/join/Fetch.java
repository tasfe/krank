package org.crank.crud.join;

public class Fetch {
	private String relationshipProperty;
	private Join join;
	private String alias = "";

	public Fetch () {
	}

	public Fetch (final Join aJoin) {
		this.join = aJoin;
	}

	public Fetch (final Join aJoin, final String relationship) {
		this.relationshipProperty = relationship;
		this.join = aJoin;
	}
	
	public Fetch (final Join aJoin, final String relationship, String alias) {
		this.alias = alias;
		this.relationshipProperty = relationship;
		this.join = aJoin;
	}

	public static Fetch joinFetch (String property) {
		return new Fetch(Join.RIGHT, property);
	}
	public static Fetch joinFetch (String property, String alias) {
		return new Fetch(Join.RIGHT, property, alias);
	}
	public static Fetch leftJoinFetch (String property) {
		return new Fetch(Join.LEFT, property);
	}
	public static Fetch leftJoinFetch (String property, String alias) {
		return new Fetch(Join.LEFT, property, alias);
	}
	public static Fetch[] join (Fetch... fetchList) {
		return fetchList;
	}
	public static Fetch[] fetch (Fetch... fetchList) {
		return fetchList;
	}

	public String getRelationshipProperty() {
		return relationshipProperty;
	}

	public void setRelationshipProperty(String alias) {
		this.relationshipProperty = alias;
	}

	public Join getJoin() {
		return join;
	}

	public void setJoin(Join join) {
		this.join = join;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public String getDefaultAlias(){
		return this.relationshipProperty.replaceAll(".","_");
	}
}
