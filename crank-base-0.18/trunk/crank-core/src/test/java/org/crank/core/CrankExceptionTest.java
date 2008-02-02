package org.crank.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

public class CrankExceptionTest {


    @Test
	public void testPrintStackTrace() {
        PrintStream oldErr = System.err;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(256);
        System.setErr(new PrintStream(baos));
        try {
            try {
                throw new RuntimeException("YO");
            } catch (Exception ex) {
                throw new CrankException(ex);
            }
        } catch (CrankException ex) {
            ex.printStackTrace();
        }
        AssertJUnit.assertTrue(baos.toString().contains("YO"));
        baos = new ByteArrayOutputStream(256);
        System.setErr(new PrintStream(baos));
        try {
           throw new CrankException();
        } catch (CrankException ex) {
            ex.printStackTrace();
        }
        AssertJUnit.assertTrue(baos.toString().contains("CrankException"));
        System.setErr(oldErr);
    }

}
