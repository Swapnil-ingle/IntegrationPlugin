package com.krishagni.integration.plugin.datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.krishagni.integration.plugin.core.Metadata;
import com.krishagni.integration.plugin.datasource.factory.DataSourceFactory;

public class DataSourceRegistrar {
	private static Map<String, DataSourceFactory> dataSourceFactories = new HashMap<>();
	
	private static void register(DataSourceFactory dataSource) {
		dataSourceFactories.put(dataSource.getName(), dataSource);
	}
	
	public void setDataSources(List<DataSourceFactory> dataSources) {
		for (DataSourceFactory dataSource : dataSources) {
			DataSourceRegistrar.register(dataSource);
		}
	}
	
	public List<DataSourceFactory> getDataSources(){
		return new ArrayList<>(dataSourceFactories.values());
	}
	
	public static DataSource getDataSource(Metadata.DataSource dsOpts) {
		DataSourceFactory factory = dataSourceFactories.get(dsOpts.getType());

		if (factory == null) {
			throw new IllegalArgumentException("Invalid data source type: " + dsOpts.getType());
		}
		
		return factory.createDataSource(dsOpts.getOpts());
	}
}
