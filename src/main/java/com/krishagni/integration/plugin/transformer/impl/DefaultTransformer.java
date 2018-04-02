package com.krishagni.integration.plugin.transformer.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.krishagni.integration.plugin.core.Metadata.ObjectSchema;
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
			for (ObjectSchema.Field columnMetadata : metadata.getObjectSchema().getFields()) {
				Object value = getValue(record, columnMetadata);
				
				if (value != null) {
					attrValueMap.put(columnMetadata.getAttribute(), value);
				} else {
					logger.error("Error: A field present in Metadata doesn't occur in Record.");
					continue;
				}
			} 
		} catch (ParseException e) {
			logger.error("Error while parsing record");
			throw OpenSpecimenException.userError(ImportJobErrorCode.RECORD_PARSE_ERROR, e.getLocalizedMessage());
		}
		
		return objMapper.convertValue(attrValueMap, objectType);
	}

	private Object getValue(Record record, ObjectSchema.Field columnMetadata) throws ParseException{
		Object columnValue = getColumnValue(record, columnMetadata);
		String columnType = columnMetadata.getType();
		
		if (columnValue == null) {
			return null;
		}
		
		if (columnType.equals("date") || columnType.equals("datetime")) {
			Date date = parseDate(columnMetadata, columnValue);
			return date;
		} 
		
		return columnValue;
	}

	private Date parseDate(ObjectSchema.Field columnMetadata, Object value) throws ParseException {
		String format = columnMetadata.getFormat();
		
		if (StringUtils.isBlank(format)) {
			if (columnMetadata.getType().equals("date")) {
				format = ConfigUtil.getInstance().getDateFmt();
			} else {
				format = ConfigUtil.getInstance().getDateTimeFmt();
			}
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		
		return sdf.parse(value.toString());
	}

	private Object getColumnValue(Record record, ObjectSchema.Field columnMetadata) {
		Object columnValue = null;
		
		if (columnMetadata.isMultiple()) {
			columnValue = getMultiValueList(record,columnMetadata.getColumn());
		} else {
			columnValue = record.getValue(columnMetadata.getColumn());
		}
		
		return columnValue;
	}

	private List<String> getMultiValueList(Record record, String columnName) {
		List<String> result = new ArrayList<>();
		int instance = 1;
		String value =null;
		
		while ((value = (String)record.getValue(columnName + "#" + instance)) != null) {
				result.add(record.getValue(columnName + "#" + instance).toString());
				++instance;
		}
		
		return result;
	}

}


