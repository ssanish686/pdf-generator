package com.sughelp.pdf.generator;

import java.util.List;

import com.sughelp.pdf.generator.constants.PdfErrorDescription;
import com.sughelp.pdf.generator.constants.PdfTemplateDefaultValues;
import com.sughelp.pdf.generator.exception.PdfTemplateException;

/**
 * The table is the major part of a pdf template {@link Template}. Each section
 * in a pdf is represented by a table in the template. Each table contains
 * multiple rows. The rows will be processed in the order it is stored in the
 * list and in same order the corresponding rows will be displayed in pdf
 * sections. <br/>
 * <br/>
 * The structure is : {@link Template} => {@link Table} => {@link Row} =>
 * {@link Column}
 * 
 * @author Anish
 *
 * @since 08-May-2020
 */
public class Table {

	private List<Row> rows;
	private int totalColumnCount;
	private boolean drawBoundary;
	private float[] boundaryColorComponents;
	private float boundaryThickness;
	private List<Float> columnWidthRatios;
	private float widthRatio;
	private float width;
	private float height;
	private float leftMargin;
	private float rightMargin;
	private float topMargin;
	private float yPositionFromBottom;

	public List<Row> getRows() {
		return rows;
	}

	/**
	 * List of rows to be displayed as a horizontal section in a table. The rows
	 * will be displayed in the order it is stored in the list. (FIFO)
	 */
	public void setRows(List<Row> rows) {
		this.rows = rows;
	}

	public int getTotalColumnCount() {
		return totalColumnCount;
	}

	/**
	 * It is the total vertical section inside a table.
	 */
	public void setTotalColumnCount(int totalColumnCount) {
		this.totalColumnCount = totalColumnCount;
	}

	public boolean getDrawBoundary() {
		return drawBoundary;
	}

	/**
	 * If boundary is required for this table set it to true. Table boundary
	 * includes only the outer four borders. Inner column or row border will not be
	 * considered as part of this value.
	 */
	public void setDrawBoundary(boolean drawBoundary) {
		this.drawBoundary = drawBoundary;
	}

	public List<Float> getColumnWidthRatios() {
		return columnWidthRatios;
	}

	/**
	 * List containing the ratios of the width of column inside a table. Based on
	 * this value the column are horizontally placed inside the table. The sum of
	 * all the ratio should be equal to 1. If not set then all the column will be
	 * equally divided and have same width.<br/>
	 * The size of the list set here should be same as the totalColumnCount value
	 * set in the table.</br>
	 * <b>Example</b> : If a table contain two column and if you want column1 to be
	 * 75% of the table width and column2 remaining width, then set the list with
	 * values 0.75 and 0.25. Then column1 will be set with 75% of table width and
	 * column2 with 25% of table width
	 *
	 */
	public void setColumnWidthRatios(List<Float> columnWidthRatios) {
		this.columnWidthRatios = columnWidthRatios;
	}

	void setWidth(float width) {
		this.width = width;
	}

	void setHeight(float height) {
		this.height = height;
	}

	float getWidth() {
		return width;
	}

	float getHeight() {
		return height;
	}

	public float getWidthRatio() {
		if (widthRatio == 0)
			return PdfTemplateDefaultValues.TABLE_WIDTH_RATIO.getFloatValue();
		return widthRatio;
	}

	/**
	 * Width ratio of the table inside a page. Based on this value the width of
	 * table is calculated in a page. The width of the table is exclusive of its
	 * margin. i.e margin width will not be reduced or increased based on this
	 * ratio.<br/>
	 * If widthRatio is not set the default width of table will be the width of the
	 * page minus its margin size.<br/>
	 * <b>Example:</b> If you want the table width to occupy only half of the page
	 * width then set widthRatio as 0.5
	 * 
	 * @param widthRatio
	 *            The width ratio to be set. It should be greater than 0 and less
	 *            than or equal to 1
	 */
	public void setWidthRatio(float widthRatio) {
		this.widthRatio = widthRatio;
	}

	public float[] getBoundaryColorComponents() {
		return boundaryColorComponents;
	}

	/**
	 * Set the color of the boundary line.
	 * 
	 * @param boundaryColorComponents
	 *            float Array which should contain RGB decimal code from (0,0,0) to
	 *            (255,255,255)
	 * @throws PdfTemplateException
	 *             if the RGB code is not set properly
	 */
	public void setBoundaryColorComponents(float[] boundaryColorComponents) {
		if (boundaryColorComponents != null && (boundaryColorComponents.length != 3 || boundaryColorComponents[0] < 0
				|| boundaryColorComponents[0] > 255 || boundaryColorComponents[1] < 0
				|| boundaryColorComponents[1] > 255 || boundaryColorComponents[2] < 0
				|| boundaryColorComponents[2] > 255))
			throw new PdfTemplateException(PdfErrorDescription.INVALID_RGB_COMPONENTS.getErrorDescription());
		this.boundaryColorComponents = boundaryColorComponents;
	}

	public float getBoundaryThickness() {
		return boundaryThickness;
	}

	/**
	 * Set the thickness of the boundary line. <br/>
	 * Default value : {@link PdfTemplateDefaultValues#LINE_WIDTH}
	 *
	 * @param boundaryThickness
	 */
	public void setBoundaryThickness(float boundaryThickness) {
		this.boundaryThickness = boundaryThickness;
	}

	public float getLeftMargin() {
		if (leftMargin == 0)
			return PdfTemplateDefaultValues.MARGIN.getFloatValue();
		return leftMargin;
	}

	/**
	 * Table margin size from the left side of the page. <br/>
	 * Default value : {@link PdfTemplateDefaultValues#MARGIN}
	 */
	public void setLeftMargin(float leftMargin) {
		this.leftMargin = leftMargin;
	}

	public float getRightMargin() {
		if (rightMargin == 0)
			return PdfTemplateDefaultValues.MARGIN.getFloatValue();
		return rightMargin;
	}

	/**
	 * Table margin size from the right side of the page. <br/>
	 * Default value : {@link PdfTemplateDefaultValues#MARGIN}
	 */
	public void setRightMargin(float rightMargin) {
		this.rightMargin = rightMargin;
	}

	float getyPositionFromBottom() {
		return yPositionFromBottom;
	}

	/**
	 * Used to set the y coordinate (from bottom) of the table dynamically. <br/>
	 * For first table in the page it will be the page height minus top margin.
	 * <br/>
	 * For other tables below the first table this value will be calculated
	 * dynamically based on the height of first table created
	 */
	void setyPositionFromBottom(float yPositionFromBottom) {
		this.yPositionFromBottom = yPositionFromBottom;
	}

	public float getTopMargin() {
		return topMargin;
	}

	/**
	 * Margin between this table and the table above it or top margin of the page
	 * (if this is the first table in the page). <br/>
	 * Default value : 0
	 */
	public void setTopMargin(float topMargin) {
		this.topMargin = topMargin;
	}

}
