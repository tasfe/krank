package org.crank.core.spring.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import java.util.List;

import javax.faces.model.SelectItem;

import org.crank.core.PropertiesUtil;
import org.crank.crud.controller.CrudUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NotReadablePropertyException;

/**
 * Utility class for taking a list of Entites and turning that list into a list
 * of select items.
 * 
 * @author Rick Hightower
 */
@SuppressWarnings("unchecked")
public class SelectItemUtils {
    private PropertiesUtil propertiesUtil = new SpringBeanWrapperPropertiesUtil();
    /**
     * Name of id property. Defaults to "id".
     */
    private String idPropertyName = "id";

    /**
     * Name of label property. Defaults to "name".
     */
    private String labelPropertyName = "name";

    /**
     */
    private String optionalLabel = "select one";

    /**
     * Class of id. Defaults to java.lang.Long.
     */
	private Class classTypeOfId = Long.class;

    /**
     * Is the id primitive. Defaults to false.
     */
    private boolean primitive = false;

    public List createSelectItems( final Collection list, String idProperty, String labelProperty ) {
    	return createSelectItems(list, idProperty, labelProperty, false);
    }

    public List createSelectItems( final Collection list, String idProperty, String labelProperty, boolean optional ) {
        List selectItems = new ArrayList(list.size()); // new list of selectItems
        if (optional) {
        	selectItems.add(new SelectItem (-1L, this.optionalLabel));
        }
        for (Iterator iter = list.iterator(); iter.hasNext();) {
        	Object item = (Object) iter.next();
            String label = "?";
            String id;

            try {
                BeanWrapper entity = new BeanWrapperImpl( (Object)item );
                label = "" + entity.getPropertyValue( labelProperty );
                id = entity.getPropertyValue( idProperty ).toString();
            } catch (NotReadablePropertyException ex) {
	              label = CrudUtils.generateEnumLabelValue((String)item.toString());
	              id = (String)item.toString();
            }
            
            SelectItem selectItem = new SelectItem( id, label );
            selectItems.add( selectItem );
        }
        return selectItems;
    }

    /**
     * Utility method for taking a list of Entites and turning that list into a
     * list of select items.
     * 
     * @param list
     *            List of entites
     * 
     * @return list of SelectItems
     */
    public List createSelectItems( final Collection list ) {
        return createSelectItems( list, this.getIdPropertyName(), this.getLabelPropertyName(), false );
    }

	public List<SelectItem> createSelectItems(Collection list, boolean optional) {
		return createSelectItems( list, this.getIdPropertyName(), this.getLabelPropertyName(), optional );
	}
    
    
    /**
     * What it the type of the id class, defaults to java.lang.Long.
     * 
     * @return type of the id class.
     */
    public Class getClassTypeOfId() {
        return classTypeOfId;
    }

    /**
     * Class type of id.
     * 
     * @param aClassTypeOfId
     *            Class type of id.
     */
    public void setClassTypeOfId( final Class aClassTypeOfId ) {
        this.classTypeOfId = aClassTypeOfId;
    }

    /**
     * Is the id primitive or an object wrapper?
     * 
     * @return if the id is primitive.
     */
    public boolean isPrimitive() {
        return primitive;
    }

    /**
     * Is the id primitive or an object wrapper?
     * 
     * @param aPrimitive
     *            set to this.
     */
    public void setPrimitive( final boolean aPrimitive ) {
        this.primitive = aPrimitive;
    }

    /**
     * 
     * The name of the id property, defaults to "id".
     * 
     * @return the id of the value.
     */
    public String getIdPropertyName() {
        return idPropertyName;
    }

    /**
     * 
     * 
     * @param aIdPropertyName
     *            idPropertyName
     */
    public void setIdPropertyName( final String aIdPropertyName ) {
        this.idPropertyName = aIdPropertyName;
    }

    /**
     * 
     * 
     * @return label property name.
     */
    public String getLabelPropertyName() {
        return labelPropertyName;
    }

    /**
     *
     *
     * @param aLabelPropertyName
     *            labelPropertyName
     */
    public void setLabelPropertyName( final String aLabelPropertyName ) {
        this.labelPropertyName = aLabelPropertyName;
    }

    public PropertiesUtil getPropertiesUtil() {
        return propertiesUtil;
    }

    public void setPropertiesUtil( PropertiesUtil propertiesUtil ) {
        this.propertiesUtil = propertiesUtil;
    }

    

}
