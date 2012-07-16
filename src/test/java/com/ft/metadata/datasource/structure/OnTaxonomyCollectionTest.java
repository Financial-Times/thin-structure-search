package com.ft.metadata.datasource.structure;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eidosmedia.datasource.QueryTerm;
import com.eidosmedia.datasource.ResourceIterator;

public class OnTaxonomyCollectionTest {

	@Mock
	OnTaxonomyDataSource dataSource;

	OnTaxonomyCollection unit;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		unit = new OnTaxonomyCollection(dataSource);
	}

	@Test
	public void testQuery() throws Exception {
		final QueryTerm[] terms = { new QueryTerm("name", "HSBC") };
		final String[] viewElements = { "" };
		final PostMethod post = mock(PostMethod.class);
		final HttpClient client = mock(HttpClient.class);
		final String response = readContent("com/ft/metadata/datasource/structure/taxonomy-search-response.xml");

		when(dataSource.getClient()).thenReturn(client);
		when(dataSource.createRequest()).thenReturn(post);
		when(post.getResponseBodyAsString()).thenReturn(response);
		when(client.executeMethod(any(HostConfiguration.class), eq(post), any(HttpState.class))).thenReturn(200);

		final ResourceIterator resourceIterator = unit.query(terms, viewElements);
		String resource = new String(resourceIterator.next(0).getContent("UTF-8"), "UTF-8");
		List<OnTaxonomyTerm> onTaxonomyTerm = buildTermsFromXml(resource);

		assertEquals(onTaxonomyTerm.size(), 6);
		assertEquals(onTaxonomyTerm.get(0).getSedol(), "B0T4LH6");
		assertEquals(onTaxonomyTerm.get(0).getCanonicalName(), "HICL Infrastructure Co Ltd");
		assertEquals(onTaxonomyTerm.get(0).getCountry(), "United Kingdom");
		assertEquals(onTaxonomyTerm.get(0).getTickerSymbol(), "uk:HICL");
		assertEquals(onTaxonomyTerm.get(0).getExchangeCountry(), "uk");
		assertEquals(onTaxonomyTerm.get(0).getCompositeId(), "NDAwZTdmNTYtMmJlNy00ZjRhLThlMmUtMDRjMjM3ZDhhNTc1-T04=");
	}

	private List<OnTaxonomyTerm> buildTermsFromXml(String xml) throws Exception {
		StringBuffer buf = new StringBuffer();
		buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buf.append("<root>");
		buf.append(xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
		buf.append("</root>");
		List<OnTaxonomyTerm> terms = new LinkedList<OnTaxonomyTerm>();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new ByteArrayInputStream(buf.toString().getBytes("UTF-8")));
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("r");

		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			Element eElement = (Element) nNode;
			String wsodKey = getValueFromNode("wsodKey", eElement);
			String ftCode = getValueFromNode("FTCode", eElement);
			String sedol = getValueFromNode("SEDOL", eElement);
			String tickerSymbol = getValueFromNode("Ticker", eElement);
			String country = getValueFromNode("Country", eElement);
			String name = getValueFromNode("Name", eElement);
			String compositeId = getValueFromNode("Composite-Id", eElement);
			String exchangeCountry = getValueFromNode("Exchange-Country", eElement);
			terms.add(new OnTaxonomyTerm.Builder().ftWsodKey(wsodKey).ftCndCode(ftCode).sedol(sedol).compositeId(compositeId)
					.tickerSymbol(tickerSymbol).country(country).canonicalName(name).exchangeCountry(exchangeCountry)
					.build());
		}
		return terms;
	}

	private String getValueFromNode(String tag, Element element) {
		Node node = element.getElementsByTagName(tag).item(0);
		if (node != null) {
			NodeList nlList = node.getChildNodes();
			Node nValue = (Node) nlList.item(0);
			if (nValue != null) {
				return nValue.getNodeValue();
			}
		}

		return null;
	}

	private static String readContent(final String filename) throws IOException {
		final InputStream is = ClassLoader.getSystemResourceAsStream(filename);
		return new Scanner(is).useDelimiter("\\A").next();
	}
}
