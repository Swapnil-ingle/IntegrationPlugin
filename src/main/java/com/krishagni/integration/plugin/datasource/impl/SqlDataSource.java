package com.krishagni.integration.plugin.datasource.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.krishagni.integration.plugin.core.Record;
import com.krishagni.integration.plugin.datasource.DataSource;

import java.sql.Connection;

public class SqlDataSource implements DataSource {
	private SqlRowSet rowSet;

	private Connection connection;

	private final static Log logger = LogFactory.getLog(SqlDataSource.class);

	public SqlDataSource(SingleConnectionDataSource scds, JdbcTemplate jdbcTemplate, String query) {
		try {
			this.connection = scds.getConnection();
		} catch (Exception e) {
			logger.error("Error while Creating a connection to mysql"+e.getMessage());
		}
		this.rowSet = jdbcTemplate.queryForRowSet(query);
	}
	
	@Override
	public Record nextRecord() {
		return getRecord(rowSet.getMetaData().getColumnNames());
	}
	
	private Record getRecord(String[] columnNames) {
		Record record = new Record();
		for (int i = 0; i < columnNames.length; i++) {
			record.addValue(columnNames[i], rowSet.getObject(columnNames[i]));
		}
		
		return record;
	}
	
	@Override
	public boolean hasNext() {
		return rowSet.next();
	}

	@Override
	public void close() {
		try {
			connection.close();
		} catch (Exception e) {
			logger.error("Error while closing sql connection "+ e.getMessage());
		}
	}

}
