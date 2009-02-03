package com.arcmind.codegen
/**
 * Created by IntelliJ IDEA.
 * User: richardhightower
 * Date: Feb 2, 2009
 * Time: 4:04:25 PM
 * To change this template use File | Settings | File Templates.
 */

public class ExceptionCategoryTest extends GroovyTestCase  {

    public void someMethodThrowsException() {

        throw new Exception ("I love groovy")

    }


    String messageProduced;
    int count = 0

    public void printerMethod (String message) {
        if (count == 0){
            this.messageProduced = message;
        }
        count ++
    }


    public void testCategory () {

        use (StringCategory,ExceptionCategory) {
            try {
                someMethodThrowsException();
            }  catch (Exception ex) {
                  println (ex.message)
                  ex.printMe("I love Java", this.&printerMethod);
            }
        }
        assert messageProduced == "I love Java I love groovy"
    }


}