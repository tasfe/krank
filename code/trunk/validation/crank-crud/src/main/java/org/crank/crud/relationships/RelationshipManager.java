package org.crank.crud.relationships;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;


public class RelationshipManager {

    /**
     * @CONFIG
     * The class of the entity. Used to create new instances of the entity.
     */
    private Class entityClass;

    /**
     * @CONFIG @JSF_EXPRESSION
     * The name of the entity. This gets used to calculate the button names. And
     * the add method name on the parent object, i.e., add${entityName} is the default
     * add method that will get called on the parent.
     */
    private String entityName;
    
    /**
     * The name of the id property. For example, pet.getId().
     * If the property is indexed, this will be the name of the index property.
     */
    private String idPropertyName = "id";
    
    /**
     * @CONFIG
     * The type of id, used to retrieve the object from the database for
     * read operations, i.e., the user clicks on the object out of the list.
     */
    //private Class idType = Long.class;
    
    
    /**
     * @CONFIG or @RUNTIME
     * This allows developers to overide the addMethod of the parent.
     * This will default to add${entityName} if not configured by the developer.
     */
    private String addToParentMethodName = null;

    /**
     * @CONFIG or @RUNTIME
     * This allows developers to overide the removeMethod of the parent.
     * This will default to remove${entityName} if not configured by the developer.
     */
    private String removeFromParentMethodName = null;

    /**
     * @CONFIG or @RUNTIME
     * This allows developers to overide the getListMethod of the parent.
     * This will default to get${entityName}s if not configured by the developer.
     */
    private String listFromParentMethodName = null;

    /**
     * @RUNTIME
     * The cached version of the add method so we don't keep looking it up.
     */
    private Method addToParentMethod = null;

    /**
     * @RUNTIME
     * The cached version of the add method so we don't keep looking it up.
     */
    private Method removeFromParentMethod = null;
    
    /**
     * @CONFIG
     * Is the relationship some sort of bag, i.e., it can't be indexed, set or bag.
     * List that is a bag
     * or a Set. Set is automatically bag like.
     */
    private boolean relationshipIsBag = false;
    
    /**
     * Hold the last value of the getListMethod. Cached for speed.
     * This is used to grab the list from the parent.
     */
    private Method listMethod;


    /**
     * Used to index hashmaps out of the list.
     */
    private Class keyType = String.class;
    
    /**
     * Read entity from parent.
     * @param indexValue The value of the index into the parent's children collection.
     */
    public Serializable readEntityFromParent(Object parentObject, String indexValue) {
        try {

            /* Get the list from the parent. */
            Object listTypeThing = retrieveChildCollectionFromParentObject(parentObject);

            if ((listTypeThing instanceof Set) ||
                    (listTypeThing instanceof List && relationshipIsBag)) {
                return readEntityFromParentBagOrSet(indexValue, listTypeThing);
            } else if (listTypeThing instanceof List && !relationshipIsBag) {
                return readEntityFromParentList(indexValue, listTypeThing);
            } else if (listTypeThing instanceof Array) {
                return readEntityFromParentArray(indexValue, listTypeThing);
            } else if (listTypeThing instanceof Map) {
                return readEntityFromParentMap(indexValue, listTypeThing);
            }
            
        } catch (Exception exp) {
            throw new RuntimeException("Unable to get the method that " +
                    "retrieves the list/map/set of objects from parent object.", exp);
        }
        return null;
    }    
    
    /**
     * Read the value from a map.
     * If not String use the keyType (Class) and look up a JSF converter
     * for it.
     **/
    private Serializable readEntityFromParentMap(String indexValue, Object listTypeThing) {
        Map map = (Map) listTypeThing;
        if (keyType==String.class) {
            return (Serializable) map.get(indexValue);
        } else {
            throw new RuntimeException();
        }
    }
    
    private Serializable readEntityFromParentArray(String indexValue, Object listTypeThing) {
        Object [] objects = (Object[])listTypeThing;
        return (Serializable) objects[Integer.parseInt(indexValue)];
    }
    
    private Serializable readEntityFromParentList(String indexValue, Object listTypeThing) {
        if (indexValue.startsWith("ix--")){
            int index = Integer.parseInt(indexValue.substring(4));

            /* Read a value from a list. */
            List list = (List) listTypeThing;
            return (Serializable) list.get(index);
        } else {
            return getChildFromChildrenCollectionUsingId(indexValue, listTypeThing);
        }
    }
    
    private Serializable readEntityFromParentBagOrSet(String indexValue, Object listTypeThing) {
        /* Get object from Set or Bag. */

        if (indexValue.startsWith("hc--")){
            return getChildFromChildrenCollectionUsingHashCode(indexValue, listTypeThing);

        } else {
            return getChildFromChildrenCollectionUsingId(indexValue, listTypeThing);
        }
    }
    
