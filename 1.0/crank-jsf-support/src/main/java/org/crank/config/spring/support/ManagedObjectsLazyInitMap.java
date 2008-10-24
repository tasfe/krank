package org.crank.config.spring.support;

import org.crank.crud.controller.CrudManagedObject;
import org.crank.core.MapUtils;
import org.crank.core.StringUtils;
import org.crank.core.CrankException;


import java.util.*;
import java.io.Serializable;

@SuppressWarnings("unchecked")
public class ManagedObjectsLazyInitMap<K extends Serializable, V> implements Map<K, V> {

    
	private Map map = new HashMap();
    private Map<String, CrudManagedObject> managedObjects;
    private DeferredResourceCreator deferredResourceCreator;

    public ManagedObjectsLazyInitMap(List<CrudManagedObject> mos, DeferredResourceCreator drc) {
        managedObjects = MapUtils.convertListToMap("name", mos);
        deferredResourceCreator = drc;
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public V get(Object oKey) {
        String key = (String)oKey;
        
        if (!map.containsKey(key) ) {
            String capKey = null;

            if (Character.isLowerCase(key.charAt(0))){
                capKey = StringUtils.capitalize(key);
            } else {
                capKey = key;
            }

            CrudManagedObject cmo = managedObjects.get(capKey);

            if (cmo==null) {
                throw new CrankException("Can't find managed object for key %s in %s", capKey, managedObjects);
            }
            
           try {
                deferredResourceCreator.createResource(map, cmo);
           } catch (Exception ex) {
               try {
                deferredResourceCreator.createResource(map,  cmo);
               } catch(Exception ex2)  {
                   throw new CrankException(ex, "Problem getting resource out of map key=%s message=%s ex type=%s", key, ex.getMessage(), ex.getClass().getName());
               }
           }
        }
        return (V) map.get(key);
    }

    public V put(K key, V value) {
        return (V) map.put(key, value);
    }

    public V remove(Object key) {
        return null;
    }

    public void putAll(Map t) {

    }

    public void clear() {

    }

    public Set keySet() {
        return map.keySet();
    }

    public Collection values() {
        return map.values();
    }

    public Set entrySet() {
        return map.entrySet();
    }
}
