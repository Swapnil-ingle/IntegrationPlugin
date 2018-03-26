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
	private final static Log logger = LogFactory.getLog(InstituteImporter.class);
	
	private Metadata metadata;
	
	private ObjectMapper objMapper = new ObjectMapper();
	
	public DefaultTransformer(Metadata metaData) {
		this.metadata = metaData;
	}

	public <T> T transform(Record record, Class<T> objectType) {
		Map<String, Object> attrValueMap = new HashMap<>();
		
		try {
			for (Field columnMetadata : metadata.getFields()) {
				Object columnValue = record.getValue(columnMetadata.getColumn());
				String columnType = columnMetadata.getType();
				String columnAttribute = columnMetadata.getAttribute();
				
				if (columnValue == null) {
					logger.error("Error: A field present in Metadata doesn't occur in Record.");
					continue;
				}
				
				if (columnType.equals("date")) {
					Date date = parseToDate(columnValue, columnMetadata.getFormat());
					attrValueMap.put(columnAttribute,date);
				} else if (columnType.equals("datetime")) {
					Date date = parseToDatetime(columnValue, columnMetadata.getFormat());
					attrValueMap.put(columnAttribute,date);
				} else {
					attrValueMap.put(columnAttribute, columnValue);
				}
	
			} 
		} catch (ParseException e) {
			logger.error("Error while parsing record");
			throw OpenSpecimenException.userError(ImportJobErrorCode.RECORD_PARSE_ERROR, e.getLocalizedMessage());
		}
		
		return objMapper.convertValue(attrValueMap, objectType);
	}

	private Date parseToDate(Object value, String dateFmt) throws ParseException {
		if (StringUtils.isBlank(dateFmt)) {
			dateFmt = ConfigUtil.getInstance().getDateFmt();
		} 
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFmt);
		Date date = simpleDateFormat.parse(value.toString());
					
		return date;
	}
	
	private Date parseToDatetime(Object value, String dateFmt) throws ParseException {
		if(StringUtils.isBlank(dateFmt)) {
			dateFmt = ConfigUtil.getInstance().getDateTimeFmt();
		}
		
		return parseToDate(value, dateFmt);
	}
}


