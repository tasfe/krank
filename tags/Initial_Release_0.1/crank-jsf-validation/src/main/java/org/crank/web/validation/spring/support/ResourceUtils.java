package org.crank.web.validation.spring.support;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.core.io.Resource;

public class ResourceUtils {
    public static String convertResourceToString (Resource resource) {
        BufferedReader reader=null;
        StringBuilder builder = new StringBuilder(1000);
        try {
          try {  
            reader = 
                new BufferedReader(
                        new InputStreamReader(resource.getInputStream()));
            
            String line="";
            while ((line=reader.readLine())!=null){
                builder.append(line);
            }//while
            
          } finally {
              if (reader!=null) {
                  reader.close();
              }//if
          }//finally
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } //try catch
        return builder.toString();
    }//method
        
}

