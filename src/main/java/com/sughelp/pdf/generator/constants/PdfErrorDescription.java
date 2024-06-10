package com.sughelp.pdf.generator.constants;

/**
 * @author Anish
 *
 * @since 08-May-2020
 */
public enum PdfErrorDescription {

	INVALID_FILE("The output pdf file is not valid"),
	INVALID_TEMPLATE("The template structure is not valid"),
	NO_ROWS_DEFINED("Now rows are defined in the table. Please define rows"), //
	NO_COLUMNS_DEFINED("Now columns are defined in the row. Please define columns"), //
	TOTAL_COLUMN_COUNT_EMPTY("Total column value is zero. Please provide a value for it"), //
	INVALID_COLUMN_RATIO_COUNT("The total number of columns and column ratio must be same"), //
	INVALID_COLUMN_WIDTH_RATIO("The sum of column width ratio must be 1"), //
	INVALID_TABLE_WIDTH_RATIO("The table width ratio value must be between 0 and 1"), //
	INVALID_RGB_COMPONENTS(
			"The color component should be a float array of size 3 and should contain RGB value from (0,0,0) to (255,255,255)"), //
	PAGE_SIZE_EXCEEDED("Page size exceeded"); //

	private String errorDescrption;

	PdfErrorDescription(String errorDescription) {
		this.errorDescrption = errorDescription;
	}

	public String getErrorDescription() {
		return errorDescrption;
	}
}
