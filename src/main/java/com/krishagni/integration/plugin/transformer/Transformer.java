package com.krishagni.integration.plugin.transformer;

import com.krishagni.integration.plugin.core.Record;

public interface Transformer {
	<T> T transform(Record record, Class<T> objectType);
}
