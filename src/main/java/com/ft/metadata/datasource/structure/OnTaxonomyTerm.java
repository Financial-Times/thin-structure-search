package com.ft.metadata.datasource.structure;


import java.io.UnsupportedEncodingException;

import com.eidosmedia.datasource.DSException;
import com.eidosmedia.datasource.Resource;

public class OnTaxonomyTerm implements Resource {

	private static final String CANONICAL_NAME_TEMPLATE = "<Name>%s</Name>";
	private static final String EXTERNAL_ID_TEMPLATE = "<externalId>%s</externalId>";
	private static final String WSOD_KEY_TEMPLATE = "<wsodKey>%s</wsodKey>";
	private static final String CND_CODE_TEMPLATE = "<FTCode>%s</FTCode>";
	private static final String SEDOL_TEMPLATE = "<SEDOL>%s</SEDOL>";
	private static final String COUNTRY_TEMPLATE = "<Country>%s</Country>";
	private static final String TICKER_TEMPLATE = "<Ticker>%s</Ticker>";

	private static final String TYPE_TEMPLATE = "<Type>%s</Type>";
	private static final String STATUS_TEMPLATE = "<Status>%s</Status>";
	private static final String ISIN_TEMPLATE = "<ISIN>%s</ISIN>";
	private static final String SYMBOL_TEMPLATE = "<Symbol>%s</Symbol>";
	private static final String EXCHANGE_CODE_TEMPLATE = "<Exchange-Code>%s</Exchange-Code>";
	private static final String EXCHANGE_COUNTRY_TEMPLATE = "<Exchange-Country>%s</Exchange-Country>";
	private static final String VERSION_TEMPLATE = "<Version>%s</Version>";
	private static final String COMPOSITE_ID = "<Composite-Id>%s</Composite-Id>";

	private static final String CANONICAL_NAME_EMPTY = "<Name/>";
	private static final String EXTERNAL_ID_EMPTY = "<externalId/>";
	private static final String WSOD_KEY_EMPTY = "<wsodKey/>";
	private static final String CND_CODE_EMPTY = "<cndCode/>";
	private static final String SEDOL_EMPTY = "<SEDOL/>";
	private static final String COUNTRY_EMPTY = "<Country/>";
	private static final String TICKER_EMPTY = "<Ticker/>";

	private static final String TYPE_EMPTY = "<Type/>";
	private static final String STATUS_EMPTY = "<Status/>";
	private static final String ISIN_EMPTY = "<ISIN/>";
	private static final String SYMBOL_EMPTY = "<Symbol/>";
	private static final String EXCHANGE_CODE_EMPTY = "<Exchange-Code/>";
	private static final String EXCHANGE_COUNTRY_EMPTY = "<Exchange-Country/>";
	private static final String VERSION_EMPTY = "<Version/>";
	private static final String COMPOSITE_ID_EMPTY = "<Composite-Id/>";

	private static final String UTF8 = "UTF-8";
	private boolean company;
	private boolean active;
	private String canonicalName;
	private String externalTermId;
	private String ftWsodKey;
	private String ftCndCode;
	private String sedol;
	private String tickerSymbol;
	private String country;
	private String type;
	private String status;
	private String isin;
	private String exchangeCode;
	private String exchangeCountry;
	private String version;
	private String compositeId;

