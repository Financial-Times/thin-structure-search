package com.ft.metadata.datasource.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.eidosmedia.datasource.DSException;
import com.eidosmedia.datasource.Datasource;
import com.eidosmedia.datasource.DatasourceManager;

public class OnTaxonomyDataSourceManager implements DatasourceManager {
	private static final Logger logger = Logger.getLogger(OnTaxonomyDataSourceManager.class);
	
	private Map<String,String> params = new HashMap<String,String>();
	private List<Datasource> datasources = new ArrayList<Datasource>();

	@SuppressWarnings({"unchecked","rawtypes"})
	public void init(final HashMap map) throws DSException {
		params.putAll((Map<String,String>)map);
	}

	public Datasource makeDatasource() throws DSException {
		final Datasource ds = new OnTaxonomyDataSource(params);
		datasources.add(ds);
		return ds;
	}
	
	public void close() {
		for(final Datasource ds : datasources) {
			try {
				ds.close();
			} catch( final DSException e ) {
				logger.warn("Failed to close ostensibly open Methode Structure Service datasource",e);
			}
		}
	}
}
