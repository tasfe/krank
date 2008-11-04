package org.crank.crud.controller;

import java.util.Date;

enum EmployeeType  {
    HOURLY, SALARY;
}

public class Employee {

    String firstName;
    Date dob;
    EmployeeType type;
    short age;
    int empId;
    Float avg;


    public Employee() {

    }
    
    public Employee(String firstName, Date dob, EmployeeType type, short age, int empId, Float avg) {
        this.firstName = firstName;
        this.dob = dob;
        this.type = type;
        this.age = age;
        this.empId = empId;
        this.avg = avg;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public Date getDob() {
        return dob;
    }
    public void setDob(Date dob) {
        this.dob = dob;
    }
    public EmployeeType getType() {
        return type;
    }
    public void setType(EmployeeType type) {
        this.type = type;
    }
    public short getAge() {
        return age;
    }
    public void setAge(short age) {
        this.age = age;
    }
    public int getEmpId() {
        return empId;
    }
    public void setEmpId(int empId) {
        this.empId = empId;
    }
    public Float getAvg() {
        return avg;
    }
    public void setAvg(Float avg) {
        this.avg = avg;
    }



}
