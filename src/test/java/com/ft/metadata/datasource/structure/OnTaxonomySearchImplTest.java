package com.ft.metadata.datasource.structure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OnTaxonomySearchImplTest {
	@Mock
	private OnTaxonomyDataSource dataSource;
	
	private OnTaxonomySearchImpl unit;
	
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		unit = new OnTaxonomySearchImpl(dataSource);
	}
	
	@Test
	public void testTaxonomySearch() throws Exception {
		final String response = readContent("com/ft/metadata/datasource/structure/taxonomy-search-response.xml");
		final PostMethod post = mock(PostMethod.class);
		final HttpClient client = mock(HttpClient.class);
		
		when(post.getResponseBodyAsString()).thenReturn(response);		
		when(dataSource.getClient()).thenReturn(client);
		when(client.executeMethod(any(HostConfiguration.class), eq(post), any(HttpState.class))).thenReturn(200);
		when(dataSource.createRequest()).thenReturn(post);
		
		final List<OnTaxonomyTerm> terms = unit.search("HSBC");
		assertNotNull(terms);
		assertEquals(6,terms.size());
	}
	
	private static String readContent(final String filename) throws IOException {
		final StringBuffer buffer = new StringBuffer();
		final BufferedReader reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(filename)));
		try {
			String line = null;
			while((line = reader.readLine()) != null) {
				buffer.append(line);
				buffer.append("\n");
			}
			return buffer.toString();
		} finally {
			reader.close();			
		}
	}
}
