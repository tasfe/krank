package org.crank.crud.controller;

import org.testng.annotations.Test;

public class AutoCompleteControllerTest {
//	@Test
//	public void testConstructorArgs() throws Exception {
//		final boolean[] groupMethodCalls = new boolean[5];
//		Group testGroup = new Group() {
//			@Override
//			public void clear() {
//				groupMethodCalls[0] = true;
//				super.clear();
//			}
//			
//		};
//		List<TestSourceSupport> testData = Arrays.asList(
//				new TestSourceSupport[] {
//					
//				});
//		FilteringDataSource<TestSourceSupport> dataSource = createMock(FilteringDataSource.class);
//		expect(dataSource.group()).andReturn(testGroup); // call to clear
//		expect(dataSource.group()).andReturn(testGroup); // call to add
//		dataSource.setOrderBy(
//				aryEq(
//						new OrderBy[] { new OrderBy("prop1", OrderDirection.ASC)}
//						) 
//				); // call to clear
//		expect(dataSource.list()).andReturn(testData);
//
//		replay(dataSource);
//		CrudOperations<TestDestSupport> dataTarget = createMock(CrudOperations.class);
//		AutoCompleteController<TestSourceSupport, TestDestSupport> autoCompleteController = new AutoCompleteController<TestSourceSupport, TestDestSupport>(
//				"prop1",
//				dataSource,
//				"prop2",
//				dataTarget
//				);
//		Field sourcePropertyNameField = autoCompleteController.getClass().getDeclaredField("propertyName");
//		Field targetPropertyNameField = autoCompleteController.getClass().getDeclaredField("fieldName");
//		Field dataSourceField = autoCompleteController.getClass().getDeclaredField("dataSource");
//		Field controllerField = autoCompleteController.getClass().getDeclaredField("crudController");
//		checkFieldEquality(sourcePropertyNameField, autoCompleteController, "prop1");
//		checkFieldEquality(targetPropertyNameField, autoCompleteController, "prop2");
//		checkFieldEquality(dataSourceField, autoCompleteController, dataSource);
//		checkFieldEquality(controllerField, autoCompleteController, dataTarget);
//		assertEquals(dataSource, autoCompleteController.getDataSource());
//		assertEquals(dataSource, autoCompleteController.getDataSource());
//		
//		dataSource = createMock(FilteringDataSource.class);
//		autoCompleteController.setDataSource(dataSource);
//		assertEquals(dataSource, autoCompleteController.getDataSource());
//	}
//	
//	@Test
//	public void testTextChanged() throws Exception {
//		final boolean[] groupMethodCalls = new boolean[5];
//		Group testGroup = new Group() {
//			@Override
//			public void clear() {
//				groupMethodCalls[0] = true;
//				super.clear();
//			}
//			
//		};
//		List<TestSourceSupport> testDataEmpty = new ArrayList<TestSourceSupport>();
//		FilteringDataSource<TestSourceSupport> dataSource = createMock(FilteringDataSource.class);
//		expect(dataSource.group()).andReturn(testGroup); // call to clear
//		expect(dataSource.group()).andReturn(testGroup); // call to add
//		dataSource.setOrderBy(
//				aryEq(
//						new OrderBy[] { new OrderBy("prop1", OrderDirection.ASC)}
//						) 
//				); // call to clear
//		expect(dataSource.list()).andReturn(testDataEmpty);
//		
//		List<TestSourceSupport> testData = new ArrayList<TestSourceSupport>();
//		testData.add(new TestSourceSupport(99L, "test"));
//		expect(dataSource.group()).andReturn(testGroup); // call to clear
//		expect(dataSource.group()).andReturn(testGroup); // call to add
//		dataSource.setOrderBy(
//				aryEq(
//						new OrderBy[] { new OrderBy("prop1", OrderDirection.ASC)}
//						) 
//				); // call to clear
//		expect(dataSource.list()).andReturn(testData);
//		
//
//		CrudOperations<TestDestSupport> dataTarget = createMock(CrudOperations.class);
//		AutoCompleteController<TestSourceSupport, TestDestSupport> autoCompleteController = new AutoCompleteController<TestSourceSupport, TestDestSupport>(
//				"prop1",
//				dataSource,
//				"prop2",
//				dataTarget
//				);
//		Method textChangedMethod = autoCompleteController.getClass().getDeclaredMethod("textChanged", new Class[] {String.class});
//		textChangedMethod.setAccessible(true);
//		SelectListener mockSelectListener = createMock(SelectListener.class);
//		mockSelectListener.unselect(eqEvent(new SelectEvent(autoCompleteController, null)));
//		mockSelectListener.select(eqEvent(new SelectEvent(autoCompleteController, testData.get(0))));
//
//		replay(dataSource);
//		replay(mockSelectListener);
//		
//		autoCompleteController.addSelectListener(mockSelectListener);
//		textChangedMethod.invoke(autoCompleteController, new Object[] {"nomatch"});
//		assertFalse(autoCompleteController.isFound());
//		
//		textChangedMethod.invoke(autoCompleteController, new Object[] {"test"});
//		assertTrue(autoCompleteController.isFound());
//		autoCompleteController.removeSelectListener(mockSelectListener);
//		verify(mockSelectListener);
//		verify(dataSource);
//	}
//
//	
//	public static SelectEvent eqEvent(SelectEvent se) {
//	    reportMatcher(new SelectEventMatcher(se));
//	    return null;		
//	}
//
//	static class SelectEventMatcher implements IArgumentMatcher {
//		SelectEvent se1 = null;
//		
//		SelectEventMatcher(SelectEvent se1) {
//			this.se1 = se1;
//		}
//		
//		public boolean matches(Object arg0) {
//			SelectEvent se2 = (SelectEvent)arg0;
//			if (se1.getSource() == null) {
//					if (se2.getSource() != null) {
//						return false;
//					}
//			}
//			else if (!se1.getSource().equals(se2.getSource())) {
//				return false;
//			}
//			if (se1.getValue() == null) {
//				if (se2.getValue() != null) {
//					return false;
//				}
//			}
//			else if (!se1.getValue().equals(se2.getValue())) {
//				return false;
//			}
//			return true;
//		}
//		public void appendTo(StringBuffer buffer) {
//	        buffer.append("eqEvent(");
//	        buffer.append(se1.getClass().getName());
//	        buffer.append(" with source \"");
//	        buffer.append(se1.getSource());
//	        buffer.append(" with value \"");
//	        buffer.append(se1.getValue());
//	        buffer.append("\")");		
//		}
//	
//	};
//	
//	@Test
//	public void testSimpleAccessors() throws Exception {
//		AutoCompleteController<TestSourceSupport, TestDestSupport> autoCompleteController = new AutoCompleteController<TestSourceSupport, TestDestSupport>();
//		autoCompleteController.setFound(false);
//		assertEquals(false, autoCompleteController.isFound());
//		autoCompleteController.setFound(true);
//		assertEquals(true, autoCompleteController.isFound());
//		autoCompleteController.setValue("some value");
//		assertEquals("some value", autoCompleteController.getValue());
//		
//		FilteringDataSource dataSource = createMock(FilteringDataSource.class);
//		autoCompleteController.setDataSource(dataSource);
//		assertEquals(dataSource, autoCompleteController.getDataSource());
//		
//	}	
//	
//	private void checkFieldEquality(Field f, Object o, Object comparisonValue) throws IllegalArgumentException, IllegalAccessException {
//		f.setAccessible(true);
//		assertEquals(comparisonValue, f.get(o));
//	}
//	
//	@Test void testCrudOperations1() {
//		FilteringDataSource<TestSourceSupport> mockDataSource = createNiceMock(FilteringDataSource.class);
//		final CrudControllerListener[] holder = new CrudControllerListener[1];
//		CrudController<TestDestSupport, Long> mockController = new CrudController<TestDestSupport, Long>() {
//			@Override
//			public void addCrudControllerListener(
//					CrudControllerListener listener) {
//				holder[0] = listener;
//			}
//		};
//		AutoCompleteController<TestSourceSupport, TestDestSupport> autoCompleteController = new AutoCompleteController<TestSourceSupport, TestDestSupport>(
//				"prop1",
//				mockDataSource,
//				"prop2",
//				mockController
//				);
//		CrudControllerListener autoListener = holder[0];
//		autoCompleteController.setValue("something not null");
//		autoListener.afterLoadCreate(null);
//		assertNull(autoCompleteController.getValue());
//		
//		TestSourceSupport tss1 = new TestSourceSupport(1L, "test1");
//		autoListener.afterCreate(new CrudEvent(mockController, new TestDestSupport(1L, tss1)));
//		assertEquals("test1", autoCompleteController.getValue());
//		
//		TestSourceSupport tss2 = new TestSourceSupport(1L, "test2");
//		autoListener.afterRead(new CrudEvent(mockController, new TestDestSupport(1L, tss2)));
//		assertEquals("test2", autoCompleteController.getValue());
//
//	}
//
//	@Test 
//	public void testCrudOperations2() {
//
//		List<TestSourceSupport> emptyList = Collections.emptyList();
//		FilteringDataSource<TestSourceSupport> mockDataSource = createNiceMock(FilteringDataSource.class);
//		Group testGroup = new Group();
//		expect(mockDataSource.group()).andReturn(testGroup); // call to clear
//		expect(mockDataSource.group()).andReturn(testGroup); // call to clear
//		expect(mockDataSource.group()).andReturn(testGroup); // call to clear
//		expect(mockDataSource.group()).andReturn(testGroup); // call to clear
//		expect(mockDataSource.group()).andReturn(testGroup); // call to clear
//		expect(mockDataSource.group()).andReturn(testGroup); // call to clear
//		expect(mockDataSource.group()).andReturn(testGroup); // call to clear
//		expect(mockDataSource.group()).andReturn(testGroup); // call to clear
//		expect(mockDataSource.list()).andReturn(emptyList);
//		List<TestSourceSupport> multiList = Arrays.asList(new TestSourceSupport[] {
//				new TestSourceSupport(1L, "test-01"),
//				new TestSourceSupport(2L, "test-02")
//		});
//		
//		
//		expect(mockDataSource.list()).andReturn(multiList);
//		List<TestSourceSupport> uniList = Arrays.asList(new TestSourceSupport[] {
//				new TestSourceSupport(1L, "test-01"),
//		});
//		expect(mockDataSource.list()).andReturn(uniList);
//		expect(mockDataSource.list()).andReturn(uniList);
//		replay(mockDataSource);
//		
//		
//		final CrudControllerListener[] holder = new CrudControllerListener[1];
//		CrudController<TestDestSupport, Long> mockController = new CrudController<TestDestSupport, Long>() {
//			@Override
//			public void addCrudControllerListener(
//					CrudControllerListener listener) {
//				holder[0] = listener;
//			}
//		};
//		AutoCompleteController<TestSourceSupport, TestDestSupport> autoCompleteController = new AutoCompleteController<TestSourceSupport, TestDestSupport>(
//				"prop1",
//				mockDataSource,
//				"prop2",
//				mockController
//				);
//		CrudControllerListener autoListener = holder[0];
//
//		{
//			autoCompleteController.setValue(null);
//			TestSourceSupport tss1 = new TestSourceSupport(1L, "no data");
//			autoListener.beforeCreate(new CrudEvent(mockController, new TestDestSupport(1L, tss1)));
//		}
//
//		try {
//			autoCompleteController.setValue("a new value");
//			TestSourceSupport tss1 = new TestSourceSupport(1L, "no data");
//			autoListener.beforeCreate(new CrudEvent(mockController, new TestDestSupport(1L, tss1)));
//			assertFalse(true);
//		} catch (IllegalArgumentException e) {
//			// expected
//		}
//
//		try {
//			autoCompleteController.setValue("a new value");
//			TestSourceSupport tss1 = new TestSourceSupport(1L, "multiple entities returned");
//			autoListener.beforeCreate(new CrudEvent(mockController, new TestDestSupport(1L, tss1)));
//			assertFalse(true);
//		} catch (IllegalArgumentException e) {
//			// expected
//		}		
//		
//		autoCompleteController.setValue("a new value");
//		TestSourceSupport tss1 = new TestSourceSupport(1L, "test1");
//		TestDestSupport testDestSupport1 = new TestDestSupport(1L, tss1);
//		autoListener.beforeCreate(new CrudEvent(mockController, testDestSupport1));
//		assertEquals(uniList.get(0).getProp1(), testDestSupport1.getProp2().getProp1());
//		
//		autoCompleteController.setValue("another new value");
//		TestSourceSupport tss2 = new TestSourceSupport(1L, "test2");
//		TestDestSupport testDestSupport2 = new TestDestSupport(1L, tss2);
//		autoListener.beforeUpdate(new CrudEvent(mockController, testDestSupport2));
//		assertEquals(uniList.get(0).getProp1(), testDestSupport2.getProp2().getProp1());
//	}
//	
//	
//	@Test
//	public void testAutocomplete() {
//		final boolean[] groupMethodCalls = new boolean[5];
//		Group testGroup = new Group() {
//			@Override
//			public void clear() {
//				groupMethodCalls[0] = true;
//				super.clear();
//			}
//			
//		};
//		List<TestSourceSupport> testData = Arrays.asList(
//				new TestSourceSupport[] {
//					new TestSourceSupport(1L, "test 1"),	
//					new TestSourceSupport(2L, "test 2"),	
//					new TestSourceSupport(3L, "test 3")
//				});
//		FilteringDataSource<TestSourceSupport> dataSource = createMock(FilteringDataSource.class);
//		expect(dataSource.group()).andReturn(testGroup); // call to clear
//		expect(dataSource.group()).andReturn(testGroup); // call to add
//		dataSource.setOrderBy(
//				aryEq(
//						new OrderBy[] { new OrderBy("prop1", OrderDirection.ASC)}
//						) 
//				); // call to clear
//		expect(dataSource.list()).andReturn(testData);
//
//		replay(dataSource);
//		CrudOperations<TestDestSupport> dataTarget = createMock(CrudOperations.class);
//		AutoCompleteController<TestSourceSupport, TestDestSupport> autoCompleteController = new AutoCompleteController<TestSourceSupport, TestDestSupport>(
//				"prop1",
//				dataSource,
//				"prop2",
//				dataTarget
//				);
//		autoCompleteController.autocomplete("test");
//		verify(dataSource);
//		
//		
//	}
//	
//	@Test
//	public void testAutocompleteNoMatches() {
//		final boolean[] groupMethodCalls = new boolean[5];
//		Group testGroup = new Group() {
//			@Override
//			public void clear() {
//				groupMethodCalls[0] = true;
//				super.clear();
//			}
//			
//		};
//		List<TestSourceSupport> testData = Arrays.asList(
//				new TestSourceSupport[] {
//					
//				});
//		FilteringDataSource<TestSourceSupport> dataSource = createMock(FilteringDataSource.class);
//		expect(dataSource.group()).andReturn(testGroup); // call to clear
//		expect(dataSource.group()).andReturn(testGroup); // call to add
//		dataSource.setOrderBy(
//				aryEq(
//						new OrderBy[] { new OrderBy("prop1", OrderDirection.ASC)}
//						) 
//				); // call to clear
//		expect(dataSource.list()).andReturn(testData);
//
//		replay(dataSource);
//		CrudOperations<TestDestSupport> dataTarget = createMock(CrudOperations.class);
//		AutoCompleteController<TestSourceSupport, TestDestSupport> autoCompleteController = new AutoCompleteController<TestSourceSupport, TestDestSupport>(
//				"prop1",
//				dataSource,
//				"prop2",
//				dataTarget
//				);
//		autoCompleteController.autocomplete("test");
//		verify(dataSource);
//		
//		
//	}	
//	
//	static class TestSourceSupport implements java.io.Serializable {
//		private Long id;
//		private String prop1;
//
//		public TestSourceSupport(Long id, String prop1) {
//			this.id = id;
//			this.prop1 = prop1;
//		}
//		
//		public String getProp1() {
//			return prop1;
//		}
//
//		public void setProp1(String prop1) {
//			this.prop1 = prop1;
//		}
//
//		public Long getId() {
//			return id;
//		}
//
//		public void setId(Long id) {
//			this.id = id;
//		}
//	}
//	static class TestDestSupport implements java.io.Serializable {
//		private Long id;
//		private TestSourceSupport prop2;
//
//		public TestDestSupport(Long id, TestSourceSupport prop2) {
//			this.id = id;
//			this.prop2 = prop2;
//		}
//		
//		public TestSourceSupport getProp2() {
//			return prop2;
//		}
//
//		public void setProp2(TestSourceSupport prop2) {
//			this.prop2 = prop2;
//		}
//
//		public Long getId() {
//			return id;
//		}
//
//		public void setId(Long id) {
//			this.id = id;
//		}
//	}
	
	@Test
	public void test() {
		
	}
}
