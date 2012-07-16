package com.ft.metadata.datasource.structure;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class OnTaxonomySearchImpl implements OnTaxonomySearch {
	private static final String APPLICATION_XML = "application/xml";
	private static final String UTF8 = "UTF-8";
	private static final String COMPOSITE_ID_DELIMITER = "-";

	// @formatter:off
	private static final String CONTENT_WITH_PLACEHOLDER =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
				"<search:searchRequest"+
				" xmlns:search=\"http://metadata.internal.ft.com/metadata/xsd/metadata_search_v1.0.xsd\""+
				" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""+
				" xsi:schemaLocation=\"http://metadata.internal.ft.com/metadata/xsd/metadata_search_v1.0.xsd metadata_search_v1.0.xsd\">"+
					"<search:query>%s</search:query>"+
					"<search:searchContext>"+
					"<search:taxonomy>ON</search:taxonomy>"+
					"</search:searchContext>"+
				"</search:searchRequest>";
	// @formatter:on

	private final OnTaxonomyDataSource dataSource;
	private static final Map<String, String> substitutes = new LinkedHashMap<String, String>();
	static{
		substitutes.put("&", "&amp;");
		substitutes.put("<", "&lt;");
	}

	public OnTaxonomySearchImpl(final OnTaxonomyDataSource dataSource) {
		this.dataSource = dataSource;
	}

	// Tear apart the content as XML while ignoring schemas
	private static List<OnTaxonomyTerm> processResponse(final String response) throws OnTaxonomySearchException {
		final List<OnTaxonomyTerm> terms = new ArrayList<OnTaxonomyTerm>();
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final InputStream input = new ByteArrayInputStream(response.getBytes());
			final Document doc = builder.parse(input);

			// Completely dumb prefix behaviour
			final NodeList list = doc.getElementsByTagName("tx:term");
			for (int index = 0; index < list.getLength(); index++) {
				final Element termNode = (Element) list.item(index);
				final String externalTermId = termNode.getAttribute("tm:externalTermId");
				final NodeList canonicalNames = termNode.getElementsByTagName("tm:canonicalName");
				final NodeList termAttributes = termNode.getElementsByTagName("tm:attribute");
				final String taxonomy = termNode.getAttribute("tm:taxonomy");
				final OnTaxonomyTerm term = createTerm(externalTermId, canonicalNames, termAttributes, taxonomy);
				if (term != null) {
					terms.add(term);
				}
			}
			return terms;
		} catch (final ParserConfigurationException e) {
			throw new OnTaxonomySearchException("Failed to prepare for XML parsing", e);
		} catch (final IOException e) {
			throw new OnTaxonomySearchException("Failed (IO) during XML parsing", e);
		} catch (final SAXException e) {
			throw new OnTaxonomySearchException("Failed (SAX) during XML parsing", e);
		}
	}

	private static OnTaxonomyTerm createTerm(final String externalTermId, final NodeList canonicalNames, final NodeList termAttributes, final String taxonomy) throws OnTaxonomySearchException {
		final String canonicalName = extractCanonicalName(externalTermId,canonicalNames);
		final String compositeId = buildCompositeTermId(taxonomy, externalTermId);
		final Map<String, String> attributes = mapAttributes(termAttributes);
		final String cmrStatus = attributes.get("cmrStatus");
		final boolean active = cmrStatus != null ? "ACTIVE".equalsIgnoreCase(cmrStatus.trim()) : false;
		final String isCompany = attributes.get("is-company");
		final boolean company = isCompany != null ? "Yes".equalsIgnoreCase(isCompany.trim()) : false;
		if (company && active) {
			return new OnTaxonomyTerm.Builder().ftWsodKey(attributes.get("ft-wsod-key"))
					.ftCndCode(attributes.get("ft-code-from-cnd")).sedol(attributes.get("sedol"))
					.tickerSymbol(attributes.get("ft-wsod-key")).country(attributes.get("country"))
					.type(attributes.get("Type")).canonicalName(canonicalName).compositeId(compositeId)
					.exchangeCountry(attributes.get("exchange-country")).build();
		}
		return null;
	}

	private static Map<String, String> mapAttributes(final NodeList termAttributes) {
		final Map<String, String> map = new HashMap<String, String>();
		for (int index = 0; index < termAttributes.getLength(); index++) {
			final Element termAttribute = (Element) termAttributes.item(index);
			map.put(termAttribute.getAttribute("tm:name"), termAttribute.getTextContent());
		}
		return map;
	}

	private static String extractCanonicalName(final String externalTermId, final NodeList canonicalNames) throws OnTaxonomySearchException {
		if (canonicalNames.getLength() >= 1) {
			return ((Element) canonicalNames.item(0)).getTextContent();
		} else {
			throw new OnTaxonomySearchException("No canonical name for term " + externalTermId);
		}
	}

	private static String buildCompositeTermId(final String externalTermId, final String taxonomy) throws OnTaxonomySearchException {
		try {
			return new StringBuffer().append(encodeString(taxonomy)).
			append(COMPOSITE_ID_DELIMITER).
			append(encodeString(externalTermId)).toString();
		}
		catch (UnsupportedEncodingException e) {
			throw new OnTaxonomySearchException(String.format("composite term id build failed for externalTermId %s and taxonomy %s ", externalTermId,  taxonomy));
		}
	}

	private static String encodeString(String text) throws UnsupportedEncodingException{
		return new String(Base64.encodeBase64(text.getBytes("UTF-8")));
	}

	public List<OnTaxonomyTerm> search(final String query) throws OnTaxonomySearchException {
		final HttpState state = dataSource.createAuthenticationState();
		try {
			final PostMethod post = dataSource.createRequest();
			applyQueryText(post,query);
			final int status = dataSource.getClient().executeMethod(new HostConfiguration(), post, state);
			if (status == HttpStatus.SC_OK) {
				return processResponse(post.getResponseBodyAsString());
			} else {
				throw new OnTaxonomySearchException("Search failed with status " + status);
			}
		} catch (final UnsupportedEncodingException e) {
			throw new OnTaxonomySearchException("String encoding failed while creating the query body", e);
		} catch (final HttpException e) {
			throw new OnTaxonomySearchException("Failed during conversation with service", e);
		} catch (final IOException e) {
			throw new OnTaxonomySearchException("Failed during conversation with service", e);
		}
	}

	public void applyQueryText(final PostMethod post, final String query) throws UnsupportedEncodingException {
		final String requestContent = String.format(CONTENT_WITH_PLACEHOLDER, escapeForXml(query));
		final RequestEntity requestEntity = new StringRequestEntity(requestContent, APPLICATION_XML, UTF8);
		post.setRequestEntity(requestEntity);
	}

	private static String escapeForXml(final String content) {
		for (final Map.Entry<String, String> subs : substitutes.entrySet()){
			content.replaceAll(subs.getKey(), subs.getValue());
		}
		return content;
	}
}
