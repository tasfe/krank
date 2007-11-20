package org.crank.crud.hibernate;

import java.sql.Types;

/**
 * @see http://forum.hibernate.org/viewtopic.php?p=2324183#2324183
 */
public class HSQLDialect_HHH_1598 extends org.hibernate.dialect.HSQLDialect { 

    public HSQLDialect_HHH_1598() { 
        super(); 
        registerColumnType(Types.BIT, "boolean"); 

        // Assert that the new type is registered correctly. 
        if (!"boolean".equals(getTypeName(Types.BIT))) { 
            throw new IllegalStateException("Failed to register HSQLDialect " 
                    + "column type for Types.BIT to \"boolean\"."); 
        } 
    } 
} 
