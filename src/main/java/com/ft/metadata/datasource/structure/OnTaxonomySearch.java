package com.ft.metadata.datasource.structure;

import java.util.List;

public interface OnTaxonomySearch {
	List<OnTaxonomyTerm> search(String query) throws OnTaxonomySearchException;
}
