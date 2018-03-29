package com.krishagni.integration.plugin.core;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishagni.catissueplus.core.administrative.services.InstituteService;
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
			
			ds = new CsvFileDataSource("/home/krishagni/Desktop/TestInstituteCsv.csv");
		
			while (ds.hasNext()) {
				Record record = ds.nextRecord();
				InstituteDetail detail = transformer.transform(record, InstituteDetail.class);
				//instituteSvc.createInstitute(new RequestEvent<InstituteDetail>(detail));
				logger.info("Id : "+detail.getId());
				logger.info("Name : "+detail.getName());
				logger.info("Date : "+detail.getDate());
				logger.info("City Names : "+detail.getCityNames()+"\n");
			}
		} catch(Exception e) {
			logger.error("Error while processing.");
		} finally {
			if (ds != null) {
				ds.close();
			}
		}
		
	}
	
	private Metadata getMetadata() throws Exception{
		ObjectMapper objMapper = new ObjectMapper();
		
		InputStream is = new FileInputStream("/home/krishagni/Documents/Metadata.json");
		String Json = IOUtils.toString(is);
		
		/*String Json = "{"
				+ 	"\"fields\" : ["
				+ 				"{"
				+ 					"\"attribute\" : \"id\", "
				+ 					"\"column\" : \"Identifier\", "
				+ 					"\"type\" : \"Long\", "
				+ 					"\"multiple\" : \"false\""
				+ 				"},"
				+  				"{"
				+ 					"\"attribute\" : \"name\", "
				+ 					"\"column\" : \"Institute Name\", "
				+ 					"\"type\" : \"String\", "
				+ 					"\"multiple\" : \"false\""
				+ 				"},"
				+				"{"
				+ 					"\"attribute\" : \"date\", "
				+ 					"\"column\" : \"Date\", "
				+ 					"\"type\" : \"datetime\", "
				+ 					"\"multiple\" : \"false\","
				+					"\"format\" : \"dd/MM/yyyy HH:mm:ss\""
				+ 				"},"
				+				"{"
				+ 					"\"attribute\" : \"cityNames\", "
				+ 					"\"column\" : \"City\", "
				+ 					"\"type\" : \"String\", "
				+ 					"\"multiple\" : \"true\""
				+ 				"}"
				+ 		"]"
				+ "}";*/
		
		Metadata metadata = objMapper.readValue(Json, Metadata.class);
		
		return metadata;
	}
}
