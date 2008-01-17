package org.crank.crud.join;

public class Join {

	public Join () {
	}


	public static Join joinFetch (String property) {
		return new RelationshipFetch(JoinType.RIGHT, property);
	}
	public static Join joinFetch (String property, boolean aliasedRelationship) {
		return new RelationshipFetch(JoinType.RIGHT, property, aliasedRelationship);
	}
	public static Join joinFetch (String property, String alias) {
		return new RelationshipFetch(JoinType.RIGHT, property, alias);
	}
	public static Join joinFetch (String property, boolean aliasedRelationship, String alias) {
		return new RelationshipFetch(JoinType.RIGHT, property, aliasedRelationship, alias);
	}
	public static Join leftJoinFetch (String property) {
		return new RelationshipFetch(JoinType.LEFT, property);
	}
	public static Join leftJoinFetch (String property, boolean aliasedRelationship) {
		return new RelationshipFetch(JoinType.LEFT, property, aliasedRelationship);
	}
	public static Join leftJoinFetch (String property, boolean aliasedRelationship, String alias) {
		return new RelationshipFetch(JoinType.LEFT, property, aliasedRelationship, alias);
	}
	public static Join[] join (Join... fetchList) {
		return fetchList;
	}
	public static Join[] fetch (Join... fetchList) {
		return fetchList;
	}	
}
