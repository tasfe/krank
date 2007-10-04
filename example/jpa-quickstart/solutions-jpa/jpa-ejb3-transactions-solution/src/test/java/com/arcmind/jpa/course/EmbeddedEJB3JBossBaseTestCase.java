package com.arcmind.jpa.course;

import java.io.File;
import java.net.URL;
import java.util.Hashtable;

import javax.naming.InitialContext;

import junit.framework.TestCase;

import org.jboss.ejb3.embedded.EJB3StandaloneBootstrap;
import org.jboss.ejb3.embedded.EJB3StandaloneDeployer;

public abstract class EmbeddedEJB3JBossBaseTestCase extends TestCase {
	
	public EmbeddedEJB3JBossBaseTestCase() {
		super("EmbeddedEjb3TestCase");
	}
	

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		startupEmbeddedJboss();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		shutdownEmbeddedJboss();
	}

	public static void startupEmbeddedJboss() throws Exception {
		EJB3StandaloneBootstrap.boot(null);
		
		EJB3StandaloneDeployer deployer = EJB3StandaloneBootstrap.createDeployer();
		URL lDeployDirectories = new File( ".", "target/test-classes" ).toURL();
		deployer.getDeployDirs().add( lDeployDirectories );
		lDeployDirectories = new File( ".", "target/" ).toURL();
		deployer.getDeployDirs().add( lDeployDirectories );
		deployer.getArchivesByResource().add( "META-INF/persistence.xml" );
		deployer.create();
		deployer.start();
		
		//This is an alternative to the above deployer strategy but is hellaslow.
		//So we use the above to limit the scope of the classpath upon which
		// to look for EJB's.
		//EJB3StandaloneBootstrap.scanClasspath();
	}

	public static void shutdownEmbeddedJboss() {
		EJB3StandaloneBootstrap.shutdown();
	}

	public static InitialContext getInitialContext() throws Exception {
		Hashtable props = getInitialContextProperties();
		return new InitialContext(props);
	}

	private static Hashtable getInitialContextProperties() {
		Hashtable props = new Hashtable();
		props.put("java.naming.factory.initial", "org.jnp.interfaces.LocalOnlyContextFactory");
		props.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
		return props;
	}
}