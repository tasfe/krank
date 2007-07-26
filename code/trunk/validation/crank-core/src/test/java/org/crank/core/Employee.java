package org.crank.core;

import org.crank.annotations.ErrorHandler;


public class Employee {
    int age;
    
    @ErrorHandler
    long iq;

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
