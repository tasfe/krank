package org.crank.crud.controller;

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
	protected void setUp() throws Exception {
		row = new Row(new Foo());
	}

	public void testGet() {
		assertEquals("baz", row.get("bar.baz"));
		assertEquals("baz", row.get("object.bar.baz"));
	}

}
