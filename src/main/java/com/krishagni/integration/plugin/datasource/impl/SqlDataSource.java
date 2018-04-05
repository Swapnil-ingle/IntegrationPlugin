package com.krishagni.integration.plugin.datasource.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.krishagni.integration.plugin.core.Record;
import com.krishagni.integration.plugin.datasource.DataSource;

public class SqlDataSource implements DataSource {
	private JdbcTemplate jdbcTemplate;
	
	private SqlRowSet rowSet;
	
	private String query;
	
	public SqlDataSource(JdbcTemplate jdbcTemplate, String query) {
		this.jdbcTemplate = jdbcTemplate;
		this.query = query;
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
		// TODO Auto-generated method stub
	}

}
