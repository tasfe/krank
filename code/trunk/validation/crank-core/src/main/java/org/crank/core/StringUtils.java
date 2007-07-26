package org.crank.core;

import java.util.Arrays;
import java.util.List;

public class StringUtils {
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
