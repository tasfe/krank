package org.crank.validation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



/**
 * 
 * <p>
 * A collection of messages.
 * </p>
 * @author Rick Hightower
 */
public class ValidatorMessages implements Serializable, ValidatorMessageHolder, Iterable<ValidatorMessage> {
    private List<ValidatorMessage> messages=new ArrayList<ValidatorMessage>();

    public Iterator<ValidatorMessage> iterator() {
        return this.messages.iterator();
    }

    public void add(ValidatorMessage message) {
        messages.add(message);
    }
}
