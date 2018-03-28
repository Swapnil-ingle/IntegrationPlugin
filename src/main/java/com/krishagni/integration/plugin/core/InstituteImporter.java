package com.krishagni.integration.plugin.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.administrative.services.InstituteService;
import com.krishagni.integration.plugin.core.Metadata.Field;
import com.krishagni.integration.plugin.datasource.DataSource;
import com.krishagni.integration.plugin.datasource.impl.CsvFileDataSource;
import com.krishagni.integration.plugin.transformer.Transformer;
import com.krishagni.integration.plugin.transformer.impl.DefaultTransformer;

public class InstituteImporter {
	@Autowired
	InstituteService instituteSvc;
	
	private DataSource ds;
	
	private final static Log logger = LogFactory.getLog(InstituteImporter.class);
	
	public void importObjects () {
		try {
			Metadata instituteMetadata = getMetadata();
			Transformer transformer = new DefaultTransformer(instituteMetadata);
			//InstituteDetail detail = null;
			
			ds = new CsvFileDataSource("/home/krishagni/Desktop/TestInstituteCsv.csv");
		
			while (ds.hasNext()) {
				Record record = ds.nextRecord();
				InstituteDetail detail = transformer.transform(record, InstituteDetail.class);
				//instituteSvc.createInstitute(new RequestEvent<InstituteDetail>(detail));
				logger.info(detail.getId());
				logger.info(detail.getName());
				logger.info(detail.getDate());
				logger.info(detail.getCityNames());
			}
		} catch(Exception e) {
			logger.error("Error while processing.");
		} finally {
			if (ds != null) {
				ds.close();
			}
		}
		
	}
	
	private Metadata getMetadata() {
		Metadata metadata = new Metadata();
		Field idMetadata = new Field();
		idMetadata.setAttribute("id");
		idMetadata.setColumn("Identifier");
		idMetadata.setType("Long");
		idMetadata.setMultiple(false);
		metadata.addField(idMetadata);
		
		Field nameMetadata = new Field();
		nameMetadata.setAttribute("name");
		nameMetadata.setColumn("Institute Name");
		nameMetadata.setType("String");
		nameMetadata.setMultiple(false);
		metadata.addField(nameMetadata);
		
		Field dateMetadata = new Field();
		dateMetadata.setAttribute("date");
		dateMetadata.setColumn("Date");
		dateMetadata.setType("date");
		dateMetadata.setFormat("dd/MM/yyyy");
		dateMetadata.setMultiple(false);
		metadata.addField(dateMetadata);
		
		Field cityMetadata = new Field();
		cityMetadata.setAttribute("cityNames");
		cityMetadata.setColumn("City");
		cityMetadata.setType("String");
		cityMetadata.setMultiple(true);
		metadata.addField(cityMetadata);
		
		return metadata;
	}
}
