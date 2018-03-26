package com.krishagni.integration.plugin.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;
import com.krishagni.catissueplus.core.administrative.services.InstituteService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.util.CsvFileReader;
import com.krishagni.integration.plugin.core.Metadata.Field;
import com.krishagni.integration.plugin.transformer.Transformer;
import com.krishagni.integration.plugin.transformer.impl.DefaultTransformer;

public class InstituteImporter {
	@Autowired
	InstituteService instituteSvc;
	
	//private final static Log logger = LogFactory.getLog(InstituteImporter.class);
	
	public void importObjects () {
		Metadata instituteMetadata = getMetadata();
		Transformer transformer = new DefaultTransformer(instituteMetadata);
		
		CsvFileReader csvReader = CsvFileReader.createCsvFileReader("/home/krishagni/Desktop/TestInstituteCsv.csv", true);
		String[] columnNames = csvReader.getColumnNames();		
		while (csvReader.next()) {
			Record record = getRecord(columnNames, csvReader.getRow());
			InstituteDetail detail = transformer.transform(record, InstituteDetail.class);
			instituteSvc.createInstitute(new RequestEvent<InstituteDetail>(detail));
		}
	}
	
	private Record getRecord(String[] columnNames, String[] row) {
		Record record = new Record();
		for (int i = 0; i <row.length; i++) {
			record.addValue(columnNames[i], row[i]);
		}
		
		return record;
	}

	private Metadata getMetadata() {
		Metadata metadata = new Metadata();
		Field idMetadata = new Field();
		idMetadata.setAttribute("id");
		idMetadata.setColumn("Identifier");
		idMetadata.setType("Long");
		metadata.addField(idMetadata);
		
		Field nameMetadata = new Field();
		nameMetadata.setAttribute("name");
		nameMetadata.setColumn("Institute Name");
		nameMetadata.setType("String");
		metadata.addField(nameMetadata);
		
		return metadata;
	}
}
