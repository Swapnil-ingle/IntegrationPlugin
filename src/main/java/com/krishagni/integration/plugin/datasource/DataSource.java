package com.krishagni.integration.plugin.datasource;

import com.krishagni.integration.plugin.core.Record;

public interface DataSource {
	Record nextRecord();
	
	boolean hasNext();
	
	void close();
}
