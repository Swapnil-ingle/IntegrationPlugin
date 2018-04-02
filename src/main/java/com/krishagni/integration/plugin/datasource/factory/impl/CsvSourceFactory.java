package com.krishagni.integration.plugin.datasource.factory.impl;

import java.util.Map;

import com.krishagni.integration.plugin.datasource.DataSource;
import com.krishagni.integration.plugin.datasource.factory.DataSourceFactory;
import com.krishagni.integration.plugin.datasource.impl.CsvFileDataSource;

public class CsvSourceFactory implements DataSourceFactory {

	@Override
	public DataSource createDataSource(Map<String, String> opts) {
		
		if (opts.get("directory")!=null) {
			CsvFileDataSource csvReader = new CsvFileDataSource(opts.get("directory"));
			return csvReader;
		}
		
		return null;
	}
	
	public String getName() {
		return "csvFile";
	}

}
