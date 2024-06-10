package com.sughelp.pdf.generator;

import java.util.List;

import com.sughelp.pdf.generator.constants.PdfErrorDescription;
import com.sughelp.pdf.generator.constants.PdfTemplateDefaultValues;
import com.sughelp.pdf.generator.exception.PdfTemplateException;

/**
 * Row defines horizontal section inside a table {@link Table}. So for a table
 * which is the main section in a pdf, row corresponds to a horizontal part
 * whose width will be same as that of table width.<br/>
 * One row contains multiple columns. The columns will be processed in the order
 * it is stored in the list and in same order it will be displayed in pdf. <br/>
 * <br/>
 * The structure is : {@link Template} => {@link Table} => {@link Row} =>
 * {@link Column}
 * 
 * @author Anish
 *
 * @since 08-May-2020
 */
public class Row {

	private List<Column> columns;
	private boolean isHeader;
	private boolean drawBottomLine;
	private float[] lineColorComponents;
	private float lineThickness;
	private float height;

	public List<Column> getColumns() {
		return columns;
	}

	/**
	 * List of columns to be displayed as a vertical section in a row. The columns
	 * will be displayed in the order it is stored in the list. (FIFO)
	 */
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public boolean getIsHeader() {
		return isHeader;
	}

	/**
	 * If the row is a header set this flag as true. Header rows will have all the
	 * texts in bold font.
	 */
	public void setIsHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}

	public boolean getDrawBottomLine() {
		return drawBottomLine;
	}

	/**
	 * If boundary is required for this row set it to true. Row boundary includes
	 * only the bottom line of a row. Inner column border will not be considered as
	 * part of this value.
	 */
	public void setDrawBottomLine(boolean drawBottomLine) {
		this.drawBottomLine = drawBottomLine;
	}

	public float[] getLineColorComponents() {
		return lineColorComponents;
	}

	/**
	 * Set the color of the boundary line.
	 * 
	 * @param lineColorComponents
	 *            float Array which should contain RGB decimal code from (0,0,0) to
	 *            (255,255,255)
	 * @throws PdfTemplateException
	 *             if the RGB code is not set properly
	 */
	public void setLineColorComponents(float[] lineColorComponents) {
		if (lineColorComponents != null && (lineColorComponents.length != 3 || lineColorComponents[0] < 0 || lineColorComponents[0] > 255
				|| lineColorComponents[1] < 0 || lineColorComponents[1] > 255 || lineColorComponents[2] < 0
				|| lineColorComponents[2] > 255))
			throw new PdfTemplateException(PdfErrorDescription.INVALID_RGB_COMPONENTS.getErrorDescription());
		this.lineColorComponents = lineColorComponents;
	}

	public float getLineThickness() {
		return lineThickness;
	}

	/**
	 * Set the thickness of the boundary line. <br/>
	 * Default value : {@link PdfTemplateDefaultValues#LINE_WIDTH}
	 *
	 * @param lineThickness
	 */
	public void setLineThickness(float lineThickness) {
		this.lineThickness = lineThickness;
	}

	float getHeight() {
		return height;
	}

	void setHeight(float height) {
		this.height = height;
	}
}
