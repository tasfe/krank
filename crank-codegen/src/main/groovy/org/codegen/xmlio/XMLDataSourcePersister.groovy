/**
 * 
 */
package org.codegen.xmlio

import groovy.xml.MarkupBuilder
import org.codegen.model.JDBCSettings
import org.codegen.model.JDBCSettings

/**
 * @author Rick Hightower
 *
 */
public class XMLDataSourcePersister{
	List<JDBCSettings> jdbcSettings
	File outputDir = new File("./target")
	String fileName = "dataSource.xml"
	boolean debug
    boolean trace
	
	/* Read our data sources from XML. */
	void read() {
		def dataSources = new XmlSlurper().parse(new File (outputDir, fileName))
        readDataSources(dataSources)
	}
	
	void readDataSources(dataSources) {
    	if (debug) println "Reading data sources"
    	jdbcSettings = []
    	
    	if (dataSources.datasource) {
    		
	    	dataSources.datasource.each {ds ->
	    		JDBCSettings jdbcSetting = 
	    			new JDBCSettings(url: ds.@url,
	    							 userName : ds.@userName,
	    							 password: ds.@password,
	    							 driver: ds.@driver)
	    		jdbcSettings << jdbcSetting
			}//jdbcSettings
			
    	}
	}
	
	/* Persist our datasources to the XML file. */
	void persist() {
		List <JDBCSettings> _jdbcSettings = jdbcSettings
		BufferedWriter bWriter = new File (outputDir, fileName).newWriter()
		bWriter.withWriter {writer ->
            def xmlDocument = new MarkupBuilder(writer)
            xmlDocument.jdbcSettings(){
            	_jdbcSettings.each {
            		'datasource'(
            				url: it.url,
            				userName: it.userName,
            				password: it.password,
            				driver: it.driver ) 
            	}
            }
		}
		
	}	
	
}
