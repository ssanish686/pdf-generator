package com.sughelp.pdf.generator;

import java.util.List;

import com.sughelp.pdf.generator.constants.PdfTemplateDefaultValues;

/**
 * The template for which the pdf will be generated. It contains list of tables
 * ({@link Table}), top and bottom margin of a page. <br/>
 * Any section the user has to write in the PDF should be configured as a table
 * ({@link Table}). This table will be converted to corresponding sections in
 * pdf. The table will be processed in the order it is stored in the list and in
 * same order the corresponding sections will be displayed in pdf. <br/>
 * <br/>
 * The structure is : {@link Template} => {@link Table} => {@link Row} =>
 * {@link Column}
 * 
 * 
 * @author Anish
 *
 * @since 09-May-2020
 */
public class Template {

	private List<Table> tables;
	private float topMargin;
	private float bottomMargin;

	public List<Table> getTables() {
		return tables;
	}

	/**
	 * List of tables to be displayed as a horizontal section in a pdf. The tables
	 * will be displayed in the order it is stored in the list. (FIFO)
	 */
	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	public float getTopMargin() {
		if (topMargin == 0)
			return PdfTemplateDefaultValues.MARGIN.getFloatValue();
		return topMargin;
	}

	/**
	 * Top margin of first table in a page. Basically it is the y coordinate of the
	 * first table row in a page from top. Other tables position will be relative to
	 * this first table.<br/>
	 * Default value : {@link PdfTemplateDefaultValues#MARGIN}
	 * 
	 * @param topMargin
	 *            the y-coordinate of the first table row.
	 */
	public void setTopMargin(float topMargin) {
		this.topMargin = topMargin;
	}

	public float getBottomMargin() {
		if (bottomMargin == 0)
			return PdfTemplateDefaultValues.MARGIN.getFloatValue();
		return bottomMargin;
	}

	/**
	 * Bottom margin of last table in a page. Basically it is the y coordinate of
	 * the last table row in a page from bottom.<br/>
	 * Default value : {@link PdfTemplateDefaultValues#MARGIN}
	 * 
	 * @param bottomMargin
	 *            the y-coordinate of the last table row including it row height.
	 */
	public void setBottomMargin(float bottomMargin) {
		this.bottomMargin = bottomMargin;
	}
}
