package org.crank.crud.controller;

import java.util.List;
import java.util.Map;

import org.crank.crud.GenericDao;
import org.crank.crud.controller.datasource.DaoFilteringPagingDataSource;
import org.crank.crud.criteria.Comparison;
import org.crank.crud.test.DbUnitTestBase;
import org.crank.crud.test.model.PetClinicInquiry;
import org.crank.crud.test.model.PetClinicLead;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * 
 * @version $Revision$
 * @author Rick Hightower
 */
public class FilteringPaginatorIntegrationTest extends DbUnitTestBase {


	private GenericDao<PetClinicInquiry, Long> petClinicInquiryDao;

	private GenericDao<PetClinicLead, Long> petClinicLeadDao;

	private FilteringPaginator paginator;
	

	@Override
	public String getDataSetXml() {
		return "data/Employee.xml";
	}
	
	@BeforeClass (dependsOnGroups={"initPersist"})
	public void setup() {
		
		petClinicInquiryDao.getClass();
		DaoFilteringPagingDataSource<PetClinicLead, Long> dataSource = new DaoFilteringPagingDataSource<PetClinicLead, Long>();
		dataSource.setDao(petClinicLeadDao);
		
		paginator = new FilteringPaginator(dataSource, PetClinicLead.class);
		
		
        paginator.addFilterableEntityJoin(PetClinicInquiry.class, //Class we are joining
        		"PetClinicInquiry", //Entity name
        		"inquiry", //
        		new String []{"anotherProp"}, //Array of property names we want to join to.
        		"o.inquiry"); //How to join to the PetClinicLead
        
        paginator.filter();
        
        for (int index = 0; index < 10; index ++) {
	        PetClinicLead lead = new PetClinicLead();
	        lead.setName("testLead" + index);
	        PetClinicInquiry inquiry = new PetClinicInquiry();
	        inquiry.setName("testInquiry" + index);
	        inquiry.setAnotherProp("testInquiryAP" + index);
	        lead.setInquiry(inquiry);
	        petClinicInquiryDao.store(inquiry);
	        petClinicLeadDao.store(lead);
        }
        
        
	}

	@AfterClass 
	public void teardown() {
		try {
			List<PetClinicInquiry> inqueries = petClinicInquiryDao.find(Comparison.startsLike("name", "testInquiry"));
			List<PetClinicLead> leads = petClinicLeadDao.find(Comparison.startsLike("name", "testLead"));
			
			petClinicLeadDao.delete(leads);
			petClinicInquiryDao.delete(inqueries);
		} catch(Exception ex) {
			
		}
	}
    
	@SuppressWarnings("unchecked")
	@Test (groups="reads")
    public void testGettingFirstPage() {
		System.out.println("----------------------------------------------------");
		Map<String, FilterableProperty> filterableProperties = paginator.getFilterableProperties();
		FilterableProperty filterableProperty = filterableProperties.get("name");
		filterableProperty.getComparison().setValue("testLead");
		filterableProperty.getComparison().enable();
		List<Object []> page = (List<Object []>) paginator.getPage();
		for (Object [] row : page) {
			PetClinicLead lead = (PetClinicLead) row[0];
			PetClinicInquiry inquiry = (PetClinicInquiry) row[1];
			assert lead.getName().startsWith("testLead");
			assert inquiry.getName().startsWith("testInquiry");
		}
		System.out.println("----------------------------------------------------");
		assert page != null : "page was not null";
		assert page.size() > 0 : "page size was greater than 0";
    }


	public void setPetClinicInquiryDao(
			GenericDao<PetClinicInquiry, Long> petClinicInquiryDao) {
		this.petClinicInquiryDao = petClinicInquiryDao;
	}


	public void setPetClinicLeadDao(GenericDao<PetClinicLead, Long> petClinicLeadDao) {
		this.petClinicLeadDao = petClinicLeadDao;
	}
	
}
