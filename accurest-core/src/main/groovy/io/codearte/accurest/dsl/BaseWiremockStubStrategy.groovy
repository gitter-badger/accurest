package io.codearte.accurest.dsl

import groovy.json.JsonBuilder
import groovy.transform.TypeChecked
import io.codearte.accurest.dsl.internal.DslProperty
import io.codearte.accurest.dsl.internal.Header
import io.codearte.accurest.dsl.internal.Headers
import io.codearte.accurest.util.ContentType

import java.util.regex.Pattern

import static io.codearte.accurest.util.ContentUtils.extractValue
import static io.codearte.accurest.util.JsonConverter.transformValues

@TypeChecked
abstract class BaseWiremockStubStrategy {

	private static Closure transform = {
		it instanceof DslProperty ? transformValues(it.clientValue, transform) : it
	}

	protected Map buildClientRequestHeadersSection(Headers headers) {
		if (!headers) {
			return null
		}
		return headers.entries.collectEntries { Header entry ->
			parseHeader(entry.name, entry.clientValue)
		}
	}

	protected Map buildClientResponseHeadersSection(Headers headers) {
		if (!headers) {
			return null
		}
		return headers.entries.collectEntries { Header entry ->
			[(entry.name): entry.clientValue]
		}
	}

	protected Map parseHeader(String entryKey, Object entry) {
		return [(entryKey): [equalTo: entry]]
	}

	protected Map parseHeader(String entryKey, String entry) {
		return [(entryKey): [equalTo: entry]]
	}

	protected Map parseHeader(String entryKey, Pattern entry) {
		return [(entryKey): [matches: entry.pattern()]]
	}

	public String parseBody(Object value, ContentType contentType) {
		return parseBody(value.toString(), contentType)
	}

	public String parseBody(Map map, ContentType contentType) {
		def transformedMap = transformValues(map, transform)
		return parseBody(toJson(transformedMap), contentType)
	}

	public String parseBody(List list, ContentType contentType) {
		return parseBody(toJson(list), contentType)
	}

	public String parseBody(GString value, ContentType contentType) {
		Object processedValue = extractValue(value, contentType, { DslProperty dslProperty -> dslProperty.clientValue })
		if (processedValue instanceof GString) {
			return parseBody(processedValue.toString(), contentType)
		}
		return parseBody(processedValue, contentType)
	}

	public String parseBody(String value, ContentType contentType) {
		return value
	}

	private static toJson(Object value) {
		return new JsonBuilder(value).toString()
	}

}