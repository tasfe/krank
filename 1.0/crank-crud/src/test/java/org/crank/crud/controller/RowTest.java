package org.crank.crud.controller;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import junit.framework.TestCase;

class Foo {
	Bar bar = new Bar();

	public Bar getBar() {
		return bar;
	}

	public void setBar(Bar bar) {
		this.bar = bar;
	}
}
class Bar {
	String baz = "baz";

	public String getBaz() {
		return baz;
	}

	public void setBaz(String baz) {
		this.baz = baz;
	}
}
public class RowTest extends TestCase {
	public Row row;
	
	@Override
	@BeforeMethod
	protected void setUp() throws Exception {
		row = new Row(new Foo());
	}

	@Test
	public void testGet() {
		assertEquals("baz", row.get("bar.baz"));
		assertEquals("baz", row.get("object.bar.baz"));
		assertNull(row.get("somepath.that.does.not.exist"));
		row.put("somepath.that.does.not.exist", "foo");
		assertEquals("foo", row.get("somepath.that.does.not.exist"));
	}

}
