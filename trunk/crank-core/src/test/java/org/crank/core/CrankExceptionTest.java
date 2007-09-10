package org.crank.core;

import junit.framework.TestCase;

public class CrankExceptionTest extends TestCase {


    public void testPrintStackTrace() {
        try {
            try {
                throw new RuntimeException("YO");
            } catch (Exception ex) {
                throw new CrankException(ex);
            }
        } catch (CrankException ex) {
            ex.printStackTrace();
        }

        try {
           throw new CrankException();
        } catch (CrankException ex) {
            ex.printStackTrace();
        }
    
    }

}
