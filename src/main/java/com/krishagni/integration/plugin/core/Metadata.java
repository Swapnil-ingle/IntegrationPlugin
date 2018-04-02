package com.krishagni.integration.plugin.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Metadata {
	private DataSource dataSource;
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	private ObjectSchema objectSchema;
	
	public ObjectSchema getObjectSchema() {
		return objectSchema;
	}

	public void setObjectSchema(ObjectSchema objectSchema) {
		this.objectSchema = objectSchema;
	}

	
	public static class ObjectSchema {
		private List <Field> fields = new ArrayList<Field>();
		
		private String type;
		
		public void addField(Field field) {
			fields.add(field);
		}
		
		public List<Field> getFields() {
			return fields;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
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

	public static class DataSource {
		private String type;
		
		private Map<String, String> opts = new HashMap<>();
		
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Map<String, String> getOpts() {
			return opts;
		}

		public void setOpts(Map<String, String> opts) {
			this.opts = opts;
		}
	}
	
}




