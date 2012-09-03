package com.ft.metadata.datasource.structure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class FtpsiDtdValidationTest {

	@Test
	public void validateGoodDtd() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		documentBuilder.setErrorHandler(new ErrorHandler());
		documentBuilder.parse(ClassLoader.getSystemResourceAsStream("story.xml"));
	}

	@Test
	public void failValidationForBadDtd() throws Exception {
		boolean thrown = false;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(true);
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			documentBuilder.setErrorHandler(new ErrorHandler());
			documentBuilder.parse(ClassLoader.getSystemResourceAsStream("story-bad-dtd.xml"));
		}
		catch (SAXException e) {
			thrown = true;
			assertEquals("Element type \"company\" already has attribute \"id\" of type ID, a second attribute \"CompositeId\" of type ID is not permitted.", e.getMessage());
		}
		assertTrue(thrown);
	}

	class ErrorHandler implements org.xml.sax.ErrorHandler {

		@Override
		public void warning(SAXParseException exception) throws SAXException {
			throw new SAXException(exception.getMessage());
		}

		@Override
		public void error(SAXParseException exception) throws SAXException {
			throw new SAXException(exception.getMessage());
		}

		@Override
		public void fatalError(SAXParseException exception) throws SAXException {
			throw new SAXException(exception.getMessage());
		}

	}
}
