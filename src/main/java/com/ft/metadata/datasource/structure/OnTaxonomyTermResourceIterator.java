package com.ft.metadata.datasource.structure;

import java.util.ArrayList;
import java.util.List;

import com.eidosmedia.datasource.DSException;
import com.eidosmedia.datasource.Resource;
import com.eidosmedia.datasource.ResourceArray;
import com.eidosmedia.datasource.ResourceIterator;

public class OnTaxonomyTermResourceIterator implements ResourceIterator {
	private final List<OnTaxonomyTerm> terms;
	private int index = -1;

	public OnTaxonomyTermResourceIterator(final List<OnTaxonomyTerm> terms) {
		this.terms = terms;
	}

	public Resource next() throws DSException {
		if(isInRange(++index)) {
			return terms.get(index);
		}
		return null;
	}

	public ResourceArray next(int count) throws DSException {
		final List<OnTaxonomyTerm> selected = new ArrayList<OnTaxonomyTerm>();
		while(isInRange(++index)) {
			selected.add(terms.get(index));
		}
		return new OnTaxonomyTermResourceArray(selected);
	}

	public int size() throws DSException {
		return terms.size();
	}

	private boolean isInRange(final int index) {
		return index >= 0 && index < terms.size();
	}
}