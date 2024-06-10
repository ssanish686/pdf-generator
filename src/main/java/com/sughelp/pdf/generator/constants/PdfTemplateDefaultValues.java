package com.sughelp.pdf.generator.constants;

/**
 * Default values to be used in template if not set by user.
 * 
 * @author Anish
 *
 * @since 09-May-2020
 */
public enum PdfTemplateDefaultValues {

	/**
	 * default size of text font : 7
	 */
	TEXT_FONT_SIZE(7f),
	/**
	 * Default ratio of table width : 1
	 */
	TABLE_WIDTH_RATIO(1f),
	/**
	 * default margin size : 30
	 */
	MARGIN(30f),
	/**
	 * default thickness of line : 0.5
	 */
	LINE_WIDTH(0.5f),
	/**
	 * default height of image : 50
	 */
	IMAGE_HEIGHT(50f),
	/**
	 * default width of image : 50
	 */
	IMAGE_WIDTH(50f);

	private String value;
	private int intValue;
	private float floatValue;

	PdfTemplateDefaultValues(String value) {
		this.value = value;
	}

	PdfTemplateDefaultValues(int value) {
		this.intValue = value;
	}

	PdfTemplateDefaultValues(float value) {
		this.floatValue = value;
	}

	public String getValue() {
		return value;
	}

	public int getIntValue() {
		return intValue;
	}

	public float getFloatValue() {
		return floatValue;
	}

}
