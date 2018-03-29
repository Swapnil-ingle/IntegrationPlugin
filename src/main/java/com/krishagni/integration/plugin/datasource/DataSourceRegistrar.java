package com.krishagni.integration.plugin.datasource;

import java.util.Map;

import com.krishagni.integration.plugin.core.Metadata;
import com.krishagni.integration.plugin.datasource.factory.DataSourceFactory;

public class DataSourceRegistrar {
	
	private static Map<String, DataSourceFactory> dataSourceFactories;
	
	public static DataSource getDataSource(Metadata.DataSource dsOpts) {
		DataSourceFactory factory = dataSourceFactories.get(dsOpts.getType());
		
		if (factory == null) {
			throw new IllegalArgumentException("Invalid data source type: " + dsOpts.getType());
		}
		
		return factory.createDataSource(dsOpts.getOpts());
	}
}
