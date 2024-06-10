package com.sughelp.pdf.generator.exception;

import com.sughelp.pdf.generator.constants.PdfErrorDescription;

/**
 * Checked exception thrown at compile time, if the template content exceeds the
 * page height
 * 
 * @author Anish
 *
 * @since 09-May-2020
 */
public class PdfPageSizeException extends Exception {

	private static final long serialVersionUID = -2667316301802517248L;

	public PdfPageSizeException() {
		super(PdfErrorDescription.PAGE_SIZE_EXCEEDED.getErrorDescription());
	}
}
