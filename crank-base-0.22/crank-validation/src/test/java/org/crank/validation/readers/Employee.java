package org.crank.validation.readers;

import org.crank.annotations.validation.LongRange;
import org.crank.annotations.validation.Range;
import org.crank.annotations.validation.Required;

public class Employee {
    int age;
    long iq;

    public long getIq() {
        return iq;
    }

    @Required
    @LongRange( min = 1L, max = 100L )
    public void setIq( long iq ) {
        this.iq = iq;
    }

    public int getAge() {
        return age;
    }

    @Required
    @Range( min = "1", max = "10" )
    public void setAge( int age ) {
        this.age = age;
    }

}
