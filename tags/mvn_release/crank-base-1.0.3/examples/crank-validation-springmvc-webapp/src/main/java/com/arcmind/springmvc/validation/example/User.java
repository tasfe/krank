package com.arcmind.springmvc.validation.example;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

//import org.crank.annotations.validation.CommonEmail;
import org.crank.annotations.validation.Currency;
import org.crank.annotations.validation.Email;
import org.crank.annotations.validation.Equals;
import org.crank.annotations.validation.LongRange;
import org.crank.annotations.validation.Number;
import org.crank.annotations.validation.Phone;
import org.crank.annotations.validation.Zip;
//import org.crank.annotations.validation.Loop;
import org.crank.annotations.validation.Regex;
import org.crank.annotations.validation.Required;

public class User implements Serializable {
    
    private String email;

    @Required    
    private String firstName;
    private String lastName;
    private String userName;
    private int age;
    private Date birthDate;
    private String zip;
    private String password;
    private String password2;
    private BigDecimal salary;
    private float gpa;
    private String phone;
    
    public String getPhone() {
        return phone;
    }

    @Phone
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public float getGpa() {
        return gpa;
    }

    @Number
    public void setGpa(float gpa) {
        this.gpa = gpa;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    @Currency
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public User () {
        //ystem.out.println("USER ............ ");
    }
    
    public int getAge() {
        return age;
    }
    
    @LongRange (min=1, max=250)
    public void setAge(int age) {
        this.age = age;
    }
    
    public Date getBirthDate() {
        return birthDate;
    }
    
    @Required 
    @org.crank.annotations.validation.Date
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    
    public String getEmail() {
        return email;
    }
    
//    @CommonEmail (detailMessage="not valid",
//        summaryMessage="Email is not valid")
    @Email (detailMessage="not valid",
          summaryMessage="Email is not valid")
    public void setEmail(String email) {
        this.email = email;
    }
    
    
    public String getFirstName() {
        return firstName;
    }
    


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    
    //@Loop (detailMessage="detail loop", summaryMessage="summary loop")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getUserName() {
        return userName;
    }

    @Regex (match="[a-zA-Z]{1}[a-zA-Z0-9_]{5,}", 
            detailMessage="Must start with a letter and then contain" +
                    " at least 5 letters, numbers and underscores",
            summaryMessage="User Name must match a certain pattern")    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String toString() {
        return " First Name " + firstName + 
               " Last Name " + lastName + 
               " User Name " + userName + 
               " CommonEmail " + email + 
               " Age " + age + 
               " Birth Date " + birthDate;
    }

    public String getZip() {
        return zip;
    }

    @Zip
    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    @Equals (compareToProperty="password")
    public void setPassword2(String password2) {
        this.password2 = password2;
    }

}
