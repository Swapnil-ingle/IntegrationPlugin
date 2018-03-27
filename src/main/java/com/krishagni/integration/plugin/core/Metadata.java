package com.krishagni.integration.plugin.core;

import java.util.ArrayList;
import java.util.List;

public class Metadata {
	List <Field> fields = new ArrayList<Field>();
	
	public void addField(Field field) {
		fields.add(field);
	}
	
	public List<Field> getFields() {
		return fields;
	}
	
	public static class Field {
		private String attribute;
		
		private String column;
		
		private String type;
		
		private Boolean multiple;
		
		private String format;
		
		private List<Field> fields;

		public String getAttribute() {
			return attribute;
		}

		public void setAttribute(String attribute) {
			this.attribute = attribute;
		}

		public String getColumn() {
			return column;
		}

		public void setColumn(String column) {
			this.column = column;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public List<Field> getFields() {
			return fields;
		}

		public void setFields(List<Field> fields) {
			this.fields = fields;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public Boolean isMultiple() {
			return multiple;
		}

		public void setMultiple(Boolean multiple) {
			this.multiple = multiple;
		}
	}
}




