package org.crank.crud.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CrankMap implements Map<String, Object>{
	private Map<String, Object> map = new HashMap<String, Object>();
	
	public void clear() {
		map.clear();
		
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsKey(value);
	}

	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return map.entrySet();
	}

	public Object get(Object key) {
		return map.get(key);
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set<String> keySet() {
		return map.keySet();
	}

	public Object put(String key, Object value) {
		return map.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends Object> map2) {
		map.putAll(map2);
	}

	public Object remove(Object key) {
		return map.remove(key);
	}

	public int size() {
		return map.size();
	}

	public Collection<Object> values() {
		return map.values();
	}

}
