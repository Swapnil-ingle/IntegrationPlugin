package com.krishagni.integration.plugin.transformer.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.importer.domain.ImportJobErrorCode;
import com.krishagni.integration.plugin.core.InstituteImporter;
import com.krishagni.integration.plugin.core.Metadata;
import com.krishagni.integration.plugin.core.Record;
import com.krishagni.integration.plugin.core.Metadata.Field;
import com.krishagni.integration.plugin.transformer.Transformer;


public class DefaultTransformer implements Transformer {
	final static Log logger = LogFactory.getLog(InstituteImporter.class);
	
	private Metadata metadata;
	
	private ObjectMapper objMapper = new ObjectMapper();
	
	public DefaultTransformer(Metadata metaData) {
		this.metadata = metaData;
	}

	public <T> T transform(Record record, Class<T> objectType) {
		Map<String, Object> attrValueMap = new HashMap<>();
		
		try {
			for (Field columnMetadata : metadata.getFields()) {
				if (record.getValue(columnMetadata.getColumn()) == null) {
					logger.error("Error: A field present in Metadata doesn't occur in Record.");
					break;
				}
				
				if (columnMetadata.getType().equals("datetime")) {
					Date date = parseToDate(record.getValue(columnMetadata.getColumn()), columnMetadata.getFormat());
					attrValueMap.put(columnMetadata.getAttribute(),date);
				} else {
					attrValueMap.put(columnMetadata.getAttribute(), record.getValue(columnMetadata.getColumn()));
				}
	
			} 
		} catch (ParseException e) {
			logger.error("Error while parsing record");
			throw OpenSpecimenException.userError(ImportJobErrorCode.RECORD_PARSE_ERROR, e.getLocalizedMessage());
		}
			
		return objMapper.convertValue(attrValueMap, objectType);
	}

	private Date parseToDate(Object value, String dateFmt) throws ParseException {
		Date date = null;
		if (StringUtils.isBlank(dateFmt)) {
			dateFmt = ConfigUtil.getInstance().getDateFmt();
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFmt);
		date = simpleDateFormat.parse(value.toString());
					
		return date;
		
	}
	
}


