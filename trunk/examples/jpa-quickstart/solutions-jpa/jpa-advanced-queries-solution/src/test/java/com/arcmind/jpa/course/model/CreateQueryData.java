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
	
	/**
	 * @param args
	 */
  public static void main(String[] args) throws Exception {
	  destroyDB();
	  
	  final Map<String, Role> roles = createRoles("Admin", "Member", "Lead");
      
	  User rick = new User("RickHigh", new ContactInfo( new PhoneNumber("home", "5205551212") ), 
              new Task("UML Diagram for Joe", 1, 1), new Task("Finish Refactor", 2, 1), 
              new Task("Setup SVN", 1, 10), new Task("Write Eclipse Plugin", 1, 2));

	  User paul = new User("Paul Tab", new ContactInfo( new PhoneNumber("home", "4085551212") ), 
	            new Task("UML Diagram for Joe", 1, 1), new Task("Finish Refactor", 2, 1), new Task("Setup SVN", 1, 2));

	  User chris = new User("Chirs Mass", new ContactInfo( new PhoneNumber("home", "6025551212") ), 
              new Task("Come up with cache strategy", 1, 1), new Task("Finish Refactor", 2, 1), 
              new Task("Setup SVN", 1, 2));
              
       User scott = new User("Chirs Mass", new ContactInfo( new PhoneNumber("home", "6025551212") ));
              
	  
	  
	  
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
			public void execute() throws Exception{
				//selectAllSubjects(JpaUtils.getCurrentEntityManager());
				//selectAllIdentifiable(JpaUtils.getCurrentEntityManager());
				//selectImplicitJoin(JpaUtils.getCurrentEntityManager(), roles);
				//selectJoinFetch(JpaUtils.getCurrentEntityManager(), roles);
//				selectObjectConstruction(JpaUtils.getCurrentEntityManager(), roles);
				selectObjectConstruction2(JpaUtils.getCurrentEntityManager(), roles);
				
			}
		});

  }
  
  private static Map<String, Role> createRoles(final String... roleNames) throws Exception{
	  final Map<String, Role> map = new HashMap<String, Role>();
      JpaUtils.execute(new JpaTemplate() {
			public void execute() throws Exception{
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
 public static void selectAllSubjects(EntityManager entityManager) throws Exception{
	 
	  Query createQuery = entityManager.createQuery("select subject from Subject subject");
	  
	  List<Subject> subjects = (List<Subject>) createQuery.getResultList();
	  for (Subject subject : subjects) {
		  System.out.println(subject.getName());
	  }
  }

 public static void  selectImplicitJoin(EntityManager entityManager, Map<String, Role> roles ) throws Exception{
	 
	  //Works with strategy=InheritanceType.SINGLE_TABLE
	  //Does not work with strategy=InheritanceType.JOINED
	  Query query = null; 
		  
	  //Works with strategy=InheritanceType.SINGLE_TABLE
	  //Does not work with strategy=InheritanceType.JOINED		  
//	  createQuery =	 entityManager.createQuery("select g from Group g " +
//	  		" where g.users.name like 'Rick%'");

	  
	  query =	 entityManager.createQuery("SELECT g  FROM Group g JOIN g.users user WHERE user.name LIKE 'Rick%'");

	  
//	  createQuery = entityManager.createQuery("select g from Group g " +
//		" where g.users.contactInfo.phoneNumbers.number like '520%'");// breaks hibernate
	  
	  query = entityManager.createQuery("SELECT g FROM Group g " +
		" JOIN g.users users WHERE users.contactInfo.phoneNumbers.number LIKE '520%'");

	  query = entityManager.createQuery(
		"SELECT distinct g " + 
		" FROM Group g " + 
		" JOIN g.users users " +
		" JOIN users.tasks tasks " + 		
		" WHERE users.contactInfo.phoneNumbers.number LIKE '520%' " +
		" AND users.name like 'Rick%' " +
		" AND tasks.estimate < tasks.actual " +
		" AND tasks.actual between 5 and 20 " );
	  

	  query = entityManager.createQuery(
				"SELECT distinct g " + 
				" FROM Group g " + 
				" WHERE g.contactInfo is not null ");
	  
	  query = entityManager.createQuery(
				" SELECT distinct g " + 
				" FROM Group g " + 
				" WHERE SIZE(g.users) > 0");

	  Role adminRole = roles.get("Admin");
	  query = entityManager.createQuery(				
			  	" SELECT g " + 
				" FROM Group g " + 
				" WHERE :role in (select role.id from g.roles role)")
				.setParameter("role", adminRole.getId());

	  adminRole = roles.get("Admin");
	  Role leadRole = roles.get("Lead");
	  query = entityManager.createQuery(				
			  	" SELECT g " + 
				" FROM Group g " + 
				" WHERE g.roles.id in (:admin, :lead)")
				.setParameter("admin", adminRole.getId())
				.setParameter("lead", leadRole.getId());

	  
	  /* Return groups where ALL users have estimates over 0 */
	  query = entityManager.createQuery(				
	  " SELECT distinct u.parentGroup  FROM User u " +
	  " WHERE 0 > ALL (select t.estimate from u.tasks t)");

	  /* Return groups where ANY user has estimates over 7 */
	  query = entityManager.createQuery(				
	  " SELECT distinct u.parentGroup  FROM User u " +
	  " WHERE 7 > ANY (select t.estimate from u.tasks t)");
	  
	  /* Return groups where ANY user has estimates over 7 */
	  query = entityManager.createQuery(				
	  "SELECT distinct u.parentGroup  FROM User u " +
	  " WHERE 7 > SOME (select t.estimate from u.tasks t)");

	  showGroupResults(query);
	  
	  
  }

 @SuppressWarnings("unchecked")
public static void  selectNplus1(EntityManager entityManager, Map<String, Role> roles ) throws Exception{
	 System.out.println("N+1 EXAMPLE");
	 
	 List<User> resultList = (List<User>) 
	 			entityManager.createQuery("select user from User user").getResultList();
	 
	 for (User user : resultList) {
		 List<Task> tasks = (List<Task>) user.getTasks();
		 for (Task task : tasks) {
			 System.out.println(task.getName());
		 }
	 }
	 
	 
 } 
 
 @SuppressWarnings("unchecked")
 public static void  selectObjectConstruction(EntityManager entityManager, Map<String, Role> roles ) throws Exception{

	 List<User> resultList = (List<User>) 
		entityManager.createQuery("SELECT new User(user.id, user.name) FROM User user").getResultList();
	 
	 for (User user : resultList) {
		 System.out.println(user.getName());
		 System.out.println(user.getId());
	 }
	 
	 
 }

 @SuppressWarnings("unchecked")
 public static void  selectObjectConstruction2(EntityManager entityManager, Map<String, Role> roles ) throws Exception{

	 List<UserInfo> resultList = (List<UserInfo>) 
		entityManager.createQuery("SELECT " +
				"	new com.arcmind.jpa.course.model.UserInfo(" +
				"             user.id, user.name, user.parentGroup.name) " +
				"   FROM User user").getResultList();
	 
	 for (UserInfo user : resultList) {
		 System.out.println(user.getGroupName());
		 System.out.println(user.getName());
		 System.out.println(user.getId());
	 }
	 
	 
 }
 

 @SuppressWarnings("unchecked")
 public static void  selectJoinFetch(EntityManager entityManager, Map<String, Role> roles ) throws Exception{
	 System.out.println("No N+1 EXAMPLE");
	 
//	 List<User> resultList = (List<User>) 
//		entityManager.createQuery("SELECT DISTINCT user FROM User user " +
//								  " JOIN FETCH user.tasks").getResultList();

	 List<User> resultList = (List<User>) 
		entityManager.createQuery("SELECT DISTINCT user FROM User user " +
				                   "LEFT OUTER JOIN FETCH user.tasks").getResultList();

	 //System.out.println(resultList.size());
	 
	 

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


 /* Not possible with JPA but doable with Hibernate. */
// @SuppressWarnings("unchecked")
// public static void selectAllIdentifiable(EntityManager entityManager) throws Exception{
//	 
//	  Query createQuery = entityManager.createQuery("select identifiable from Identifiable identifiable");
//	  
//	  List<Identifiable> results = (List<Identifiable>) createQuery.getResultList();
//	  for (Identifiable result : results) {
//		  System.out.println(result.name());
//	  }
//  }
 
}
