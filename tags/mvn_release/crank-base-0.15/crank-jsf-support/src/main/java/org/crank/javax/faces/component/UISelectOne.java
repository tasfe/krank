/*
 * $Id: UISelectOne.java,v 1.54 2007/01/29 07:56:08 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package org.crank.javax.faces.component;


import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;


/**
 * <p><strong>UISelectOne</strong> is a {@link UIComponent} that represents
 * the user's choice of zero or one items from among a discrete set of
 * available options.  The user can modify the selected value.  Optionally,
 * the component can be preconfigured with a currently selected item, by
 * storing it as the <code>value</code> property of the component.</p>
 *
 * <p>This component is generally rendered as a select box or a group of
 * radio buttons.</p>
 *
 * <p>By default, the <code>rendererType</code> property is set to
 * "<code>javax.faces.Menu</code>".  This value can be changed by
 * calling the <code>setRendererType()</code> method.</p>
 */

public class UISelectOne extends javax.faces.component.UISelectOne {



    /**
     * <p>Create a new {@link UISelectOne} instance with default property
     * values.</p>
     */
    public UISelectOne() {

        super();
        setRendererType("javax.faces.Menu");

    }


    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    // ------------------------------------------------------ Validation Methods


    /**
     * <p>In addition to the standard validation behavior inherited from
     * {@link UIInput}, ensure that any specified value is equal to one of
     * the available options.  Before comparing each option, coerce the 
     * option value type to the type of this component's value following
     * the Expression Language coercion rules.  If the specified value is 
     * not equal to any of the options,  enqueue an error message
     * and set the <code>valid</code> property to <code>false</code>.</p>
     *
     * @param context The {@link FacesContext} for the current request
     *
     * @param value The converted value to test for membership.
     *
     * @throws NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    protected void validateValue(FacesContext context, Object value) {

        // Skip validation if it is not necessary
        //super.validateValue(context, value);

//        if (!isValid() || (value == null)) {
//            return;
//        }

        // Ensure that the value matches one of the available options
//        boolean found = matchValue(value, new SelectItemsIterator(this));

        
        // Enqueue an error message if an invalid value was specified
//        if (!found) {
//            FacesMessage message =
//                MessageFactory.getMessage(context, INVALID_MESSAGE_ID,
//                     MessageFactory.getLabel(context, this));
//            context.addMessage(getClientId(context), message);
//            setValid(false);
//        }
    }


    // --------------------------------------------------------- Private Methods


//    /**
//     * <p>Return <code>true</code> if the specified value matches one of the
//     * available options, performing a recursive search if if a
//     * {@link SelectItemGroup} instance is detected.</p>
//     *
//     * @param value {@link UIComponent} value to be tested
//     * @param items Iterator over the {@link SelectItem}s to be checked
//     */
//    private boolean matchValue(Object value, Iterator items) {
//        return (true);
//    }




}
