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
	private Metadata metadata;
	
	final static Log logger = LogFactory.getLog(InstituteImporter.class);
	
	private ObjectMapper objMapper = new ObjectMapper();
	
	public DefaultTransformer(Metadata metaData) {
		this.metadata = metaData;
	}

	public <T> T transform(Record record, Class<T> objectType){
		Map<String, Object> attrValueMap = new HashMap<>();
		
		for (Field columnMetadata : metadata.getFields()) {
			if (record.getValue(columnMetadata.getColumn()) != null) {
				if (columnMetadata.getType().equals("datetime")) {
					Date date = parseToDate(record.getValue(columnMetadata.getColumn()),columnMetadata.getFormat());
					attrValueMap.put(columnMetadata.getAttribute(),date);
				} else {
					attrValueMap.put(columnMetadata.getAttribute(), record.getValue(columnMetadata.getColumn()));
				}
//				rowData.remove(columnMetadata.getColumn());
			} else {
				logger.error("Error: A field present in Metadata doesn't occur in Record.");
				break;
				}
		}

//		assert(rowData.isEmpty()):"Error: A field is present in record that doesn't have mapping in Metadata.";
		
		return objMapper.convertValue(attrValueMap, objectType);
	}

	private Date parseToDate(Object value, String format) {
		Date date = null;
		try {
			if(StringUtils.isBlank(format)) {
				format = ConfigUtil.getInstance().getDateFmt();
			}
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			date = simpleDateFormat.parse(value.toString());
			
			return date;
		} catch (ParseException e) {
			throw OpenSpecimenException.userError(ImportJobErrorCode.RECORD_PARSE_ERROR);
		}
		
	}
	
}


