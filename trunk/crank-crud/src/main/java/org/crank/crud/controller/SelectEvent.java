package org.crank.crud.controller;

import java.io.Serializable;
import java.util.EventObject;

public class SelectEvent extends EventObject implements Serializable {
	private Object value;
	private Long serial = System.currentTimeMillis();
	public SelectEvent( Object source, Object value ) {
        super( source );
        this.value = value;
    }
    public Object getValue() {
		return value;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + serial.hashCode();
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SelectEvent))
			return false;
		final SelectEvent other = (SelectEvent) obj;
		if (serial == null) {
			if (other.serial != null)
				return false;
		} else if (!serial.equals(other.serial))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		
		return true;
	}

}
