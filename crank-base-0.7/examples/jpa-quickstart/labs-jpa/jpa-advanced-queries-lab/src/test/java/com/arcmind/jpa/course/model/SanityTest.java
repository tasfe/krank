package com.arcmind.jpa.course.model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Iterator;


import com.arcmind.jpa.course.model.Address;
import com.arcmind.jpa.course.model.ContactInfo;
import com.arcmind.jpa.course.model.Group;
import com.arcmind.jpa.course.model.PhoneNumber;
import com.arcmind.jpa.course.model.Role;
import com.arcmind.jpa.course.model.User;
import com.arcmind.jpa.utils.JpaTemplate;
import com.arcmind.jpa.utils.JpaTemplateWithReturn;
import com.arcmind.jpa.utils.JpaUtils;

import junit.framework.TestCase;

public class SanityTest extends TestCase {


	private String[] roles = new String[] { "ADMIN", "USER", "SUPER_USER" };

	private void createRoles() throws Exception {
		/* Setup the roles. */
		JpaUtils.execute(new JpaTemplate() {
			public void execute() {

				for (String sRole : roles) {
					JpaUtils.getCurrentEntityManager().persist(new Role(sRole));
				}
			}
		});
	}

	static {
		destroyDB();
	}

	protected void setUp() throws Exception {
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


		try {
			deleteRoles();
		} catch (Exception ex) {
			destroyDB();
		}

	}

	private void deleteRoles() throws Exception {
		/* Delete the roles. */
		JpaUtils.execute(new JpaTemplate() {
			public void execute() {
				JpaUtils.getCurrentEntityManager().createQuery("delete Role").executeUpdate();
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
		
		User paulTab = new User("PaulTab", contactInfo, 
				new Task("Refactor Code base for DAO", 4, 5),
				new Task("Design new Validation framework", 5, 4),
				new Task("Integrate CRUD framework to work with GWT", 3, 4));

		group.addUser(paulTab);

		/* Persist the group. */
		JpaUtils.execute(new JpaTemplate() {
			public void execute() {

				JpaUtils.getCurrentEntityManager().persist(group);

				/* Associate the group with a role. */
				group.getRoles().add(
						(Role) JpaUtils.getCurrentEntityManager().createNamedQuery(
								"loadRole").setParameter("name",
								"ADMIN").getSingleResult());

			}
		});

		/* Shut down the entityManager session. */
		JpaUtils.cleanup();

		/* Read the group. */
		Group loadedGroup = loadGroup();

		/* Ensure it was written to the database correctly. */
		assertEquals("sysadmins", loadedGroup.getName()); //1
		assertEquals("ADMIN", loadedGroup.getRoles().iterator().next().getName()); //2
		assertEquals(3, loadedGroup.getUsers().size()); //3
		
		
		Iterator<User> iterator = loadedGroup.getUsers().iterator();
		
		User paul = null;
		while (iterator.hasNext()) {
			paul = iterator.next();
			if (paul.getName().equals("PaulTab")) {
				break;
			}
		}
		
		 
		assertEquals("PaulTab", paul.getName()); //4
		assertEquals("85748", paul.getContactInfo().getAddress().getZip()); //5
		assertEquals("95503", paul.getContactInfo().getWorkAddress().getZip()); //6
		assertEquals("sysadmins", paul.getParentGroup().getName()); //7
		User user = (User) paul.getContactInfo().getSubject();
		assertEquals("PaulTab", user.getName()); //8
		assertEquals("520290X16X", paul.getContactInfo().getPhoneNumbers()
				.get("home").getNumber()); //9
		
		

		/* Demonstrate laziness issues. ----------------------------------------------- */
		JpaUtils.cleanup();

		/* Read the group. */
		loadedGroup = loadGroup();

		JpaUtils.cleanup();

		//   LAZY INIT PROBLEM GOES AWAY
		assertEquals("ADMIN", loadedGroup.getRoles().iterator().next().getName()); //1


		/* Reread the group. */
		final Group groupToDelete = loadGroup();

		/* Delete the group and all users in the group. */
		JpaUtils.execute(new JpaTemplate() {
			public void execute() {
				Group group = groupToDelete;
				
				JpaUtils.getCurrentEntityManager().remove(group);
				group.getRoles().clear();
				
			}
		});

	}

	private Group loadGroup() throws Exception {
		Group loadedGroup = (Group) JpaUtils.execute(new JpaTemplateWithReturn() {
			public Object execute() {

				return (Group) JpaUtils.getCurrentEntityManager().createNamedQuery(
						"loadGroup").setParameter("name", "sysadmins")
						.getSingleResult();
			}
		});
		return loadedGroup;
	}
}
