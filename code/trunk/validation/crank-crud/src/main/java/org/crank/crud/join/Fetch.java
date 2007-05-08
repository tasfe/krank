package org.crank.crud.join;

public class Fetch {
	private String relationshipProperty;
	private Join join;

	public Fetch () {
	}

	public Fetch (final Join aJoin) {
		this.join = aJoin;
	}

	public Fetch (final Join aJoin, final String relationship) {
		this.relationshipProperty = relationship;
		this.join = aJoin;
	}

	public static Fetch joinFetch (String property) {
		return new Fetch(Join.RIGHT, property);
	}
	public static Fetch leftJoinFetch (String property) {
		return new Fetch(Join.LEFT, property);
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
}
