package com.ft.metadata.datasource.structure;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.eidosmedia.datasource.Collection;
import com.eidosmedia.datasource.DSException;
import com.eidosmedia.datasource.QueryTerm;
import com.eidosmedia.datasource.Resource;
import com.eidosmedia.datasource.ResourceIterator;

public class OnTaxonomyCollection implements Collection {
	private final OnTaxonomySearch taxonomySearch;
	private List<OnTaxonomyTerm> terms = null;
	private Map<String, String> params;

	public OnTaxonomyCollection(final OnTaxonomyDataSource dataSource) {
		taxonomySearch = new OnTaxonomySearchImpl(dataSource);
		params = dataSource.getInitParams();
	}

	public void close() throws DSException {
		terms = null;
	}

	public byte[] getInfo(final String encoding) throws DSException {
		final String configFileName = params.get("configFileName");
		final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(configFileName);
		if ( inputStream == null ) {
			throw new DSException(String.format("config file %s is missing", configFileName));
		}
		final StringBuffer buffer = new StringBuffer();
		buffer.append(getHeader(encoding));
		return buffer.append(convertInputStreamToString(inputStream)).toString().getBytes();
	}

	public Resource getResource(final String id) throws DSException {

		return null;
	}

	public ResourceIterator query(QueryTerm[] queryArray, String[] viewElements) throws DSException {
		if( queryArray.length != 1 ) {
			throw new DSException(String.format("Expected a single query term but received %d",queryArray.length));
		}
		try {
			String query = queryArray[0].value;
			if(!query.endsWith("*")) {
				query = query + "*";
			}

			terms = taxonomySearch.search(query);

			return new OnTaxonomyTermResourceIterator(terms);
		} catch( final OnTaxonomySearchException e ) {
			throw new DSException(e);
		}
	}

	public int size() throws DSException {
		if( terms == null ) {
			throw new DSException("No search yet performed");
		}
		return terms.size();
	}

	private String convertInputStreamToString(InputStream is) {
	    try {
	        return new java.util.Scanner(is).useDelimiter("\\A").next();
	    } catch (java.util.NoSuchElementException e) {
	        return "";
	    }
	}

	private String getHeader(final String str){
		final String encoding = str == null ? "UFT-8" : str;
		return String.format("<?xml version=\"1.0\" encoding=\"%s\"?>", encoding);
	}
}
