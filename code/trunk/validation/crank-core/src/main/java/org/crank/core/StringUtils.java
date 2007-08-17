package org.crank.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** To expose as EL expression functions. */
public class StringUtils {
    public static boolean contains(String str, String substring) {
        return str.contains(substring);
    }    

    public static String replace(String str, String oldStr, String newStr) {
        return str.replace( oldStr, newStr );
    }    

    public static List<String> split(String str) {
        return Arrays.asList( str.split( "," ) );
    }    
    
    public static List<String> splitProperty(String str) {
    	
    	String prop1 = str.substring(0,str.indexOf("."));
    	String prop2 = str.substring(str.indexOf(".") + 1, str.length());
    	
    	List<String> results = new ArrayList();
    	
    	results.add(prop1);
    	results.add(prop2);
    	
    	return results;
        
    }    
    
    public static String unCapitalize (String string) {
        return string.substring(0, 1).toLowerCase() 
        + string.substring(1);        
    }
    public static String capitalize (String string) {
        return string.substring(0, 1).toUpperCase() 
        + string.substring(1);        
    }

}
