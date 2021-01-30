package org.opensrp.web.exceptions;

public class UploadValidationException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UploadValidationException(String error) {
		super(error);
	}
}
