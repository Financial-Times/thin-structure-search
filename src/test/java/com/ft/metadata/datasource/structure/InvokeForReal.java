package com.ft.metadata.datasource.structure;

import java.util.HashMap;

import com.eidosmedia.datasource.Collection;
import com.eidosmedia.datasource.Datasource;
import com.eidosmedia.datasource.QueryTerm;

// This is not a test but it could be the basis for an integration test
// once a suitable service is mocked up.
public class InvokeForReal {
	
	public static void main(final String... args) throws Exception {
		final HashMap<String,String> params = new HashMap<String,String>();
		params.put(OnTaxonomyDataSource.REALM_PARAM, "Metadata Services");
		params.put(OnTaxonomyDataSource.HOST_PARAM,"metadata.internal.ft.com");
		params.put(OnTaxonomyDataSource.PORT_PARAM,"83");
		params.put(OnTaxonomyDataSource.USERNAME_PARAM,"metadata-qmi");
		params.put(OnTaxonomyDataSource.PASSWORD_PARAM,"metadanger");
		params.put(OnTaxonomyDataSource.PRINCIPAL_PARAM,"methode");
		
		final OnTaxonomyDataSourceManager manager = new OnTaxonomyDataSourceManager();
		manager.init(params);

		final Datasource datasource = manager.makeDatasource();

		final Collection collection = datasource.getCollection();

		final QueryTerm[] terms = { new QueryTerm("???", "HSBC") };
		final String[] fields = {};
		collection.query(terms, fields);
		final byte[] response = collection.getInfo("UTF-8");
		System.out.println(new String(response, "UTF-8"));
	}
}
