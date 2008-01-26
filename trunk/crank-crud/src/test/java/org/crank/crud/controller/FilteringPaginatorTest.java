package org.crank.crud.controller;

import junit.framework.TestCase;
import org.crank.crud.controller.datasource.FilteringPagingDataSource;
import org.crank.crud.criteria.Comparison;
import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.join.Join;
import org.testng.annotations.Test;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.List;

@SuppressWarnings({"JpaModelErrorInspection"})
public class FilteringPaginatorTest extends TestCase {
	private FilteringPaginator paginator;
	private FilteringPagingDataSource dataSource = new FilteringPagingDataSource() {

        private Group group = new Group();

        public int getCount() {
			return 0;
		}

		@SuppressWarnings("unchecked")
		public List list(int startItem, int numItems) {
			return null;
		}

		@SuppressWarnings("unchecked")
		public List list() {
			return null;
		}

		public Group group() {
			return group;
		}

		public OrderBy[] orderBy() {
            return new OrderBy[]{};
        }

		public void setOrderBy(OrderBy[] orderBy) {
		}

        public Join[] fetches() {
            return new Join[0];  //To change body of implemented methods use File | Settings | File Templates.
        }

        public void setFetches(Join[] fetches) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

		public Join[] joins() {
			// TODO Auto-generated method stub
			return null;
		}

		public void setJoins(Join[] fetches) {
			// TODO Auto-generated method stub
			
		}


    };
	
	protected void setUp() throws Exception {
		
	}

	@Test
	public void testOneToOne() {
		paginator = new FilteringPaginator(dataSource, A.class);
		assertNotNull(paginator.getFilterableProperties().get("b.c.name"));
		
	}

	@Test
    public void testClearCriteria() {
        paginator = new FilteringPaginator(dataSource, A.class);
        paginator.addCriterion(Comparison.eq("name", "foo"));
        paginator.getCriteria().clear();
        paginator.filter();
        assertEquals(0, paginator.getCriteria().size());
        assertEquals("(AND [])", dataSource.group().toString());

        paginator.addCriterion(Comparison.eq("name", "foo"));
        paginator.filter();
        assertEquals(1, paginator.getCriteria().size());
        assertEquals("(AND [name_EQ_foo])", dataSource.group().toString());


    }

    protected void tearDown() throws Exception {
		super.tearDown();
	}

	@SuppressWarnings({"JpaModelErrorInspection"})
    @Embeddable
	class A {

		private String name;
		
		@ManyToOne
		private A aparent;
		

		@OneToOne
		private B b;

		public B getB() {
			return b;
		}

		public void setB(B b) {
			this.b = b;
		}


		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public A getAparent() {
			return aparent;
		}

		public void setAparent(A parent) {
			this.aparent = parent;
		}

	}

	@SuppressWarnings({"JpaModelErrorInspection"})
    @Embeddable
	class C {
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	
	@SuppressWarnings({"JpaModelErrorInspection"})
    @Embeddable
	class B {
		
		private String name;
		
		@ManyToOne( )
		private C c;
		
		public C getC() {
			return c;
		}

		public void setC(C c) {
			this.c = c;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}

}
