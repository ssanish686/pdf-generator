package com.sughelp.pdf.generator.constants;

/**
 * Constant values which is used for Pdf template generation.
 * 
 * @author Anish
 *
 * @since 09-May-2020
 */
public enum PdfConstants {

	/**
	 * value : <b>image</b>
	 */
	CONTENT_TYPE_IMAGE("image"),
	/**
	 * value : <b>text</b>
	 */
	CONTENT_TYPE_TEXT("text"),
	/**
	 * value : <b>center</b>
	 */
	COLUMN_GRAVITY_CENTER("center"),
	/**
	 * value : <b>left</b>
	 */
	COLUMN_GRAVITY_LEFT("left"),
	/**
	 * value : <b>right</b>
	 */
	COLUMN_GRAVITY_RIGHT("right"),
	/**
	 * value : <b>top</b>
	 */
	COLUMN_GRAVITY_TOP("top"),
	/**
	 * value : <b>bottom</b>
	 */
	COLUMN_GRAVITY_BOTTOM("bottom");

	private String value;

	PdfConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
