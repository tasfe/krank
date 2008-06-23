package org.crank.crud.controller;

import org.crank.crud.GenericDao;
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
    private Map<String, Object> properties = new HashMap<String, Object>();
    private Map<String, Boolean> useProperties = new HashMap<String, Boolean>();
    
    private GenericDao<T, Serializable> repo;
    private Class<T> type;
    private EntityLocator entityLocator;
    private boolean show;
    protected Logger log = Logger.getLogger(BulkUpdaterController.class);
    
    

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
            Set<Map.Entry<String,Object>> entries = properties.entrySet();
            for (Map.Entry<String,Object> entry : entries) {
            	if (!useProperties.get(entry.getKey())) {
            		continue;
            	}
            	debug(log, "Process property key=%s value=%s", entry.getKey(), entry.getValue());
                Object value = null;
                if (entry.getValue() instanceof String) {
                	
                	String sValue = (String) entry.getValue();
	                
                	if (sValue==null || sValue.trim().equals("")) {
                		continue;
                	}
                	
                	if (TypeUtils.isEnum(type, entry.getKey())) {
	                	Class pType = TypeUtils.getPropertyType(type, entry.getKey());
	                    value = Enum.valueOf(pType, sValue);
	                } else if (TypeUtils.isString(type, entry.getKey())) {
	                    value = entry.getValue();
	                } else if (TypeUtils.isBoolean(type, entry.getKey())) {
	                    value = Boolean.valueOf(sValue);
	                } else if (TypeUtils.isInteger(type, entry.getKey())) {
	                    value = Integer.valueOf(sValue);
	                } else if (TypeUtils.isFloat(type, entry.getKey())) {
	                    value = Float.valueOf(sValue);
	                }  else if (TypeUtils.isShort(type, entry.getKey())) {
	                    value = Short.valueOf(sValue);
	                }  else if (TypeUtils.isLong(type, entry.getKey())) {
	                    value = Long.valueOf(sValue);
	                }
                } else {
                	value = entry.getValue();
                }
                bw.setPropertyValue(entry.getKey(), value);
            }
            repo.merge((T)object);
        }
        this.show=false;
        this.useProperties.clear();
    }
    public void setRepo(GenericDao repo) {
        this.repo = repo;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
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