	private OnTaxonomyTerm(Builder builder) {
		this.canonicalName = builder.canonicalName;
		this.externalTermId = builder.externalTermId;
		this.ftWsodKey = builder.ftWsodKey;
		this.ftCndCode = builder.ftCndCode;
		this.company = builder.company;
		this.active = builder.active;
		this.sedol = builder.sedol;
		this.tickerSymbol = builder.tickerSymbol;
		this.country = builder.country;
		this.type = builder.type;
		this.status = builder.status;
		this.isin = builder.isin;
		this.exchangeCode = builder.exchangeCode;
		this.exchangeCountry = builder.exchangeCountry;
		this.version = builder.version;
		this.compositeId = builder.compositeId;
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

	public String getSedol() {
		return sedol;
	}

	public String getTickerSymbol() {
		return tickerSymbol;
	}

	public String getCountry() {
		return country;
	}

	public String getType() {
		return type;
	}

	public String getStatus() {
		return status;
	}

	public String getIsin() {
		return isin;
	}

	public String getExchangeCode() {
		return exchangeCode;
	}

	public String getExchangeCountry() {
		return exchangeCountry;
	}

	public String getVersion() {
		return version;
	}


	public String getCompositeId() {
		return compositeId;
	}

	@Override
	public String toString() {
		return "OnTaxonomyTerm [canonicalName=" + canonicalName + ", externalTermId=" + externalTermId + ", ftWsodKey="
				+ ftWsodKey + ", ftCndCode=" + ftCndCode + ", sedol=" + sedol + ", tickerSymbol=" + tickerSymbol
				+ ", country=" + country + ", type=" + type + ", status=" + status + ", isin=" + isin
				+ ", exchangeCode=" + exchangeCode + ", exchangeCountry=" + exchangeCountry + ", version=" + version
				+ "]";
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
		append(response,SEDOL_TEMPLATE,SEDOL_EMPTY,sedol);
		append(response,COUNTRY_TEMPLATE,COUNTRY_EMPTY,country);
		append(response,TICKER_TEMPLATE,TICKER_EMPTY,tickerSymbol);

		append(response,TYPE_TEMPLATE,TYPE_EMPTY,type);
		append(response,STATUS_TEMPLATE,STATUS_EMPTY,status);
		append(response,ISIN_TEMPLATE,ISIN_EMPTY,isin);
		append(response,SYMBOL_TEMPLATE,SYMBOL_EMPTY, tickerSymbol);
		append(response,EXCHANGE_CODE_TEMPLATE,EXCHANGE_CODE_EMPTY,exchangeCode);
		append(response,EXCHANGE_COUNTRY_TEMPLATE,EXCHANGE_COUNTRY_EMPTY,exchangeCountry);
		append(response,VERSION_TEMPLATE,VERSION_EMPTY,version);
		append(response, COMPOSITE_ID, COMPOSITE_ID_EMPTY, compositeId);

		return response.toString();
	}

	private static void append(final StringBuffer buffer, final String template, final String empty, final String value) {
		if( value == null ) {
			buffer.append(empty);
		} else {
			buffer.append(String.format(template,value));
		}
	}

	public static class Builder {

		private boolean company;
		private boolean active;
		private String canonicalName;
		private String externalTermId;
		private String ftWsodKey;
		private String ftCndCode;
		private String sedol;
		private String tickerSymbol;
		private String country;
		private String type;
		private String status;
		private String isin;
		private String exchangeCode;
		private String exchangeCountry;
		private String version;
		private String compositeId;

		public Builder company(final boolean company){
			this.company = company;
			return this;
		}

		public Builder active(final boolean active){
			this.active = active;
			return this;
		}

		public Builder canonicalName(final String canonicalName){
			this.canonicalName = canonicalName;
			return this;
		}

		public Builder externalTermId(final String externalTermId){
			this.externalTermId = externalTermId;
			return this;
		}

		public Builder ftWsodKey(final String ftWsodKey){
			this.ftWsodKey = ftWsodKey;
			return this;
		}

		public Builder ftCndCode(final String ftCndCode){
			this.ftCndCode = ftCndCode;
			return this;
		}

		public Builder sedol(final String sedol){
			this.sedol = sedol;
			return this;
		}

		public Builder tickerSymbol(final String tickerSymbol){
			this.tickerSymbol = tickerSymbol;
			return this;
		}

		public Builder country(final String country){
			this.country = country;
			return this;
		}

		public Builder type(final String type){
			this.type = type;
			return this;
		}

		public Builder status(final String status){
			this.status = status;
			return this;
		}

		public Builder isin(final String isin){
			this.isin = isin;
			return this;
		}

		public Builder exchangeCode(final String exchangeCode){
			this.exchangeCode = exchangeCode;
			return this;
		}

		public Builder exchangeCountry(final String exchangeCountry){
			this.exchangeCountry = exchangeCountry;
			return this;
		}

		public Builder version(final String version){
			this.version = version;
			return this;
		}

		public Builder compositeId(final String compositeId){
			this.compositeId = compositeId;
			return this;
		}

		public OnTaxonomyTerm build(){
			return new OnTaxonomyTerm(this);
		}



	}
}