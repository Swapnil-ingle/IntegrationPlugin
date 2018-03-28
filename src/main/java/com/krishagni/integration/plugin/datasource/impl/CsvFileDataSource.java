package com.krishagni.integration.plugin.datasource.impl;

import com.krishagni.catissueplus.core.common.util.CsvFileReader;
import com.krishagni.integration.plugin.core.Record;
import com.krishagni.integration.plugin.datasource.DataSource;

public class CsvFileDataSource implements DataSource {
	private CsvFileReader csvReader;
	
	private String[] columnNames;
	
	public CsvFileDataSource(String filename) {
		this.csvReader = CsvFileReader.createCsvFileReader(filename, true);
		this.columnNames = csvReader.getColumnNames();
	}
	
	@Override
	public Record nextRecord() {
		return getRecord(columnNames, csvReader.getRow());
	}
	
	private Record getRecord(String[] columnNames, String[] row) {
		Record record = new Record();
		for (int i = 0; i <row.length; i++) {
			record.addValue(columnNames[i], row[i]);
		}
		
		return record;
	}
	
	@Override
	public boolean hasNext() {
		return csvReader.next();
	}
	
	@Override
	public void close() {
		csvReader.close();
	}

}
