package com.sughelp.pdf.generator.exception;

/**
 * Unchecked exception thrown at runtime, if the input template is not set
 * properly
 * 
 * @author Anish
 *
 * @since 08-May-2020
 */
public class PdfTemplateException extends RuntimeException {

	private static final long serialVersionUID = 7108742652546355581L;

	public PdfTemplateException(String errorDescription) {
		super(errorDescription);
	}
	
	public PdfTemplateException(String errorDescription,Throwable t) {
		super(errorDescription,t);
	}
}
