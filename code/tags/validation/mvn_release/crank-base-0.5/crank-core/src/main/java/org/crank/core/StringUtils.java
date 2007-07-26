package org.crank.core;

import java.util.Arrays;
import java.util.List;

public class StringUtils {
    public static List<String> split(String str) {
        return Arrays.asList( str.split( "," ) );
    }    
}
