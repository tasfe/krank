package org.crank.crud.controller;

/**
 * Created by IntelliJ IDEA.
 * User: Rick
 * Date: Oct 4, 2007
 * Time: 4:05:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FilteringListener {

    void beforeFilter(FilteringEvent fe);
    void afterFilter(FilteringEvent fe);    
}
