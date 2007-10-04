package com.arcmind.jpa.course.model;



import java.io.File;
import java.io.FilenameFilter;

import javax.persistence.EntityManager;

import com.arcmind.jpa.utils.JpaUtils;

import junit.framework.TestCase;

//import javax.persistence.Query;
//import java.util.List;

public class SQLJpaTest extends TestCase {
	private EntityManager entityManager;

	
	@SuppressWarnings("unchecked")
	public void testSqlSimple() throws Exception {
		//TODO Simple Query select * from the Task Table.
		//TODO Check the log and make sure this is doing what you think it should.
		//		for (Task task : resultList) {
		//			System.out.println(task);
		//		}
	}
	
	@SuppressWarnings("unchecked")
	public void testComplex() throws Exception {
		//TODO Simple Query select individual columns from the Task Table.
		//TODO Check the log and make sure this is doing what you think it should.
		//HINT select t.id as id, t.version as version,
		//			t.name as name,
		// 			t.estimate as estimate, 
		//			t.actual as actual 
		//		from  Task t
		//		for (Task task : resultList) {
		//			System.out.println(task);
		//		}

	}

	@SuppressWarnings("unchecked")
	public void testComplexDifferentTable() throws Exception {
		//TODO Select columns from TaskHistory and map it to the Task.class
		//TODO Check the log and make sure this is doing what you think it should.
		//HINT entityManager.createNativeQuery(sQuery, Task.class);
		//		for (Task task : resultList) {
		//			System.out.println(task);
		//		}

	}

	@SuppressWarnings("unchecked")
	public void testComplexDifferentTableDifferentColumns() throws Exception {
		//TODO Select columns from DeletedTask and map it to the Task.class
		//TODO Map column act to actual, Map column est to estimate
		//HINT dt.act  AS actual 
		
		//		for (Task task : resultList) {
		//			System.out.println(task);
		//		}
	}

	@SuppressWarnings("unchecked")
	public void testUsingTaskToDeletedTaskMapping() throws Exception {
		//TODO Go to the Task class and create TaskToDeletedTask SqlResultSetMapping
//HINT		String sQuery = "" +
//				"SELECT * FROM DeletedTask dt";
//		Query nativeQuery = entityManager.createNativeQuery(sQuery, "TaskToDeletedTask");
//		List<Task> resultList = nativeQuery.getResultList();
//		for (Task task : resultList) {
//			System.out.println(task);
//		}
	}

	@SuppressWarnings("unchecked")
	public void testTypeWithScaler() throws Exception {
		//TODO Go to the DeletedTask.class and create TaskToDeletedTaskPlusFooAndDelDate SqlResultSetMapping
		//BTW You can put named queries in any entity or mapped super class.
//HINT
//		String sQuery = "" +
//				"SELECT  1 as foo, dt.deleteDate AS delDate, * FROM DeletedTask dt";
//		Query nativeQuery = entityManager.createNativeQuery(sQuery, 
//				"TaskToDeletedTaskPlusFooAndDelDate");
//		List<Object[]> resultList = nativeQuery.getResultList();
//		for (Object[] row: resultList) {
//			System.out.printf("foo=%s delDate=%s task=%s \n", row[0], row[1], row[2]);
//		}
	}

	@SuppressWarnings("unchecked")
	public void testMoreThanOneEntity() throws Exception {
		//TODO Go to the TaskHistory.class and create TwoEntities SqlResultSetMapping

//HINT		String sqlQuery = 
//		" select p.id as pid, t.id as tid, p.name as pname, t.version as tversion, " +
//        " t.name as tname, t.estimate as est, t.actual as act " + 
//        " from Person p inner join Person_Task tasks  on p.id=tasks.Person_id " +  
//        " inner join Task t  on tasks.tasks_id=t.id";
//		Query nativeQuery = entityManager.createNativeQuery(sqlQuery, "TwoEntities");
//		List<Object[]> resultList = nativeQuery.getResultList();
//
//		for (Object[] row: resultList) {
//			System.out.printf("person=%s task=%s\n", row[0], row[1]);
//		}
	}

	@SuppressWarnings("unchecked")
	public void testMoreThanOneEntityNamed() throws Exception {

		//TODO Finish this method. Use the "peopleAndTasks" named query defined in TaskHistory class
		//TODO Ensure the "peopleAndTasks" query is complete
		
// HINT		for (Object[] row: resultList) {
//			System.out.printf("person=%s task=%s\n", row[0], row[1]);
//		}
	}

	// YOU ARE DONE
	protected void setUp() throws Exception {
		createTasks();
		JpaUtils.createEntityManagerAndStartTransaction();
		entityManager =JpaUtils.getCurrentEntityManager();
		
		System.out.println("TEST OBJECTS ARE SETUP-------------------------------------------------------");
	}


	private void createTasks() throws Exception {
		JpaUtils.createEntityManagerAndStartTransaction();
		entityManager =JpaUtils.getCurrentEntityManager();
		entityManager.merge(new Task("Call Wife", 1, 2));
		entityManager.merge(new Task("Hang out with Son", 2, 1));
		entityManager.merge(new Task("Hassle Paul Tabor", 2, 1));
		entityManager.merge(new Task("Move to Tucson AZ which is better than Phoenix AZ", 2, 1));
		entityManager.merge(new TaskHistory("Hassle Paul Tabor", 2, 1));
		entityManager.merge(new TaskHistory("Move to Tucson AZ which is better than Phoenix AZ", 2, 1));
		entityManager.merge(new DeletedTask("Hassle Paul Tabor", 2, 1));
		entityManager.merge(new DeletedTask("Move to Tucson AZ which is better than Phoenix AZ", 2, 1));
		person = entityManager.merge(new Person("Paul", new Task("Send Rick money", 2, 1),new Task("Buy Rick lunch", 2, 1)));
		JpaUtils.commitTransactionAndCloseEntityManager();
		
	}
	Person person = null;

	protected void tearDown() throws Exception {
		System.out.println("TEARING DOWN TEST OBJECTS ---------------------------------------------------------");
		try {
			deleteTasks();
		} finally {
			JpaUtils.cleanup();
		}
	}

	private void deleteTasks() throws Exception{
		JpaUtils.createEntityManagerAndStartTransaction();
		person = entityManager.merge(person);
		entityManager.remove(person);
		int rows = entityManager.createQuery("delete from Task").executeUpdate();
		rows += entityManager.createQuery("delete from TaskHistory").executeUpdate();
		rows += entityManager.createQuery("delete from DeletedTask").executeUpdate();
		System.out.printf("This many rows were deleted %s", rows);
		JpaUtils.commitTransactionAndCloseEntityManager();		
	}

	static {
		destroyDB(); //UNCOMMENT THIS IF THINGS GO WRONG
	}
	@SuppressWarnings("unused")
	private static void destroyDB() {
		File tmpDir = new File("/TMP");
		if (tmpDir.exists()) {
			File[] files = tmpDir.listFiles(new FilenameFilter() {

				public boolean accept(File dir, String name) {
					return name.contains("PROTO");
				}

			});
			for (File file : files) {
				file.delete();
			}
		}

	}

}
