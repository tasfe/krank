package com.arcmind.jpa.course.inheritance2.model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import junit.framework.TestCase;

public class Inheritance2RelationshipsTest extends TestCase {

	private EntityManager entityManager;
	private EntityManagerFactory entityManagerFactory;
	private EntityTransaction transaction;

	private String[] roles = new String[] { "ADMIN", "USER", "SUPER_USER" };

	private void createRoles() throws Exception {
		/* Setup the roles. */
		execute(new TransactionTemplate() {
			public Object execute() {

				for (String sRole : roles) {
					entityManager.persist(new Role(sRole));
				}
				return null;
			}
		});
	}

	static {
		//destroyDB();
	}

	protected void setUp() throws Exception {
		/* Use Persistence.createEntityManagerFactory to create 
		 * "security-domain" persistence unit. */
		entityManagerFactory = Persistence
				.createEntityManagerFactory("security-domain");

		deleteRoles();
		createRoles();

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

	protected void tearDown() throws Exception {

		if (entityManager != null && entityManager.isOpen()) {
			entityManager.close();
		}
		if (transaction != null && transaction.isActive()) {
			if (transaction.getRollbackOnly()) {
				transaction.rollback();
			}
		}

		try {
			deleteRoles();
		} catch (Exception ex) {
			//destroyDB();
		}

		entityManager = null;
		transaction = null;
	}

	private void deleteRoles() throws Exception {
		/* Delete the roles. */
		execute(new TransactionTemplate() {
			public Object execute() {

				entityManager.createQuery("delete In2Role").executeUpdate();

				return null;
			}
		});
	}

	public void testGroupUserRelationship() throws Exception {

		/* Construct the group. */
		final Group group = new Group("sysadmins");
		group.addUser(new User("RickHigh"));
		group.addUser(new User("PaulHix"));
		
		ContactInfo contactInfo = new ContactInfo("5205551212", "Paul",
				"Taboraz", new Address("123 Main", "", "85748", "AZ"),
				new Address("1350 A Kinney", "", "95503", "CA"));
		contactInfo.addPhoneNumber(new PhoneNumber("home", "520290X16X"));
		contactInfo.addPhoneNumber(new PhoneNumber("mobile", "52029037XX"));
		
		User paulTab = new User("PaulTab", contactInfo);

		group.addUser(paulTab);

		/* Persist the group. */
		execute(new TransactionTemplate() {

			public Object execute() {

				entityManager.persist(group);

				/* Associate the group with a role. */
				group.getRoles().add(
						(Role) entityManager.createNamedQuery(
								"in2.loadRole").setParameter("name",
								"ADMIN").getSingleResult());

				return null;
			}

		});

		/* Shut down the entityManager session. */
		entityManager.close();
		entityManager = entityManagerFactory.createEntityManager();

		/* Read the group. */
		Group loadedGroup = (Group) execute(new TransactionTemplate() {

			public Object execute() {

				return (Group) entityManager.createNamedQuery(
						"in2.loadGroup").setParameter("name", "sysadmins")
						.getSingleResult();

			}

		});

		/* Ensure it was written to the database correctly. */
		assertEquals("sysadmins", loadedGroup.getName()); //1
		assertEquals("ADMIN", loadedGroup.getRoles().iterator().next().getName()); //2
		assertEquals(3, loadedGroup.getUsers().size()); //3
		
		Iterator<User> iterator = loadedGroup.getUsers().iterator();
		iterator.next();
		User paul = iterator.next();
		assertEquals("PaulTab", paul.getName()); //4
		assertEquals("85748", paul.getContactInfo().getAddress().getZip()); //5
		assertEquals("95503", paul.getContactInfo().getWorkAddress().getZip()); //6
		assertEquals("sysadmins", paul.getParentGroup().getName()); //7
		assertEquals("520290X16X", paul.getContactInfo().getPhoneNumbers()
				.get("home").getNumber()); //8
		
		

		/* Demonstrate laziness issues. ----------------------------------------------- */
		entityManager.close();
		entityManager = entityManagerFactory.createEntityManager();

		/* Reread the group. */
		loadedGroup = (Group) execute(new TransactionTemplate() {

			public Object execute() {

				return (Group) entityManager.createNamedQuery(
						"in2.loadGroup").setParameter("name", "sysadmins")
						.getSingleResult();

			}

		});

		entityManager.close();

		//   LAZY INIT PROBLEM GOES AWAY
		assertEquals("ADMIN", loadedGroup.getRoles().iterator().next().getName()); //1

		entityManager = entityManagerFactory.createEntityManager();

		/* Reread the group. */
		final Group groupToDelete = (Group) execute(new TransactionTemplate() {
			public Object execute() {
				return (Group) entityManager.createNamedQuery(
						"in2.loadGroup").setParameter("name", "sysadmins")
						.getSingleResult();
			}
		});

		/* Delete the group and all users in the group. */
		execute(new TransactionTemplate() {
			public Object execute() {
				Group group = groupToDelete;
				
				entityManager.remove(group);
				group.getRoles().clear();
				
				return null;
			}
		});

	}

	public interface TransactionTemplate {
		Object execute();
	}

	private Object execute(TransactionTemplate tt) throws Exception {
		Object result = null;
		if (entityManager == null || !entityManager.isOpen()) {
			entityManager = entityManagerFactory.createEntityManager();
		}
		boolean join = false;
		if (transaction == null || !transaction.isActive()) {
			transaction = entityManager.getTransaction();
			transaction.begin();
		} else {
			join = true;
		}
		try {
			result = tt.execute();
			try {
			if (!join)
				transaction.commit();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			if (!join) {
				try {
					transaction.rollback();
				} catch (Exception ise) {
					ex.printStackTrace();
				}
			}
			if (join)
				transaction.setRollbackOnly();
			throw ex;
		}
		return result;
	}

}
