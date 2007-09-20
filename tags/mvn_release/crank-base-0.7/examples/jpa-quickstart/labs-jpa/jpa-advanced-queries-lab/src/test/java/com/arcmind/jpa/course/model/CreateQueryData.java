package com.arcmind.jpa.course.model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.arcmind.jpa.utils.JpaTemplate;
import com.arcmind.jpa.utils.JpaUtils;

public class CreateQueryData {

	/**
	 * @param args
	 */
	public static void main(String... args) throws Exception {
		destroyDB();

		final Map<String, Role> roles = createRoles("Admin", "Member", "Lead");

		User rick = new User("RickHigh", new ContactInfo(new PhoneNumber(
				"home", "5205551212")), new Task("UML Diagram for Joe", 1, 1),
				new Task("Finish Refactor", 2, 1),
				new Task("Setup SVN", 1, 10), new Task("Write Eclipse Plugin",
						1, 2));

		User paul = new User("Paul Tab", new ContactInfo(new PhoneNumber(
				"home", "4085551212")), new Task("UML Diagram for Joe", 1, 1),
				new Task("Finish Refactor", 2, 1), new Task("Setup SVN", 1, 2));

		User chris = new User("Chirs Mass", new ContactInfo(new PhoneNumber(
				"home", "6025551212")), new Task("Come up with cache strategy",
				1, 1), new Task("Finish Refactor", 2, 1), new Task("Setup SVN",
				1, 2));

		User scott = new User("Chirs Mass", new ContactInfo(new PhoneNumber(
				"home", "6025551212")));

		rick.addRole(roles.get("Lead"));

		final Group group = new Group("Crank Team", rick, paul, chris, scott);

		group.addRole(roles.get("Admin"));

		JpaUtils.execute(new JpaTemplate() {
			public void execute() {
				JpaUtils.getCurrentEntityManager().persist(group);
			}
		});

		JpaUtils.cleanup();

		JpaUtils.execute(new JpaTemplate() {
			public void execute() throws Exception {
				selectAllSubjects(JpaUtils.getCurrentEntityManager());
				selectImplicitJoin(JpaUtils.getCurrentEntityManager(), roles);
				selectJoinFetch(JpaUtils.getCurrentEntityManager(), roles);
				selectObjectConstruction(JpaUtils.getCurrentEntityManager(),
						roles);
				selectObjectConstruction2(JpaUtils.getCurrentEntityManager(),
						roles);

			}
		});

	}

	private static Map<String, Role> createRoles(final String... roleNames)
			throws Exception {
		final Map<String, Role> map = new HashMap<String, Role>();
		JpaUtils.execute(new JpaTemplate() {
			public void execute() throws Exception {
				for (String roleName : roleNames) {
					Role role = new Role(roleName);
					map.put(roleName, role);
					JpaUtils.getCurrentEntityManager().persist(role);
				}
			}
		});
		return map;
	}

	@SuppressWarnings("unchecked")
	public static void selectAllSubjects(EntityManager entityManager)
			throws Exception {
		//TODO Select all objects that are subjects
//		Query createQuery = entityManager
//				.createQuery("");

		//TODO Uncomment this
//		List<Subject> subjects = (List<Subject>) createQuery.getResultList();
//		for (Subject subject : subjects) {
//			System.out.println(subject.getName());
//		}
	}

	public static void selectImplicitJoin(EntityManager entityManager,
			Map<String, Role> roles) throws Exception {

		//Works with strategy=InheritanceType.SINGLE_TABLE
		//Does not work with strategy=InheritanceType.JOINED
		Query query = null;


		//TODO Create a query that selects group based on user.name LIKE 'Rick%'
		//TODO Put a breakpoint and what SQL gets generated.
		//1 Query
		
		//TODO Create a query that works with implicit joins.
		//TODO Select phone numbers that begin with 520
		//HINT  SELECT g FROM Group g JOIN g.users users
		//2nd Query

		//TODO create a query that does the following:
		// Join group users
		// Joins users tasks
		// Restricts phone number that start with 520 (users.contactInfo.phoneNumbers.number)
		// Restricts to tasks that exceed their estimates (tasks.estimate < tasks.actual)
		// Restricts user names (users.name like 'Rick%')
		// Restrict tasks that are between 5 and 20
		//3rd Query

		//TODO Find groups that have no contactInfo.
		//4th Query

		//TODO Find groups that have more than 10 users in them
		//5th Query
		
		
		//TODO Find groups that are in the role Admin using the "in" keyword and named parameters.
		Role adminRole = roles.get("Admin");
		Role leadRole = roles.get("Lead");
		//6th Query
		
		
		//TODO Find groups in lead or admin using "in" keyword and named parameters.
		//7th Query

		/* TODO Return groups where ALL users have estimates over 0 */
		//8th

		/* TODO Return groups where ANY user has estimates over 7 */
		//9th


		//showGroupResults(query);

		//TODO change as follows: Works with strategy=InheritanceType.SINGLE_TABLE
		//Does not work with strategy=InheritanceType.JOINED
		// Change to strategy=InheritanceType.JOINED
		//	  createQuery =	 entityManager.createQuery("select g from Group g " +
		//	  		" where g.users.name like 'Rick%'");
		
	}

	@SuppressWarnings("unchecked")
	public static void selectNplus1(EntityManager entityManager,
			Map<String, Role> roles) throws Exception {
		System.out.println("N+1 EXAMPLE");

		/* TODO see if this causes an N+1 issues. */
		List<User> resultList = (List<User>) entityManager.createQuery(
				"select user from User user").getResultList();

		for (User user : resultList) {
			List<Task> tasks = (List<Task>) user.getTasks();
			for (Task task : tasks) {
				System.out.println(task.getName());
			}
		}

	}

	@SuppressWarnings("unchecked")
	public static void selectObjectConstruction(EntityManager entityManager,
			Map<String, Role> roles) throws Exception {

		/* TODO try using an object construction query. */
//		List<User> resultList = (List<User>) entityManager.createQuery(
//				"")
//				.getResultList();
//
//		for (User user : resultList) {
//			System.out.println(user.getName());
//			System.out.println(user.getId());
//		}

	}

	@SuppressWarnings("unchecked")
	public static void selectObjectConstruction2(EntityManager entityManager,
			Map<String, Role> roles) throws Exception {

		/* TODO try using an object construction query. */		
		List<UserInfo> resultList = null; 
//		for (UserInfo user : resultList) {
//			System.out.println(user.getGroupName());
//			System.out.println(user.getName());
//			System.out.println(user.getId());
//		}

	}

	@SuppressWarnings("unchecked")
	public static void selectJoinFetch(EntityManager entityManager,
			Map<String, Role> roles) throws Exception {
		System.out.println("No N+1 EXAMPLE");
		
		//TODO Run this and see if there is an N+1 problem.
		//TODO Look at the log file.

		List<User> resultList = (List<User>) entityManager.createQuery(
				"SELECT DISTINCT user FROM User user "
						+ "LEFT OUTER JOIN FETCH user.tasks").getResultList();

		for (User user : resultList) {
			List<Task> tasks = (List<Task>) user.getTasks();
			for (Task task : tasks) {
				System.out.println(task.getName());
			}
		}

	}

	@SuppressWarnings("unchecked")
	private static void showGroupResults(Query createQuery) {
		List<Group> results = (List<Group>) createQuery.getResultList();
		for (Group group : results) {
			System.out.println(group.getName());
		}
	}

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
