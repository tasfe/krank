package org.crank.crud.controller;

import junit.framework.TestCase;
import org.crank.crud.controller.datasource.FilteringPagingDataSource;
import org.crank.crud.criteria.Comparison;
import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.OrderBy;

import javax.persistence.Embeddable;
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

		public List list(int startItem, int numItems) {
			return null;
		}

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

		
	};
	
	protected void setUp() throws Exception {
		
	}

	public void testOneToOne() {
		paginator = new FilteringPaginator(dataSource, A.class);
	}

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

		@OneToOne
		private B b;

		public B getB() {
			return b;
		}

		public void setB(B b) {
			this.b = b;
		}

	}

	@SuppressWarnings({"JpaModelErrorInspection"})
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
