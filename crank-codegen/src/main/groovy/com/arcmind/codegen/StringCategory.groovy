/**
 * 
 */
package com.arcmind.codegen



/**
 * @author richardhightower
 *
 */
public class StringCategory{

    public static String cap(String self) {
        try {
            return self[0].toUpperCase() + self[1..-1]
        }   catch (Exception ex){
            return self
        }
    }

    public static String capAndLower(String self) {
        try {
            return self[0].toUpperCase() + self[1..-1].toLowerCase()
        }   catch (Exception ex){
            return self
        }
    }

	public static String unCap(String self) {
        try {
            return self[0].toLowerCase() + self[1..-1]
        }   catch (Exception ex){
            return self
        }
	}
	
	public static boolean isAllUppers(String self) {

		boolean notAllUpper = false
		self.each {
		    if (!Character.isUpperCase(it.toCharacter())) {
		        notAllUpper = true
		    }
		}

		return !notAllUpper
	}
	
}
