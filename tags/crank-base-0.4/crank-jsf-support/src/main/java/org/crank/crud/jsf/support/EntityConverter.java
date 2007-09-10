package org.crank.crud.jsf.support;

import java.io.Serializable;
import java.util.Map;

import org.crank.core.CrankContext;
import org.crank.core.ObjectRegistry;
import org.crank.crud.GenericDao;
import org.crank.crud.controller.CrudManagedObject;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;


/**
 * Converts an integer string into some object.
 *
 * @author Rick Hightower
 */
public class EntityConverter implements Converter, Serializable {
    /**
     * DAO crud to look up Entity based on id.
     */
    private GenericDao dao;

    /**
     * Name of id property. Defaults to "id".
     */
    private String idPropertyName = "id";

    /**
     * Class of id. Defaults to java.lang.Long.
     */
    private Class idType = Long.class;
    
    /**
     *
     */
    private CrudManagedObject managedObject;

    /**
     * Is the id primitive. Defaults to false.
     */
    private boolean primitive = false;


    /**
     * Converts the value into an entity.
     * The type of the entity depends on the type that the daoCrud returns.
     *
     * @param facesContext current faces context
     * @param component current component
     * @param value current value submitted from user.
     *
     * @return An Entity
     *
     */
    @SuppressWarnings("unchecked")
    public Object getAsObject(final FacesContext facesContext, final UIComponent component, final String value) {
        Serializable entityId = CrudUtils.getIdObject(value, this.idType);
        if (dao == null) {
            ObjectRegistry objectRegistry = CrankContext.getObjectRegistry();
            Map<String, GenericDao> maps = (Map<String, GenericDao>) objectRegistry.getObject( "converters" );
            dao = maps.get( managedObject.getName() );
        }
        return dao.read(entityId);            
    }

    /**
     * Converts the entity to a String.
     *
     * @param facesContext current faces context
     * @param component current component
     * @param value current value of the entity
     *
     * @return value converted to string.
     *
     */
    public String getAsString(final FacesContext facesContext, final UIComponent component,
        final Object value) {
        if (value == null) {
            return "";
        }

        BeanWrapper bwValue = new BeanWrapperImpl(value);

        if (value instanceof String) {
            return value.toString();
        }

        return bwValue.getPropertyValue(idPropertyName).toString();
    }

    /**
     * Exposed for Spring to do injection.
     *
     * @param aDaoCrud DAOCrud used to look up Entity.
     */
    public void setDao(final GenericDao aDaoCrud) {
        this.dao = aDaoCrud;
    }

    /**
     * Is the id primitive or an object wrapper?
     * @return if the id is primitive.
     */
    public boolean isPrimitive() {
        return primitive;
    }

    /**
     * Is the id primitive or an object wrapper?
     * @param aPrimitive set to this.
     */
    public void setPrimitive(final boolean aPrimitive) {
        this.primitive = aPrimitive;
    }

   /**
    * @param aIdPropertyName idPropertyName
    */
   public void setIdPropertyName(final String aIdPropertyName) {
        this.idPropertyName = aIdPropertyName;
   }

    /**
     * Class type of id.
     * @param aClassTypeOfId Class type of id.
     */
    public void setIdType(final Class aClassTypeOfId) {
        idType = aClassTypeOfId;
    }

    public CrudManagedObject getManagedObject() {
        return managedObject;
    }

    public void setManagedObject( CrudManagedObject managedObject ) {
        this.managedObject = managedObject;
    }

}
