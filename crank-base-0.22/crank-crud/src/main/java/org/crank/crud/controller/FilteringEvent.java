package org.crank.crud.controller;

import org.crank.crud.criteria.Group;

import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * User: Rick
 * Date: Oct 4, 2007
 * Time: 3:59:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class FilteringEvent extends EventObject {
    private Group group;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public FilteringEvent(Object source, Group group) {
        super(source);
        this.group = group;
    }
}
