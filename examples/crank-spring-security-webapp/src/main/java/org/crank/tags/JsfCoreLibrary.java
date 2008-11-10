package org.crank.tags;

import com.sun.facelets.tag.AbstractTagLibrary;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by IntelliJ IDEA.
 * User: kregester
 * Date: Oct 24, 2008
 * Time: 2:08:24 PM
 * Package: org.crank.tags
 * Copyright Vantage Media 2008
 */
public final class JsfCoreLibrary extends AbstractTagLibrary {
    /** Namespace used to import this library in Facelets pages  */
    public static final String NAMESPACE = "http://www.crank.org/springsecurity/tags";

    /**  Current instance of library. */
    public static final JsfCoreLibrary INSTANCE = new JsfCoreLibrary();

    /**
     * Creates a new JsfCoreLibrary object.
     * iterates through all the methods on the current class using reflection,
     * It then adds all JsfCoreLibrary static methods using the inherited method addFunction().
     */
    public JsfCoreLibrary() {
        super(NAMESPACE);
        
        //Register tag <yournamespace:springSecurityAuthorize/> in the J;sfCoreLibrary
        this.addTagHandler("springSecurityAuthorize", SpringSecurityAuthorize.class);


        try {
            Method[] methods = SpringSecurityTagUtils.class.getMethods();

            for (int i = 0; i < methods.length; i++) {
                if (Modifier.isStatic(methods[i].getModifiers())) {
                    this.addFunction(methods[i].getName(), methods[i]);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}