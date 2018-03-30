package com.krishagni.integration.plugin.datasource;

import java.util.HashMap;
import java.util.Map;

import com.krishagni.integration.plugin.core.Metadata;
import com.krishagni.integration.plugin.datasource.factory.DataSourceFactory;
import com.krishagni.integration.plugin.datasource.factory.impl.CsvSourceFactory;
import com.krishagni.integration.plugin.datasource.factory.impl.SqlSourceFactory;

public class DataSourceRegistrar {
	
	private static Map<String, DataSourceFactory> dataSourceFactories = new HashMap<>();
	
	public DataSourceRegistrar() {
		CsvSourceFactory csvFactory = new CsvSourceFactory();
		SqlSourceFactory sqlFactory = new SqlSourceFactory();
		dataSourceFactories.put("csvFile", csvFactory);
		dataSourceFactories.put("sqlResult", sqlFactory);
	}
	
	public DataSource getDataSource(Metadata.DataSource dsOpts) {
		
		DataSourceFactory factory = dataSourceFactories.get(dsOpts.getType());
		
		if (factory == null) {
			throw new IllegalArgumentException("Invalid data source type: " + dsOpts.getType());
		}
		
		return factory.createDataSource(dsOpts.getOpts());
	}
}
