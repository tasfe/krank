package com.arcmind.jpa.course.model;



import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.arcmind.jpa.utils.JpaUtils;

import junit.framework.TestCase;

public class SQLJpaTest extends TestCase {
	private EntityManager entityManager;

	
	@SuppressWarnings("unchecked")
	public void testSqlSimple() throws Exception {
		Query nativeQuery = entityManager.createNativeQuery("select * from Task", Task.class);
		List<Task> resultList = nativeQuery.getResultList();
		for (Task task : resultList) {
			System.out.println(task);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void testComplex() throws Exception {
		
		String sQuery = "select t.id as id, t.version as version," +
				" t.name as name, " +
				" t.estimate as estimate, " +
				" t.actual as actual from " +
				" Task t";
		Query nativeQuery = entityManager.createNativeQuery(sQuery, Task.class);
		List<Task> resultList = nativeQuery.getResultList();
		for (Task task : resultList) {
			System.out.println(task);
		}
	}

	@SuppressWarnings("unchecked")
	public void testComplexDifferentTable() throws Exception {
		
		String sQuery = "select th.id as id, th.version as version," +
				" th.name as name, " +
				" th.estimate as estimate, " +
				" th.actual as actual from " +
				" TaskHistory th";
		Query nativeQuery = entityManager.createNativeQuery(sQuery, Task.class);
		List<Task> resultList = nativeQuery.getResultList();
		for (Task task : resultList) {
			System.out.println(task);
		}
	}

	@SuppressWarnings("unchecked")
	public void testComplexDifferentTableDifferentColumns() throws Exception {
		
		String sQuery = "" +
				"SELECT " +
				" dt.id AS id, " +
				" dt.version AS version," +
				" dt.name AS name, " +
				" dt.est  AS estimate, " +
				" dt.act  AS actual " +
				"FROM DeletedTask dt";
		Query nativeQuery = entityManager.createNativeQuery(sQuery, Task.class);
		List<Task> resultList = nativeQuery.getResultList();
		for (Task task : resultList) {
			System.out.println(task);
		}
	}

	@SuppressWarnings("unchecked")
	public void testUsingTaskToDeletedTaskMapping() throws Exception {
		
		String sQuery = "" +
				"SELECT * FROM DeletedTask dt";
		Query nativeQuery = entityManager.createNativeQuery(sQuery, "TaskToDeletedTask");
		List<Task> resultList = nativeQuery.getResultList();
		for (Task task : resultList) {
			System.out.println(task);
		}
	}

	@SuppressWarnings("unchecked")
	public void testTypeWithScaler() throws Exception {
		String sQuery = "" +
				"SELECT  1 as foo, dt.deleteDate AS delDate, * FROM DeletedTask dt";
		Query nativeQuery = entityManager.createNativeQuery(sQuery, "TaskToDeletedTaskPlusFooAndDelDate");
		List<Object[]> resultList = nativeQuery.getResultList();
		for (Object[] row: resultList) {
			System.out.printf("foo=%s delDate=%s task=%s \n", row[0], row[1], row[2]);
		}
	}

	@SuppressWarnings("unchecked")
	public void testMoreThanOneEntity() throws Exception {

		String sqlQuery = 
		" select p.id as pid, t.id as tid, p.name as pname, t.version as tversion, " +
        " t.name as tname, t.estimate as est, t.actual as act " + 
        " from Person p  " +
        " inner join Person_Task tasks  on p.id=tasks.Person_id " +  
        " inner join Task t  on tasks.tasks_id=t.id";
        
		Query nativeQuery = entityManager.createNativeQuery(sqlQuery, "TwoEntities");

		
		List<Object[]> resultList = nativeQuery.getResultList();
		for (Object[] row: resultList) {
			System.out.printf("person=%s task=%s\n", row[0], row[1]);
		}
	}

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
