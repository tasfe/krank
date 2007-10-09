package org.crank.crud.test.model;

import javax.persistence.Embeddable;

@Embeddable
public class Address {
    
    private String line1;
    private String line2;
    private String zipCode;
    public String getLine1() {
        return line1;
    }
    public void setLine1( String line1 ) {
        this.line1 = line1;
    }
    public String getLine2() {
        return line2;
    }
    public void setLine2( String line2 ) {
        this.line2 = line2;
    }
    public String getZipCode() {
        return zipCode;
    }
    public void setZipCode( String zipCode ) {
        this.zipCode = zipCode;
    }

}
