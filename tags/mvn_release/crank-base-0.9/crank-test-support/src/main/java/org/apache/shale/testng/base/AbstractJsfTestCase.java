package org.apache.shale.testng.base;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import org.apache.shale.test.ShaleMockObjects;
import org.apache.shale.test.mock.MockApplication;
import org.apache.shale.test.mock.MockExternalContext;
import org.apache.shale.test.mock.MockFacesContext;
import org.apache.shale.test.mock.MockFacesContextFactory;
import org.apache.shale.test.mock.MockHttpServletRequest;
import org.apache.shale.test.mock.MockHttpServletResponse;
import org.apache.shale.test.mock.MockHttpSession;
import org.apache.shale.test.mock.MockLifecycle;
import org.apache.shale.test.mock.MockLifecycleFactory;
import org.apache.shale.test.mock.MockRenderKit;
import org.apache.shale.test.mock.MockServletConfig;
import org.apache.shale.test.mock.MockServletContext;

/**
 * <p>Abstract JUnit test case base class, which sets up the JavaServer Faces
 * mock object environment for a particular simulated request.  The following
 * protected variables are initialized in the <code>setUp()</code> method, and
 * cleaned up in the <code>tearDown()</code> method:</p>
 * <ul>
 * <li><code>application</code> (<code>MockApplication</code>)</li>
 * <li><code>config</code> (<code>MockServletConfig</code>)</li>
 * <li><code>externalContext</code> (<code>MockExternalContext</code>)</li>
 * <li><code>facesContext</code> (<code>MockFacesContext</code>)</li>
 * <li><code>lifecycle</code> (<code>MockLifecycle</code>)</li>
 * <li><code>request</code> (<code>MockHttpServletRequest</code></li>
 * <li><code>response</code> (<code>MockHttpServletResponse</code>)</li>
 * <li><code>servletContext</code> (<code>MockServletContext</code>)</li>
 * <li><code>session</code> (<code>MockHttpSession</code>)</li>
 * </ul>
 *
 * <p>In addition, appropriate factory classes will have been registered with
 * <code>javax.faces.FactoryFinder</code> for <code>Application</code> and
 * <code>RenderKit</code> instances.  The created <code>FacesContext</code>
 * instance will also have been registered in the apppriate thread local
 * variable, to simulate what a servlet container would do.</p>
 *
 * <p><strong>WARNING</strong> - If you choose to subclass this class, be sure
 * your <code>setUp()</code> and <code>tearDown()</code> methods call
 * <code>super.setUp()</code> and <code>super.tearDown()</code> respectively,
 * and that you implement your own <code>suite()</code> method that exposes
 * the test methods for your test case.</p>
 */

public abstract class AbstractJsfTestCase {
	private ShaleMockObjects shaleMockObjects;

	// ------------------------------------------------------------ Constructors

	// ---------------------------------------------------- Overall Test Methods

	/**
	 * <p>Set up instance variables required by this test case.</p>
	 */
	@BeforeMethod
	protected void setUp() throws Exception {
		shaleMockObjects = new ShaleMockObjects();
		shaleMockObjects.setUp();
		application = shaleMockObjects.getApplication();
		config = shaleMockObjects.getConfig();
		externalContext = shaleMockObjects.getExternalContext();
		facesContext = shaleMockObjects.getFacesContext();
		facesContextFactory = shaleMockObjects.getFacesContextFactory();
		lifecycle = shaleMockObjects.getLifecycle();
		lifecycleFactory = shaleMockObjects.getLifecycleFactory();
		renderKit = shaleMockObjects.getRenderKit();
		request = shaleMockObjects.getRequest();
		response = shaleMockObjects.getResponse();
		servletContext = shaleMockObjects.getServletContext();
		session = shaleMockObjects.getSession();

	}

	/**
	 * <p>Tear down instance variables required by this test case.</p>
	 */
	@AfterMethod
	protected void tearDown() throws Exception {
		shaleMockObjects.tearDown();
		application = null;
		config = null;
		externalContext = null;
		facesContext = null;
		facesContextFactory = null;
		lifecycle = null;
		lifecycleFactory = null;
		renderKit = null;
		request = null;
		response = null;
		servletContext = null;
		session = null;

	}

	// ------------------------------------------------------ Instance Variables

	// Mock object instances for our tests
	protected MockApplication application = null;

	protected MockServletConfig config = null;

	protected MockExternalContext externalContext = null;

	protected MockFacesContext facesContext = null;

	protected MockFacesContextFactory facesContextFactory = null;

	protected MockLifecycle lifecycle = null;

	protected MockLifecycleFactory lifecycleFactory = null;

	protected MockRenderKit renderKit = null;

	protected MockHttpServletRequest request = null;

	protected MockHttpServletResponse response = null;

	protected MockServletContext servletContext = null;

	protected MockHttpSession session = null;

}