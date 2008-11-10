package org.crank.core;

import org.crank.annotations.ErrorHandler;


public class Employee extends Person {
    int age;
    
    @ErrorHandler
    long iq;
    
    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress( Address address ) {
        this.address = address;
    }

    public long getIq() {
        return iq;
    }

    @ErrorHandler (messageDetail="foo")
    public void setIq( long iq ) {
        this.iq = iq;
    }

    public int getAge() {
        return age;
    }

    @ErrorHandler (messageDetail="foo age")
    public void setAge( int age ) {
        this.age = age;
    }

}
