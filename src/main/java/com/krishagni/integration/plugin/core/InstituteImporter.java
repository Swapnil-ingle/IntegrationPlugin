package com.krishagni.integration.plugin.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;
import com.krishagni.catissueplus.core.administrative.services.InstituteService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.CsvFileReader;
import com.krishagni.integration.plugin.core.Metadata.Field;
import com.krishagni.integration.plugin.transformer.Transformer;
import com.krishagni.integration.plugin.transformer.impl.DefaultTransformer;

public class InstituteImporter {
	@Autowired
	InstituteService instituteSvc;
	
	final static Log logger = LogFactory.getLog(InstituteImporter.class);
	
	public void importerMain () {
		Metadata instituteMetadata = new Metadata();
		
		Record record = new Record();
		
		CsvFileReader csvReader = null;
		csvReader = csvReader.createCsvFileReader("TestInstituteCSV", true);
		String[] ColumnNames = csvReader.getColumnNames();
		
		instituteMetadata = populateMetadata(instituteMetadata);
		record = getRecordObject(ColumnNames,csvReader.getRow(),record);
		
		Transformer transformer = new DefaultTransformer(instituteMetadata);
		InstituteDetail detail = transformer.transform(record, InstituteDetail.class);

		RequestEvent<InstituteDetail> req = new RequestEvent<InstituteDetail>(detail);
		ResponseEvent<InstituteDetail> resp = instituteSvc.createInstitute(req);
		resp.throwErrorIfUnsuccessful();
		//
		//Testing Output
		//
		logger.info(detail.getId());
		logger.info(detail.getName());
	}
	
	private Record getRecordObject(String[] columnNames, String[] row, Record record) {
		
		for(int i=0;i<row.length;i++) {
			record.add(columnNames[i], row[i]);
		}
		
		return record;
	}

	private Metadata populateMetadata(Metadata metadata) {
		Field idMetadata = metadata.new Field();
		idMetadata.setAttribute("id");
		idMetadata.setColumn("Identifier");
		idMetadata.setType("Long");
		metadata.addField(idMetadata);
		
		Field nameMetadata = metadata.new Field();
		nameMetadata.setAttribute("name");
		nameMetadata.setColumn("Institute Name");
		nameMetadata.setType("String");
		metadata.addField(nameMetadata);
		
		return metadata;
	}
}
