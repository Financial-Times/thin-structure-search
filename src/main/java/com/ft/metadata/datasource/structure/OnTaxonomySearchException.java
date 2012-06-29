package com.ft.metadata.datasource.structure;

public class OnTaxonomySearchException extends Exception {
	private static final long serialVersionUID = -3741805523987490190L;

	public OnTaxonomySearchException(final String message) {
		super(message);
	}

	public OnTaxonomySearchException(final String message, final Throwable cause) {
		super(message,cause);
	}
}
