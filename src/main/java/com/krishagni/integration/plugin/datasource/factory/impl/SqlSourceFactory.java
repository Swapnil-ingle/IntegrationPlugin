package com.krishagni.integration.plugin.datasource.factory.impl;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.krishagni.integration.plugin.datasource.DataSource;
import com.krishagni.integration.plugin.datasource.factory.DataSourceFactory;
import com.krishagni.integration.plugin.datasource.impl.SqlDataSource;

public class SqlSourceFactory implements DataSourceFactory{

	@Override
	public DataSource createDataSource(Map<String, String> opts) {
		SingleConnectionDataSource scds = new SingleConnectionDataSource(opts.get("url"), opts.get("username"), 
				opts.get("password"), true);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(scds);
		
		return new SqlDataSource(jdbcTemplate, opts.get("query"));
	}

	@Override
	public String getName() {
		return "sqlResult";
	}

}
