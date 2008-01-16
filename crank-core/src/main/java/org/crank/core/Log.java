package org.crank.core;

import java.util.HashMap;
import java.util.Map;


public class Log {
    
    private static Map<Class<?>, Log> logMap = new HashMap<Class<?>, Log>();
    
    protected Class<?> clasz;
    
    public void info(String message) {
        System.out.println(message);
    }
    public void debug(String message) {
        System.out.println(message);
    }
    public void warn(String message) {
        System.out.println(message);
    }
    public void error(String message) {
        System.err.println(message);
    }
    public void fatal(String message) {
        System.err.println(message);
    }
    
    
    public static Log getLog(Class<?> clazz) {
        Log log = logMap.get(clazz);
        if (log !=null) {
            return log;
        }
        try {
                Class<?> logClass = Class.forName(CrankConstants.LOG);
                log = (Log) logClass.newInstance();
                log.setClasz(clazz);
                logMap.put(clazz,log);
                return log;
        } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
        }
    }
    
    protected void setClasz(Class<?> clasz) {
        this.clasz = clasz;
    }
    
    protected void init () {
        
    }
    public void handleExceptionError(String message, Exception ioe) {
        System.err.println(" AN EXCEPTION OCCURRED: " + ioe.getMessage());
        ioe.printStackTrace(System.err);
    }
    public void handleExceptionFatal(String message, Exception ioe) {
        System.err.println(" AN EXCEPTION OCCURRED: " + ioe.getMessage());
        ioe.printStackTrace(System.err);
    }
    public void handleExceptionWarn(String message, Exception ioe) {
        System.out.println(" AN EXCEPTION OCCURRED: " + ioe.getMessage());
        ioe.printStackTrace(System.out);
    }
    public void handleExceptionInfo(String message, Exception ioe) {
        System.out.println(" AN EXCEPTION OCCURRED: " + ioe.getMessage());
        ioe.printStackTrace(System.out);
    }
    

}
