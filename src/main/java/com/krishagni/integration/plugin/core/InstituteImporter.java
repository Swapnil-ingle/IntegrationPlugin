package com.krishagni.integration.plugin.core;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;
import com.krishagni.catissueplus.core.administrative.services.InstituteService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.integration.plugin.datasource.DataSource;
import com.krishagni.integration.plugin.datasource.DataSourceRegistrar;
import com.krishagni.integration.plugin.transformer.Transformer;
import com.krishagni.integration.plugin.transformer.impl.DefaultTransformer;

public class InstituteImporter {
	@Autowired
	InstituteService instituteSvc;
	
	private DataSource ds;
	
	private final static Log logger = LogFactory.getLog(InstituteImporter.class);
	
	public void importObjects () {
		try {
			Metadata metadata = getMetadata();
			Transformer transformer = new DefaultTransformer(metadata);
			ds = DataSourceRegistrar.getDataSource(metadata.getDataSource());
			InstituteDetail detail;
			
			while (ds.hasNext()) {
				Record record = ds.nextRecord();
				detail = transformer.transform(record, InstituteDetail.class);
				instituteSvc.createInstitute(new RequestEvent<InstituteDetail>(detail));
			}
		} catch(Exception e) {
			logger.error("Error while processing: " + e.getMessage());
		} finally {
			if (ds != null) {
				ds.close();
			}
		}
	}
	
	private Metadata getMetadata() throws Exception{
		ObjectMapper objMapper = new ObjectMapper();
		
		File Json = new File("/Users/swapnil/Integrationplugin/Resources/Metadata.json");
		
		return objMapper.readValue(Json, Metadata.class);
	}
}
