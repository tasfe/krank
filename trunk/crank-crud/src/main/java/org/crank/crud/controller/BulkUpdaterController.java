package org.crank.crud.controller;

import org.crank.crud.GenericDao;
import org.crank.core.CrankException;
import org.crank.core.LogUtils;
import org.crank.core.TypeUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.io.Serializable;
import static org.crank.core.LogUtils.debug;
import static org.crank.core.LogUtils.info;
import org.crank.message.MessageUtils;
import org.crank.message.MessageManagerUtils;
import org.apache.log4j.Logger;


public class BulkUpdaterController <T> {
    //private Map<String, Object> properties = new HashMap<String, Object>();
    private Map<String, Boolean> useProperties = new HashMap<String, Boolean>();
    
    private GenericDao<T, Serializable> repo;
    private Class<T> type;
    private EntityLocator entityLocator;
    private boolean show;
    protected Logger log = Logger.getLogger(BulkUpdaterController.class);
    private T prototype;
    private Map map;

    public BulkUpdaterController() {

    }

    public T getPrototype() {
    	if(prototype==null) {
    		try {
				prototype = type.newInstance();
			} catch (Exception e) {
				throw new CrankException(e, "Unable to instantiate class=%s", type);
			}
    	}
		return prototype;
	}



    public Map getMap() {
        if (map == null) {
            map = new MagicMap(getPrototype());
        }
        return map;
    }


    public void setPrototype(T prototype) {
		this.prototype = prototype;
	}

	public void show () {
    	this.show = true;
    }
    
    public void hide () {
    	this.show = false;
    }
    
    public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	
	@Transactional
	public void process() {
        List list = entityLocator.getSelectedEntities();
        debug(log, "Process list=%s", list);
        for (Object object : list) {
            BeanWrapper bw = new BeanWrapperImpl(object);
            
            Map map = getMap();
            Set<Map.Entry<String,Boolean>> entries = useProperties.entrySet();
            for (Map.Entry<String,Boolean> entry : entries) {
                String propertyName = entry.getKey();
                if (!useProperties.get(propertyName)) {
            		continue;
            	}
                Object value = null;
                if (CrudUtils.isParentManyToOne(this.type, propertyName)) {
                	debug(log, "isParentManyToOne true");
                	propertyName = CrudUtils.getParentProperty(propertyName);
                }
                value = map.get(propertyName);
                debug(log, "Process property key=%s value=%s", propertyName, value);
                bw.setPropertyValue(propertyName, value);
            }
            repo.merge((T)object);
        }
        this.show=false;
        this.useProperties.clear();
        this.map = null;
		try {
			prototype = type.newInstance();
		} catch (Exception e) {
			throw new CrankException(e, "Unable to instantiate class=%s", type);
		}        
    }
    public void setRepo(GenericDao repo) {
        this.repo = repo;
    }

//    public Map<String, Object> getProperties() {
//        return properties;
//    }
//
//    public void setProperties(Map<String, Object> properties) {
//        this.properties = properties;
//    }
    public void setType(Class<T> type) {
        this.type = type;
    }

    public Class<T> getType() {
		return type;
	}

	public void setEntityLocator(EntityLocator entityLocator) {
        this.entityLocator = entityLocator;
    }

	public Map<String, Boolean> getUseProperties() {
		return useProperties;
	}

	public void setUseProperties(Map<String, Boolean> useProperties) {
		this.useProperties = useProperties;
	}
}
