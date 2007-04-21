package org.crank.crud.criteria.test;

import static org.crank.crud.criteria.Comparison.*;
import static org.crank.crud.criteria.Group.*;
import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.Operator;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;


public class GroupTest {
	
	@Test
	public void testPrime () {
		Group group = and(eq("firstName",  "Rick"), eq("lastName", "Hightower"));
		assertEquals("(AND [firstName_EQ_Rick, lastName_EQ_Hightower])", group.toString());
	}

	@Test
	public void nestedPrime () {
		Group group = and(eq("firstName", "Rick"), eq("lastName", "Hightower"), or(eq("foo", "bar"), eq("baz", "foo")));
		assertEquals("(AND [firstName_EQ_Rick, lastName_EQ_Hightower, (OR [foo_EQ_bar, baz_EQ_foo])])", group.toString());
	}
	
	@Test
	public void test1 () {
		Group group = and().add("firstName", Operator.EQ, "Rick")
					.add("lastName", Operator.EQ, "Hightower");
		assertEquals("(AND [firstName_EQ_Rick, lastName_EQ_Hightower])", group.toString());
	}
	@Test
	public void test2 () {
		Group group = and().eq("firstName", "Rick")
					.eq("lastName", "Hightower");
		assertEquals("(AND [firstName_EQ_Rick, lastName_EQ_Hightower])", group.toString());
	}
	@Test
	public void nested () {
		Group group = and()
					.eq("firstName", "Rick")
					.eq("lastName", "Hightower")
					.add(
						or().eq("foo", "bar").eq("baz", "foo")
					);
		assertEquals("(AND [firstName_EQ_Rick, lastName_EQ_Hightower, (OR [foo_EQ_bar, baz_EQ_foo])])", group.toString());
	}

}
