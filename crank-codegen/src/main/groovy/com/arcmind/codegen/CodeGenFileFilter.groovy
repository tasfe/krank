/**
 * 
 */
package com.arcmind.codegen

import javax.swing.*



/**
 * @author richardhightower
 *
 */
public class CodeGenFileFilter  extends javax.swing.filechooser.FileFilter {
	
	private static String TYPE_UNKNOWN = "Type Unknown";
	private static String HIDDEN_FILE = "Hidden File";
	
	Hashtable filters = [:]
    String description
	String fullDescription
	boolean useExtensionsInDescription = true
	
	
	public void addExtension(String extension) {
		filters[extension.toLowerCase()]=this
		fullDescription = null;
	}
	
	
	public String getDescription() {
		if(fullDescription == null) {
			if(description == null || !useExtensionsInDescription) {
				StringBuilder builder = new StringBuilder()
				builder << description==null ? "(" : description + " (";
				for (String ext : extentions) {
					builder << "."
					builder << ext
					builder << ", ."
				}
				builder << ")"
				fullDescription = builder.toString()
			} else {
				return description
			}
		}
		return fullDescription
	}
	
	
	public boolean accept(File f) {
		
		if(f.isDirectory()) {
			return true;
		}
		String extension = getExtension(f);
		
		if(extension != null && filters[extension] != null) {
				return true;
		}
		return false
	}

	public String getExtension(File f) {
		if(f != null) {
			String filename = f.getName();
				int lastIndex = filename.lastIndexOf('.')
				if(lastIndex>0 && lastIndex < filename.length()-1) {
						return filename.substring(lastIndex+1).toLowerCase()
				}
		}
		return null;
	}
}
