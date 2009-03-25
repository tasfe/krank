package org.crank.crud.jsf.support;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.faces.model.DataModel;
import javax.servlet.http.HttpServletRequest;

import org.crank.crud.GenericDao;
import org.crank.crud.controller.FilterablePageable;
import org.crank.crud.controller.Row;
import org.crank.crud.criteria.Comparison;
import org.crank.crud.model.inquiry.PetClinicInquiry;
import org.crank.crud.model.inquiry.PetClinicLead;
import org.crank.jsfspring.test.CrankMockObjects;
import org.crank.test.base.SpringTestNGBase;
import org.crank.test.base.SpringTestingUtility;
import org.crank.web.HttpRequestUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class JsfCrudAdapterIntegrationTest extends SpringTestNGBase {
	private CrankMockObjects crankMockObjects;
	@SuppressWarnings("unchecked")
	private JsfCrudAdapter petClinicLeadCrud;
	
	@SuppressWarnings("unchecked")
	private Map<String, GenericDao> repos;
	
	@Override
	public String getModuleName() {
		return "crankWebExample";
	}
	
	@BeforeClass (groups="setup", dependsOnGroups="class-init")
	public void setup(){
		
        for (int index = 0; index < 10; index ++) {
	        PetClinicLead lead = new PetClinicLead();
	        lead.setName("testLead" + index);
	        PetClinicInquiry inquiry = new PetClinicInquiry();
	        inquiry.setName("testInquiry" + index);
	        inquiry.setAnotherProp("testInquiryAP" + index);
	        lead.setInquiry(inquiry);
	        inquiryRepo().store(inquiry);
	        leadRepo().store(lead);
        }

	}
	
	@AfterClass (groups="tearDown")
	public void tearDown() {
		try {
			List<PetClinicInquiry> inqueries = inquiryRepo().find(Comparison.startsLike("name", "testInquiry"));
			List<PetClinicLead> leads = leadRepo().find(Comparison.startsLike("name", "testLead"));
			
			leadRepo().delete(leads);
			inquiryRepo().delete(inqueries);
		} catch(Exception ex) {
			
		}
		
	}

	@SuppressWarnings("unchecked")
	@Test 
	public void testPagination() {
		FilterablePageable paginator = petClinicLeadCrud.getPaginator();
        List page = paginator.getPage();
		assert page.size() > 0 : "There is some data in here";
		
		System.out.println("paginator 1"  + paginator);
		
		petClinicLeadCrud.clear();
		DataModel model = petClinicLeadCrud.getModel();
		assert model != null : "The model is not null";
		List<Row> list = (List<Row>) model.getWrappedData();
		assert list.size() > 0 : "There is some data in here";
		Row row = list.get(0);
		PetClinicInquiry pci = (PetClinicInquiry) row.get("inquiry");
		assert pci.getAnotherProp().startsWith("testInquiry") : "Data seems correct";
		
	}
			
	
	@Override
	public List<String> getConfigLocations() {
		return new ArrayList<String>(Arrays.asList(new String [] {"classpath:applicationContext.xml"}));
	}

	public void setUpSpring() {
    	applicationContext = SpringTestingUtility.getContext( null, getConfigLocations(), contexts, false, getModuleName() );
    	crankMockObjects = new CrankMockObjects();
		try {
			crankMockObjects.setUp();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		crankMockObjects.setUpApplicationContextWithScopes(applicationContext);
        applicationContext.getBeanFactory().autowireBeanProperties( this,
                    AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false );
        
        HttpRequestUtils.setHttpRequest( (HttpServletRequest) crankMockObjects.getExternalContext().getRequest() );        
			
	}
	@SuppressWarnings("unchecked")
	public void setRepos(Map<String, GenericDao> repos) {
		this.repos = repos;
	}
	
	@SuppressWarnings("unchecked")
	public GenericDao<PetClinicLead, Long> leadRepo() {
		return (GenericDao<PetClinicLead, Long>)repos.get("PetClinicLead");
	}

	@SuppressWarnings("unchecked")
	public GenericDao<PetClinicInquiry, Long> inquiryRepo() {
		return (GenericDao<PetClinicInquiry, Long>)repos.get("PetClinicInquiry");
	}
	
	@SuppressWarnings("unchecked")
	public void setPetClinicLeadCrud(JsfCrudAdapter petClinicLeadCrud) {
		this.petClinicLeadCrud = petClinicLeadCrud;
	}
}
