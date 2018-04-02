package com.krishagni.integration.plugin.datasource.factory.impl;

import java.util.Map;

import com.krishagni.integration.plugin.datasource.DataSource;
import com.krishagni.integration.plugin.datasource.factory.DataSourceFactory;

public class SqlSourceFactory implements DataSourceFactory{

	@Override
	public DataSource createDataSource(Map<String, String> opts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "sqlResult";
	}

}
