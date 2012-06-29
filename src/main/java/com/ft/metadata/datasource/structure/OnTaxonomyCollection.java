package com.ft.metadata.datasource.structure;

import java.util.List;

import com.eidosmedia.datasource.Collection;
import com.eidosmedia.datasource.DSException;
import com.eidosmedia.datasource.QueryTerm;
import com.eidosmedia.datasource.Resource;
import com.eidosmedia.datasource.ResourceIterator;

public class OnTaxonomyCollection implements Collection {
	private final OnTaxonomySearch taxonomySearch;
	private List<OnTaxonomyTerm> terms = null; 
	
	public OnTaxonomyCollection(final OnTaxonomyDataSource dataSource) {
		taxonomySearch = new OnTaxonomySearchImpl(dataSource);
	}
	
	public void close() throws DSException {
		terms = null;
	}

	public byte[] getInfo(final String encoding) throws DSException {
		if( terms == null ) {
			throw new DSException("No search yet performed");
		}
		return new OnTaxonomyTermResourceArray(terms).getContent(encoding);
	}

	public Resource getResource(final String id) throws DSException {

		return null;
	}

	public ResourceIterator query(final QueryTerm[] queryArray, final String[] viewElements) throws DSException {
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
}