    /** This method finds a child object in a children collection for Sets and Bags using the hashcode of the child.
     *  This is for children who are not in the persistence system yet, i.e., not in the database yet.
     * */
    private Serializable getChildFromChildrenCollectionUsingHashCode(String indexValue, Object listTypeThing) {
        /* Parse the hashcode out of the indexValue. */
        long hash = Long.parseLong(indexValue.substring(4));


        /** Iterate through the list and grab the id of the current child, if the
         *  id of the current child is equal to the id, then this is the object
         *  we are looking for.
         */
        Collection collection = (Collection) listTypeThing;
        Iterator iterator = collection.iterator();
        Serializable child = null;
        while(iterator.hasNext()){
            Object object = iterator.next();
            if (object.hashCode()==hash) {
                child = (Serializable) object;
                break;
            }
        }

        /**
         * This should not happen. If it does happen let the world no, send out an amber alert!
         */
        if (child==null) {
            throw new RuntimeException("Unable to find child object in parent's child collection using hashcode=" + hash);
        }
         return child;
    }
    
    
    /**
     * Get the child list from the parent, e.g., call owner.getPets() or department.getEmployees(), etc.
     * @param parent the parent object (Owner has pets... Owner is the parent).
     * @return The collection of children (The Owner's pets)
     * @throws Exception Some problem.
     */
    public Object retrieveChildCollectionFromParentObject(Object parent) {
        
        try {

        /* Look up the method to call based on reflection. */
        if (listMethod==null) {

            if (listFromParentMethodName == null) {
                listFromParentMethodName = "get" + entityName + "s";
            }
            listMethod = parent.getClass().getMethod(listFromParentMethodName,(Class[]) null);
        }

        /* Invoke the method and return the list type thing (it can be an
         * Array, List, Set or Map)
         */
        Object listTypeThing = listMethod.invoke(parent,(Object[])null);
        return listTypeThing;
        
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    /**
     * This method finds a child object in a children collection for Sets and Bags using the id of the child.
     * This is for children who are in the persistence system already, i.e., in the database already.
     * */
    private Serializable getChildFromChildrenCollectionUsingId(String indexValue, Object listTypeThing) {

        /** Get the current id from the index parameter that was passed. */
        Long id = Long.valueOf(indexValue);

        /** Iterate through the list and grab the id of the current child, if the
         *  id of the current child is equal to the id, then this is the object
         *  we are looking for.
         */
        Collection collection = (Collection) listTypeThing;
        Iterator iterator = collection.iterator(); //children.
        Serializable child = null; //current child.

        /**
         * Iterate through children looking for child with id of id passed.
         */
        while(iterator.hasNext()){
            Object object = iterator.next();
            BeanWrapper wrapper = new BeanWrapperImpl(object);
            Long idPropertyValue = (Long) wrapper.getPropertyValue(this.idPropertyName);
            if (id.equals(idPropertyValue)) {
                child = (Serializable) object;
                break;
            }
        }

        /**
         * This should not happen. If it does happen let the world no, send out an amber alert!
         */
        if (child==null) {
            throw new RuntimeException("Unable to find child object in parent's child collection using id=" + id);
        }
        return child;
    }
    
    /**
     * This adds a child to a parent using reflection.
     * It will use the addMethodName and reflection to invoke the addMethodName on the parent,
     * passing the child.
     *
     *  By default the addMethodName defaults to add${entityName}, however, you can inject
     *  a different addMethodName via Spring DI.
     *
     * @param parent parent
     * @param child child
     */
    @SuppressWarnings("unchecked")
    public void addToParent(Object parent, Object child) {

        try {
            /* The following does the equiv of owner.addPet(pet); */
            Class parentClass = parent.getClass();

            /* Initialize the addMethod name if needed. */
            if (addToParentMethodName==null) {
                addToParentMethodName = "add" + entityName;
            }
            /*
             * Initialize the addMethod if needed.
             * Look up the addMethod using reflection.
             */
            if (addToParentMethod==null) {
                addToParentMethod = parentClass.getMethod(addToParentMethodName , new Class[]{this.entityClass});
            }

            /*
             * Invoke the addMethod on parent passing the child as an argument.
             */
            addToParentMethod.invoke(parent, new Object[]{child});
        } catch (Exception ex) {
            /* If there are any problems throw a nested exception. */
            throw new RuntimeException(ex);
        }
    }
    
    @SuppressWarnings("unchecked")
    public void removeFromParent(Object parent, Object child) {

        try {
            /* The following does the equiv of owner.addPet(pet); */
            Class parentClass = parent.getClass();

            /* Initialize the removeMethod name if needed. */
            if (removeFromParentMethodName==null) {
                removeFromParentMethodName = "remove" + entityName;
            }
            /*
             * Initialize the removeMethod if needed.
             * Look up the removeMethod using reflection.
             */
            if (removeFromParentMethod==null) {
                removeFromParentMethod = parentClass.getMethod(removeFromParentMethodName, new Class[]{this.entityClass});
            }

            /*
             * Invoke the removeMethod on parent passing the child as an argument.
             */
            removeFromParentMethod.invoke(parent, new Object[]{child});
        } catch (Exception ex) {
            /* If there are any problems throw a nested exception. */
            throw new RuntimeException(ex);
        }
    }

    public void setAddToParentMethodName( String addToParentMethodName ) {
        this.addToParentMethodName = addToParentMethodName;
    }

    public void setEntityClass( Class entityClass ) {
        this.entityClass = entityClass;
        if (entityName == null) {
            entityName = entityClass.getSimpleName();
        }
    }

    public void setEntityName( String entityName ) {
        this.entityName = entityName;
    }

    public void setIdPropertyName( String idPropertyName ) {
        this.idPropertyName = idPropertyName;
    }

    public boolean isIndexed() {
        return !relationshipIsBag;
    }

    /** Get the object id. */
    public String getObjectId(Object parent, Object object) {
        try {
            Object listing = retrieveChildCollectionFromParentObject(parent);
            if (listing instanceof Map) {
                return getObjectParameterIdFromMap(object,(Map)listing);
            } else if (listing instanceof List && this.isIndexed()) { //List
                return getObjectParameterIdFromList(object, (List)listing);
            } else if ((listing instanceof List && !isIndexed()) || listing instanceof Set) { //Bag or Set
                return getObjectParameterIdFromBagOrSet(object, (Collection)listing);
            } else if (listing instanceof Array) {
                throw new IllegalStateException ("Array support not implemented yet");
            } else {
                throw new IllegalStateException ("Can't retrieve value unless from map, list or set");
            }
        } catch (Exception ex) {
            if (ex instanceof IllegalStateException) {
                throw ((RuntimeException)ex);
            } else {
                throw new RuntimeException(ex);
            }
        }
    }
    
    /** Get the object id. */
    private String getObjectParameterIdFromBagOrSet(Object object, Collection collection) {
        BeanWrapper wrapper = new BeanWrapperImpl(object);
        Object idValue =  wrapper.getPropertyValue(this.idPropertyName);

        if (idValue==null) {
            return "hc--" + object.hashCode();
        } else if (idValue instanceof String) {
            String strIdValue = (String) idValue;
            if (strIdValue.trim().equals("")) {
                return "hc--" + object.hashCode();
            } else {
                return strIdValue;
            }
        } else {
            return idValue.toString();
        }
    }
    
    /** Get the object id. */
    private String getObjectParameterIdFromList(Object object, List list) {


        BeanWrapper wrapper = new BeanWrapperImpl(object);
        Object idValue =  wrapper.getPropertyValue(this.idPropertyName);


        if (!(idValue instanceof Number || idValue == null) ) {
            throw new IllegalStateException("The value of index property is not a number");
        }

        Number number = (Number)idValue;
        

        if (number == null || number.longValue() <= 0) {
            int index = list.indexOf(object) == -1 ? list.size() : list.indexOf( object );
            return "ix--" + index;
        } else {
            return number.toString();
        }
    }

    /** Get the object id. */
    private String getObjectParameterIdFromMap(Object object, Map map) {

        Object idValue = null;

        if (!this.idPropertyName.equals("id")) {
            BeanWrapper wrapper = new BeanWrapperImpl(object);
            idValue =  wrapper.getPropertyValue(this.idPropertyName);

            if (keyType==String.class) {
                return idValue.toString();
            } else {
                return idValue.toString();
            }
        } else {
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                if (entry.getValue().equals(object)) {
                    idValue = entry.getKey();
                    break;
                }
            }
        }

        if (idValue == null) {
            throw new IllegalStateException("No index value found");
        }

        if (keyType==String.class) {
            return idValue.toString();
        } else {
            return idValue.toString();
        }
    }
    
    //    public void setIdType( Class idType ) {
//        this.idType = idType;
//    }

    public void setKeyType( Class keyType ) {
        this.keyType = keyType;
    }

    public void setListFromParentMethodName( String listFromParentMethodName ) {
        this.listFromParentMethodName = listFromParentMethodName;
    }

    public void setRelationshipIsBag( boolean relationshipIsBag ) {
        this.relationshipIsBag = relationshipIsBag;
    }

    public void setRemoveFromParentMethodName( String removeFromParentMethodName ) {
        this.removeFromParentMethodName = removeFromParentMethodName;
    }
    
    
}
