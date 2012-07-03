package com.ft.metadata.datasource.structure;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.eidosmedia.datasource.DSException;
import com.eidosmedia.datasource.ResourceArray;

public class OnTaxonomyTermResourceArray implements ResourceArray {
	private static final String UTF8 = "UTF-8";
	private static final String TERM_ARRAY_ROW_TEMPLATE = "<r>%s</r>";
	private final List<OnTaxonomyTerm> terms;

	public OnTaxonomyTermResourceArray(final List<OnTaxonomyTerm> terms) {
		this.terms = terms;
	}

	public byte[] getContent(String encoding) throws DSException {
		if( encoding == null ) {
			encoding = UTF8;
		}
		final StringBuffer buffer = new StringBuffer();
		if(terms != null){
			for(final OnTaxonomyTerm term : terms) {
				final String row = String.format(TERM_ARRAY_ROW_TEMPLATE,term.getContentAsString());
				buffer.append(row);
			}
		}
		try {
			// The docs say fragment but the CND implementation is well-formed so we'll run with that
			final String header = String.format("<?xml version=\"1.0\" encoding=\"%s\"?>",encoding);
			return (header + buffer.toString()).getBytes(encoding);
		} catch( final UnsupportedEncodingException e) {
			throw new DSException(e);
		}
	}

	public int size() throws DSException {
		return terms.size();
	}
}
