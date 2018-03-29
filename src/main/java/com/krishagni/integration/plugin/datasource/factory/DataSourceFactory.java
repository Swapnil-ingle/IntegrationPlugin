package com.krishagni.integration.plugin.datasource.factory;

import java.util.Map;

import com.krishagni.integration.plugin.datasource.DataSource;

public interface DataSourceFactory {
	DataSource createDataSource(Map<String, String> opts);
}
