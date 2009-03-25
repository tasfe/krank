package org.crank.validation.readers;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;
import java.util.ArrayList;
import java.util.List;

import org.crank.validation.ValidatorMetaData;
import org.crank.validation.ValidatorMetaDataReader;

public class ChainValidatorMetaDataReaderTest {
	List <ValidatorMetaDataReader> chain;
	ChainValidatorMetaDataReader chainReader;
	
	ValidatorMetaDataReader reader1 = new ValidatorMetaDataReader() {

		public List<ValidatorMetaData> readMetaData(Class<?> clazz, String propertyName) {
			List<ValidatorMetaData> list = new ArrayList<ValidatorMetaData>();
			ValidatorMetaData data = new ValidatorMetaData();
			data.setName("d1");
			data.getProperties().put("v1", 1);
			list.add(data);
			ValidatorMetaData data2 = new ValidatorMetaData();
			data2.setName("d2");
			data2.getProperties().put("v2", 1);
			list.add(data2);

			return list;
		}
		
	};
	ValidatorMetaDataReader reader2 = new ValidatorMetaDataReader() {

		public List<ValidatorMetaData> readMetaData(Class<?> clazz, String propertyName) {
			List<ValidatorMetaData> list = new ArrayList<ValidatorMetaData>();
			ValidatorMetaData data = new ValidatorMetaData();
			data.setName("d1");
			data.getProperties().put("v1", 2);
			list.add(data);
			return list;
		}
		
	};
	
	
	@BeforeMethod
	protected void setUp() throws Exception {
		chainReader = new ChainValidatorMetaDataReader();
		chain = new ArrayList<ValidatorMetaDataReader>();
		chain.add(reader1);
		chain.add(reader2);
		chainReader.setChain(chain);
	}


	@Test()
	public void testReadMetaData() {
		List<ValidatorMetaData> list = chainReader.readMetaData(null, null);
		assertEquals("d1",list.get(0).getName());
		assertEquals("d2",list.get(1).getName());
		assertEquals(2,list.get(0).getProperties().get("v1"));
	}

}
