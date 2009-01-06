package com.arcmind.codegen



/** Adds methods to the String class that are important for code generation.
 *  @author richardhightower
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

	public static String plural(String self) {
		return self.endsWith('s') ? self + "es" : self + "s"
	}

	public static String singular(String self) {
		if (self.endsWith("es")) {
			return self[0..-3]
		} else if (self.endsWith("s")) {
			return self[0..-2]
		} else {
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
