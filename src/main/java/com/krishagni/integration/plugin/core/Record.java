package com.krishagni.integration.plugin.core;

import java.util.Map;
import java.util.HashMap;

public class Record {
	private Map<String, Object> record = new HashMap<String, Object>();

	public void add(String column, Object value) {
		record.put(column, value);
	}

	public Map<String, Object> get() {
		return record;
	}
	
	public Object getValue(String column) {
		return record.get(column);
	}

}


