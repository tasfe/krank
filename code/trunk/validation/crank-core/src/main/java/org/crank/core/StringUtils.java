package org.crank.core;

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
    
    public static String unCapitalize (String string) {
        return string.substring(0, 1).toLowerCase() 
        + string.substring(1);        
    }
    public static String capitalize (String string) {
        return string.substring(0, 1).toUpperCase() 
        + string.substring(1);        
    }

}
