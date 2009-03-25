package org.crank.crud.controller;

import junit.framework.TestCase;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;


import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class SelectOneByIdControllerTest extends TestCase {
	List <Employee1> employee1List;
    List <Tag1> tag1List;
    List <Employee2> employee2List;
    List <Tag2> tag2List;


    class Employee2 implements Serializable{
		private Long id;

        Employee2(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

    }
    
    class Tag2 implements Serializable{
        private Long id;
        private Long employeeId;

        Tag2(Long id, Long employeeId) {
            this.id = id;
            this.employeeId = employeeId;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(Long employeeId) {
            this.employeeId = employeeId;
        }
    }
    

    class Employee1 implements Serializable{
		private Long id;
        private Long tagId;
        private TagHolder tagHolder = new TagHolder();

        Employee1(Long id, Long tagId) {
            this.id = id;
            this.tagId = tagId;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getTagId() {
            return tagId;
        }

        public void setTagId(Long tagId) {
            this.tagId = tagId;
        }

        public TagHolder getTagHolder() {
            return tagHolder;
        }

        public void setTagHolder(TagHolder tagHolder) {
            this.tagHolder = tagHolder;
        }
    }

    class Tag1 implements Serializable{
        private Long id;

        Tag1(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    class TagHolder implements Serializable{
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    public void setUp() {
        employee1List = new ArrayList<Employee1>();
        employee1List.add(new Employee1(1L, 1L));
        employee1List.add(new Employee1(2L, null));
        tag1List = new ArrayList<Tag1>();
        tag1List.add(new Tag1(1L));
        tag1List.add(new Tag1(2L));

        employee2List = new ArrayList<Employee2>();
        employee2List.add(new Employee2(1L));
        employee2List.add(new Employee2(2L));
        tag2List = new ArrayList<Tag2>();
        tag2List.add(new Tag2(1L, null));
        tag2List.add(new Tag2(2L, 1L));

    }

    boolean test1 = true;

    public void test1() throws Exception {

        SelectOneByIdController <Serializable, Serializable, Serializable> controller = new SelectOneByIdController<Serializable, Serializable, Serializable>(){

			@Override
			protected List<Row> getRows() {
				return null;
			}

			@Override
			protected void prepareModelChoices(List<Row> availableTags) {
			}
			
			@Override			
		    protected List<Serializable> findSelectedChildren(String property, Object value) {
                List<Serializable> list = new ArrayList<Serializable>();
                return list;
            }
			
			protected BeanWrapper getParent() {
                if (test1) {
                    return new BeanWrapperImpl(employee1List.get(0));
                }else {
                    return new BeanWrapperImpl(employee2List.get(0));
                }
            }
			
		    protected void updateChildren(Set<Serializable> unselectedChildren) {
		    }
			
		};
		controller.setSourceProperty("id");
		controller.setTargetProperty("tagId");
		controller.setToParent(true);
		controller.setClickedItem(tag1List.get(1));
		controller.process();
		assertEquals((Long)2L, (Long)employee1List.get(0).getTagId());

        test1=false;
        controller.setSourceProperty("id");
        controller.setTargetProperty("employeeId");
        controller.setToParent(false);
        controller.setClickedItem(tag2List.get(0));
        controller.process();
        assertEquals((Long)1L, (Long)tag2List.get(0).getEmployeeId());

        test1=true;

        controller.setSourceProperty("id");
        controller.setTargetProperty("tagHolder.id");
        controller.setToParent(true);
        controller.setClickedItem(tag1List.get(1));
        controller.process();
        assertEquals((Long)2L, (Long)employee1List.get(0).getTagHolder().getId());


    }

}
