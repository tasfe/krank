package com.arcmind.codegen
/**
 * Created by IntelliJ IDEA.
 * User: richardhightower
 * Date: Feb 2, 2009
 * Time: 3:54:38 PM
 * To change this template use File | Settings | File Templates.
 */

public class ExceptionCategory {

    public static void printMe(Exception ex, String message, Closure closure) {
        closure "${message} ${ex.message}"
        ByteArrayOutputStream bos = new ByteArrayOutputStream()
        PrintStream stream = new PrintStream(bos)
        ex.printStackTrace(stream)
        closure bos.toString()
    }

}