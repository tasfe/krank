package org.crank.validation;

import java.util.List;

import org.crank.annotations.design.ExtentionPoint;

/**
 * ValidatorMetaDataReader is an extention point for classes that need
 * to read validation meta-data. 
 * 
 * There are currently two implmentations (planned) for this.
 * 
 * One implementation reads the meta-data from a properties file.
 * The other implementation reads the data from Java 5 Annotation. 
 * @author Rick Hightower
 *
 */
@ExtentionPoint
public interface ValidatorMetaDataReader {
	
	public List<ValidatorMetaData> readMetaData(Class<?> clazz, String propertyName);

}