package com.ft.metadata.datasource.structure;

import java.io.UnsupportedEncodingException;

import com.eidosmedia.datasource.DSException;
import com.eidosmedia.datasource.Resource;

public class OnTaxonomyTerm implements Resource {
	
	private static final String CANONICAL_NAME_TEMPLATE = "<name>%s</name>";
	private static final String EXTERNAL_ID_TEMPLATE = "<externalId>%s</externalId>";
	private static final String WSOD_KEY_TEMPLATE = "<wsodKey>%s</wsodKey>";
	private static final String CND_CODE_TEMPLATE = "<cndCode>%s</cndCode>";

	private static final String CANONICAL_NAME_EMPTY = "<name/>";
	private static final String EXTERNAL_ID_EMPTY = "<externalId/>";
	private static final String WSOD_KEY_EMPTY = "<wsodKey/>";
	private static final String CND_CODE_EMPTY = "<cndCode/>";

	private static final String UTF8 = "UTF-8";
	private final boolean company;
	private final boolean active;
	private final String canonicalName;
	private final String externalTermId;
	private final String ftWsodKey;
	private final String ftCndCode;

	public OnTaxonomyTerm(final String canonicalName, final String externalTermId, final String ftWsodKey, final String ftCndCode, final boolean company,
			final boolean active) {
		this.canonicalName = canonicalName;
		this.externalTermId = externalTermId;
		this.ftWsodKey = ftWsodKey;
		this.ftCndCode = ftCndCode;
		this.company = company;
		this.active = active;
	}

	public String getCanonicalName() {
		return canonicalName;
	}

	public String getExternalTermId() {
		return externalTermId;
	}

	public String getFtWsodKey() {
		return ftWsodKey;
	}

	public String getFtCndCode() {
		return ftCndCode;
	}

	public boolean isCompany() {
		return company;
	}

	public boolean isActive() {
		return active;
	}
	
	public String toString() {
		return String.format("\"%s\",%s,%s,%s", canonicalName, externalTermId, ftWsodKey, ftCndCode);		
	}

	public byte[] getContent(String encoding) throws DSException {
		try {
			if( encoding == null ) {
				encoding = UTF8;
			}
			final String header = String.format("<?xml version=\"1.0\" encoding=\"%s\"?>",encoding);			
			return (header + getContentAsString()).getBytes(encoding);
		} catch( final UnsupportedEncodingException e) {
			throw new DSException(e);
		}
	}
	
	public String getContentAsString() throws DSException {
		final StringBuffer response = new StringBuffer();
		append(response,CANONICAL_NAME_TEMPLATE,CANONICAL_NAME_EMPTY,canonicalName);
		append(response,EXTERNAL_ID_TEMPLATE,EXTERNAL_ID_EMPTY,externalTermId);
		append(response,WSOD_KEY_TEMPLATE,WSOD_KEY_EMPTY,ftWsodKey);
		append(response,CND_CODE_TEMPLATE,CND_CODE_EMPTY,ftCndCode);
		return response.toString();
	}
	
	private static void append(final StringBuffer buffer, final String template, final String empty, final String value) {
		if( value == null ) {
			buffer.append(empty);
		} else {
			buffer.append(String.format(template,value));
		}
	}
}