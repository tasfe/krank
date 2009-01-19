/**
 * 
 */
package com.arcmind.codegen

import javax.swing.JList


/**
 * @author Alec Kotovich
 *
 */
public class RootDirValidator{

	String rootDir
	private List <String> data = []
	private List <String> criteria = 
		["/pom.xml",
		 "/src",
		 "/src/main",
		 "/src/main/java",
		 "/src/main/webapp",
		 "/src/main/webapp/WEB-INF",
		 "/src/main/webapp/WEB-INF/faces-config.xml"]
	
	
	boolean validate(){
		
		int fehler = 0
		criteria.each(
				{
					exists(rootDir+it) ? 
							(data << getName(rootDir+it, true)) :
							(data << getName(rootDir+it, false))
					
					if (!exists(rootDir+it)) ++fehler						
				}
		)
		return !fehler
	}
	
	private boolean exists(String path) {
		(new File(path)).exists()
	}
	
	private getName(path, trueorFalse){
		trueorFalse ? 
				(new File(path)).getName()+" success" :
				(new File(path)).getName()+" failure"
	}
	
	
}
