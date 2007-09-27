package org.crank.crud.controller;

import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.crank.crud.controller.datasource.FilteringPagingDataSource;
import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.OrderBy;

import junit.framework.TestCase;

public class FilteringPaginatorTest extends TestCase {
	private FilteringPaginator paginator;
	private FilteringPagingDataSource dataSource = new FilteringPagingDataSource() {

		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		public List list(int startItem, int numItems) {
			// TODO Auto-generated method stub
			return null;
		}

		public List list() {
			// TODO Auto-generated method stub
			return null;
		}

		public Group group() {
			// TODO Auto-generated method stub
			return null;
		}

		public OrderBy[] orderBy() {
			// TODO Auto-generated method stub
			return null;
		}

		public void setOrderBy(OrderBy[] orderBy) {
			// TODO Auto-generated method stub
			
		}

		
	};
	
	protected void setUp() throws Exception {
		
	}

	public void testOneToOne() {
		paginator = new FilteringPaginator(dataSource, A.class);
	}
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Embeddable
	class A {
		
		@OneToOne
		private B b;

		public B getB() {
			return b;
		}

		public void setB(B b) {
			this.b = b;
		}

	}
	
	@Embeddable
	class B {
		@OneToOne
		private A a;

		public A getA() {
			return a;
		}

		public void setA(A a) {
			this.a = a;
		}
		
	}

}
