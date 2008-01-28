package org.crank.crud.controller;

import junit.framework.TestCase;
import org.crank.crud.controller.datasource.FilteringPagingDataSource;
import org.crank.crud.criteria.Comparison;
import org.crank.crud.criteria.Criterion;
import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.OrderBy;
import org.crank.crud.join.Join;
import org.hibernate.Criteria;
import org.testng.annotations.Test;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FilteringPaginatorTest extends TestCase {
	private FilteringPaginator paginator;
	private FilteringPagingDataSource dataSource = new FilteringPagingDataSource() {
		
		private Join[] joins;
        private Group group = new Group();
        private OrderBy[] orderBy;

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
            return this.orderBy;
        }

		public void setOrderBy(OrderBy[] orderBy) {
			this.orderBy = orderBy;
		}

        public Join[] fetches() {
            return null;
        }

        public void setFetches(Join[] fetches) {
            throw new RuntimeException("Don't use this... its deprecated. Fool!");
        }

		public Join[] joins() {
			return joins;
		}

		public void setJoins(Join[] joins) {
			this.joins = joins;
			
		}
    };
	
	protected void setUp() throws Exception {
		
	}

	@Test
	public void testEntityJoin() {
		paginator = new FilteringPaginator(dataSource, A.class);
		paginator.addFilterableEntityJoin(Z.class, "Z", "zalias", new String []{"name"}, "o");
		Map<String, FilterableProperty> filterableProperties = paginator.getFilterableProperties();
//		for (Map.Entry<String, FilterableProperty> entry : filterableProperties.entrySet()) {
//			System.out.println(entry.getKey());
//			System.out.println(entry.getValue().getComparison());
//			System.out.println(entry.getValue().getOrderBy());
//			System.out.println(entry.getValue().getParentType());
//			System.out.println(entry.getValue().getType());
//			System.out.println("---");
//		}
		
		FilterableProperty filterableProperty = filterableProperties.get("zalias.name");
		assertNotNull(filterableProperty);
		assertEquals("zalias.name", filterableProperty.getComparison().getName());
		
		filterableProperty.getComparison().setValue("Ric");
		filterableProperty.getComparison().enable();
		
		Join[] joins = dataSource.joins();
		assertTrue("We should have an entity join", joins.length > 0);
		assertEquals("EntityJoin name=Z alias=zalias", joins[0].toString());
	
		Group group = dataSource.group();
		Iterator<Criterion> iter = group.iterator(); iter.next();
		assertEquals("o_null_zalias", iter.next().toString());
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
	
	
	@SuppressWarnings({"JpaModelErrorInspection"})
    @Embeddable
	class Z {
		
		private String name;
		

		public void setName(String name) {
			this.name = name;
		}
		
	}

}
