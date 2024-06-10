package com.sughelp.pdf.generator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sughelp.pdf.generator.constants.PdfConstants;
import com.sughelp.pdf.generator.constants.PdfErrorDescription;
import com.sughelp.pdf.generator.constants.PdfTemplateDefaultValues;
import com.sughelp.pdf.generator.exception.PdfPageSizeException;
import com.sughelp.pdf.generator.exception.PdfTemplateException;

/**
 * Creates pdf based on the input template. A template should contain all the
 * data and the specifications to create a pdf.<br/>
 * Please refer {@link Template} for more details on the values to be populated.
 * 
 * @author Anish
 *
 * @since 08-May-2020
 */
public class PdfGenerator implements AutoCloseable {

	private static final Logger logger = LoggerFactory.getLogger(PdfGenerator.class);

	private PDDocument pdfDoc;
	private PDPage page;
	private PDPageContentStream contentStream;

	private float pageTopMargin;
	private float pageBottomMargin;
	private float pageHeight;
	private float pageUsedHeight;

	/**
	 *
	 */
	private PDDocument createDocument() {
		pdfDoc = new PDDocument();
		return pdfDoc;
	}

	/**
	 * Creates a pdf document with values given in the template and save it to the
	 * file specified. Here the template is a String content.
	 * 
	 * @param template
	 *            The template for which pdf will be generated.
	 * @param file
	 *            The output file where the created pdf will be saved.
	 * @throws IOException
	 * @throws PdfTemplateException
	 *             if the input template is not set with proper value
	 */
	public void createPdf(String template, String file) throws IOException, PdfTemplateException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			createPdf(mapper.readValue(template, Template.class), file);
		} catch (JsonParseException | JsonMappingException e) {
			throw new PdfTemplateException(PdfErrorDescription.INVALID_TEMPLATE.getErrorDescription(), e);
		}
	}

	/**
	 * Creates and return pdf byte array with contents given in the template. Here
	 * the template is a String content.
	 * 
	 * @param template
	 *            The template for which pdf byte array will be generated.
	 * @param fileName
	 *            The name for the pdf file to set in meta data.
	 * @throws IOException
	 * @throws PdfTemplateException
	 *             if the input template is not set with proper value
	 */
	public byte[] createPdfByteArray(String template, String fileName) throws IOException, PdfTemplateException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return createPdfByteArray(mapper.readValue(template, Template.class), fileName);
		} catch (JsonParseException | JsonMappingException e) {
			throw new PdfTemplateException(PdfErrorDescription.INVALID_TEMPLATE.getErrorDescription(), e);
		}
	}

	/**
	 * Creates a pdf document with data given in the template and save it to the
	 * file specified. Here the template is a Java class {@link Template}.
	 * 
	 * @param template
	 *            The template for which pdf will be generated
	 * @param file
	 *            The output file where the created pdf will be saved.
	 * @throws IOException
	 * @throws PdfTemplateException
	 *             if the input template is not set with proper value
	 */
	public void createPdf(Template template, String file) throws IOException, PdfTemplateException {
		createDocument();
		setPdfInformation(getFileName(file));
		logger.info("Document created");
		createNewPage();
		pageTopMargin = template.getTopMargin();
		pageBottomMargin = template.getBottomMargin();
		logger.debug("Input template top margin = {} and bottom margin = {}", pageTopMargin, pageBottomMargin);
		for (int i = 0; i < template.getTables().size(); i++) {
			Table table = template.getTables().get(i);
			// condition to set tables y position of tables
			table.setyPositionFromBottom(pageHeight - pageTopMargin - table.getTopMargin() - pageUsedHeight);
			createTable(table);
			logger.info("Table content created successfully for :: table{}", i + 1);
			logger.debug("table{} height in the current page = {}", i + 1, table.getHeight());
			pageUsedHeight += table.getHeight() + table.getTopMargin();
			if (pageUsedHeight >= pageHeight)
				pageUsedHeight = 0;
			logger.info("Page used height = {}", pageUsedHeight);
		}
		closeContentStream();
		logger.info("saving pdf file :: {}", file);
		pdfDoc.save(file);
	}

	/**
	 * Creates and return pdf byte array with content given in the template. Here
	 * the template is a Java class {@link Template}.
	 * 
	 * @param template
	 *            The template for which pdf byte array will be generated
	 * @param fileName
	 *            The name for the pdf file to set in meta data.
	 * @throws IOException
	 * @throws PdfTemplateException
	 *             if the input template is not set with proper value
	 */
	public byte[] createPdfByteArray(Template template, String fileName) throws IOException, PdfTemplateException {
		createDocument();
		setPdfInformation(fileName);
		logger.info("Document created");
		createNewPage();
		pageTopMargin = template.getTopMargin();
		pageBottomMargin = template.getBottomMargin();
		logger.debug("Input template top margin = {} and bottom margin = {}", pageTopMargin, pageBottomMargin);
		for (int i = 0; i < template.getTables().size(); i++) {
			Table table = template.getTables().get(i);
			// condition to set tables y position of tables
			table.setyPositionFromBottom(pageHeight - pageTopMargin - table.getTopMargin() - pageUsedHeight);
			createTable(table);
			logger.info("Table content created successfully for :: table{}", i + 1);
			logger.debug("table{} height in the current page = {}", i + 1, table.getHeight());
			pageUsedHeight += table.getHeight() + table.getTopMargin();
			if (pageUsedHeight >= pageHeight)
				pageUsedHeight = 0;
			logger.info("Page used height = {}", pageUsedHeight);
		}
		closeContentStream();
		logger.info("saving pdf byte array...");
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		pdfDoc.save(byteArrayOutputStream);
		return byteArrayOutputStream.toByteArray();
	}

	private String getFileName(String file) {
		try {
			return file.substring(file.lastIndexOf("/") + 1, file.lastIndexOf("."));
		} catch (Exception e) {
			logger.error(PdfErrorDescription.INVALID_FILE.getErrorDescription(), e);
			throw new PdfTemplateException(PdfErrorDescription.INVALID_FILE.getErrorDescription());
		}
	}

	private void setPdfInformation(String fileName) {
		PDDocumentInformation pdi = pdfDoc.getDocumentInformation();
		pdi.setAuthor("Anish Kumar SS");
		pdi.setTitle(fileName);
		pdi.setSubject(fileName);
		pdi.setCreator("Sughelp\u00AE");
		pdi.setProducer("Sughelp\u00AE PDF Generator");
		Calendar date = Calendar.getInstance();
		pdi.setCreationDate(date);
		pdi.setModificationDate(date);
		pdi.setKeywords("Sughelp");
	}

	private void closeContentStream() throws IOException {
		if (contentStream != null)
			contentStream.close();
	}

	private void createNewPage() throws IOException {
		logger.info("******* creating new page *******");
		page = new PDPage();
		pdfDoc.addPage(page);
		closeContentStream();
		contentStream = new PDPageContentStream(pdfDoc, page);
		pageHeight = page.getMediaBox().getHeight();
		logger.info("Height = {} , Width = {} ", pageHeight, page.getMediaBox().getWidth());
		// reset page used height to zero when creating a new page
		pageUsedHeight = 0;
		logger.info("******* page created *******");
	}

	private Table createTable(Table table) throws IOException {
		validateTableData(table);
		float tableWidth = calculateTableWidth(table, table.getLeftMargin(), table.getRightMargin(), page);
		table.setWidth(tableWidth);
		HashMap<Integer, Float> columnWidthMap = new HashMap<>();
		validateColumnWidthRatio(table, columnWidthMap, tableWidth);
		createRows(table, columnWidthMap);
		drawTableBorder(table, table.getLeftMargin(), table.getyPositionFromBottom(), table.getWidth(),
				table.getHeight());
		return table;
	}

	private void validateTableData(Table table) {
		if (table.getRows() == null)
			throw new PdfTemplateException(PdfErrorDescription.NO_ROWS_DEFINED.getErrorDescription());
		if (table.getTotalColumnCount() == 0)
			throw new PdfTemplateException(PdfErrorDescription.TOTAL_COLUMN_COUNT_EMPTY.getErrorDescription());
		if (table.getColumnWidthRatios() != null && table.getColumnWidthRatios().size() != table.getTotalColumnCount())
			throw new PdfTemplateException(PdfErrorDescription.INVALID_COLUMN_RATIO_COUNT.getErrorDescription());
	}

	private float calculateTableWidth(Table table, float leftMargin, float rightMargin, PDPage page) {
		float tableWidth;
		if (table.getWidthRatio() < 0 || table.getWidthRatio() > 1)
			throw new PdfTemplateException(PdfErrorDescription.INVALID_TABLE_WIDTH_RATIO.getErrorDescription());
		tableWidth = (page.getMediaBox().getWidth() - (leftMargin + rightMargin)) * table.getWidthRatio();
		return tableWidth;
	}

	private void validateColumnWidthRatio(Table table, HashMap<Integer, Float> columnWidthMap, float tableWidth) {
		float totalColumnWidthRatio = 0;
		if (table.getColumnWidthRatios() == null || table.getColumnWidthRatios().isEmpty()) {
			for (int i = 0; i < table.getTotalColumnCount(); i++)
				columnWidthMap.put(i, tableWidth / (float) table.getTotalColumnCount());
			totalColumnWidthRatio = 1;
		} else {
			for (int i = 0; i < table.getColumnWidthRatios().size(); i++) {
				float ratio = table.getColumnWidthRatios().get(i);
				float size = tableWidth * ratio;
				columnWidthMap.put(i, size);
				totalColumnWidthRatio = totalColumnWidthRatio + ratio;
			}
		}
		if (totalColumnWidthRatio < 0.99 || totalColumnWidthRatio > 1.01)
			throw new PdfTemplateException(PdfErrorDescription.INVALID_COLUMN_WIDTH_RATIO.getErrorDescription());

	}

	private void createRows(Table table, HashMap<Integer, Float> columnWidthMap) throws IOException {
		final float cellXMargin = 3;
		final float cellYMargin = 3;
		float textx = table.getLeftMargin() + cellXMargin;
		float texty = table.getyPositionFromBottom() - cellYMargin;
		table.setHeight(0);
		for (int i = 0; i < table.getRows().size(); i++) {

			Row row = table.getRows().get(i);
			if (row.getColumns() == null)
				throw new PdfTemplateException(PdfErrorDescription.NO_COLUMNS_DEFINED.getErrorDescription());

			convertColumnTextToLines(table, row, columnWidthMap, cellXMargin, cellYMargin);
			row.setHeight(calculateRowHeight(row.getColumns(), cellYMargin));

			// if the page finishes while creating the column, then this boolean
			// will become true.If this boolean is true then a new page will be created as
			// shown in below while loop and the remaining content of the row will be
			// written in new page and it continues till all the content is written
			boolean createNewPage = createColumns(table, row, columnWidthMap, textx, texty, cellXMargin, cellYMargin);

			// resetting the column X position to initial position after creating one row.
			textx = table.getLeftMargin() + cellXMargin;
			// column Y position will be the next row position
			texty -= row.getHeight();
			table.setHeight(table.getHeight() + row.getHeight());
			drawCellBorder(row, table.getLeftMargin(), table.getyPositionFromBottom() - table.getHeight(),
					table.getWidth(), columnWidthMap);

			while (createNewPage) {
				logger.info("Page height exceeded while creating row{}", i + 1);
				// draw boundary of the table of previous page
				drawTableBorder(table, table.getLeftMargin(), table.getyPositionFromBottom(), table.getWidth(),
						table.getHeight());
				// create new page
				createNewPage();
				// reset table properties after creating new page
				resetTableOnNewPageStart(table);
				textx = table.getLeftMargin() + cellXMargin;
				texty = table.getyPositionFromBottom() - cellYMargin;
				row.setHeight(calculateRowHeight(row.getColumns(), cellYMargin));
				// continue writing the contents to new page.
				createNewPage = createColumns(table, row, columnWidthMap, textx, texty, cellXMargin, cellYMargin);
				// resetting the column X position to initial position after creating one row.
				textx = table.getLeftMargin() + cellXMargin;
				// column Y position will be the next row position
				texty -= row.getHeight();
				table.setHeight(table.getHeight() + row.getHeight());

				// draw cell border of first row in new page
				drawCellBorder(row, table.getLeftMargin(), table.getyPositionFromBottom() - table.getHeight(),
						table.getWidth(), columnWidthMap);
			}

		}

	}

	// resetting the table y position in new page to top margin position, as in new
	// page the table will start from top margin irrespective of it's start position
	// in previous page. Also the height and top margin of table will be zero as the
	// table is started from top in new page.
	private void resetTableOnNewPageStart(Table table) {
		table.setyPositionFromBottom(pageHeight - pageTopMargin);
		table.setHeight(0);
		table.setTopMargin(0);
	}

	private void convertColumnTextToLines(Table table, Row row, HashMap<Integer, Float> columnWidthMap,
			float cellXMargin, float cellYMargin) throws IOException {
		for (int j = 0; j < table.getTotalColumnCount(); j++) {
			Column column = row.getColumns().get(j);
			float fontSize = column.getFontSize();
			PDFont pdfFont = getFontType(column, row.getIsHeader());
			float colWidth = columnWidthMap.get(j);
			// subtracting with one more cellXMargin for extra margin at end of cell.
			// Otherwise
			// the cell value is touching the column right border
			float colTextMaxWidth = colWidth - (2 * cellXMargin) - cellXMargin;
			String text = column.getText() == null ? "" : column.getText();
			text = text.replace("\r", "");
			List<String> multilines = splitTextToMultiLines(pdfFont, fontSize, colTextMaxWidth, text);
			column.setTextLines(multilines);
		}
	}

	private float calculateRowHeight(List<Column> columns, float cellYMargin) {
		float rowHeight = 0;
		for (Column column : columns) {
			float columnHeight;
			if (column.getContentType().equals(PdfConstants.CONTENT_TYPE_IMAGE.getValue()))
				columnHeight = column.getImageHeight() + cellYMargin * 2;
			else {
				float textHeight = column.getFontSize() * getFontType(column, false).getFontDescriptor().getCapHeight()
						/ 1000;
				columnHeight = (textHeight + cellYMargin) * (column.getTextLines().size() + 1);
			}
			column.setHeight(columnHeight);
			if (rowHeight < column.getHeight())
				rowHeight = column.getHeight();
		}
		return rowHeight;
	}

	private boolean createColumns(Table table, Row row, HashMap<Integer, Float> columnWidthMap, float textx,
			float texty, float cellXMargin, float cellYMargin) throws IOException {
		float columnHeight = 0;
		boolean isPageFinished = false;
		for (int j = 0; j < table.getTotalColumnCount(); j++) {
			Column column = row.getColumns().get(j);
			float fontSize = column.getFontSize();
			PDFont pdfFont = getFontType(column, row.getIsHeader());
			contentStream.setFont(pdfFont, fontSize);
			float textHeight = fontSize * pdfFont.getFontDescriptor().getCapHeight() / 1000;
			float colWidth = columnWidthMap.get(j);
			if (column.getContentType().equals(PdfConstants.CONTENT_TYPE_IMAGE.getValue())) {
				columnHeight = column.getImageHeight() + cellYMargin;
				if ((colWidth - 2 * cellXMargin) < column.getImageHeight())
					column.setImageWidth(colWidth - 2 * cellXMargin);
				float textXPosition = textx
						+ calculateHorizontalGravityDelta(column, colWidth, column.getImageWidth(), cellXMargin);
				float textYPosition = texty - columnHeight;
				if (row.getHeight() > column.getHeight())
					textYPosition = textYPosition
							- calculateVerticalGravityDelta(column, row.getHeight(), columnHeight, cellYMargin);
				drawImage(textXPosition, textYPosition, column.getImageUrl(), column.getImageFile(),
						column.getImageWidth(), column.getImageHeight());
				// Once the image is drawn, we will not render it again in case of
				// PdfPageSizeException. We will ignore it. We will reset the image once
				// rendered, so if this row is called again due to PdfPageSizeException
				// for text values, this image will not drawn again
				column.resetImage();
			} else {
				columnHeight = textHeight + cellYMargin;
				List<String> textLines = column.getTextLines();
				int lineNo = 0;
				for (int k = 0; k < textLines.size(); k++) {
					try {
						float textWidth = fontSize * pdfFont.getStringWidth(textLines.get(k)) / 1000;
						float textXPosition = textx
								+ calculateHorizontalGravityDelta(column, colWidth, textWidth, cellXMargin);
						float textYPosition = texty - columnHeight;
						// Previously calculated row height and column height. Max column height is the
						// row height. So, if row height and column height are same, then for that
						// column we will not do vertical alignment as that is the base column and all
						// other column's vertical alignment will be based on that.
						if (row.getHeight() > column.getHeight())
							textYPosition = textYPosition
									- calculateVerticalGravityDelta(column, row.getHeight(), columnHeight, cellYMargin);
						writeText(textXPosition, textYPosition, textLines.get(k), column.getTextColorComponents());
						columnHeight = columnHeight + textHeight + cellYMargin;
						lineNo++;
					} catch (PdfPageSizeException e) {
						logger.debug("column{} :: {}", j + 1, e.getMessage());
						// even if page is finished while writing one cell, we should continue with
						// remaining cell of the row. And after finishing all the cells, we will inform
						// the caller
						// that page has finished, so the calling method will create new page.
						isPageFinished = true;
						row.setHeight(columnHeight);
						break;
					}
				}
				// from the current cell remove all the text lines which are
				// already written
				column.removeTextLines(lineNo);
			}
			textx += colWidth;
		}

		return isPageFinished;
	}

	private float calculateHorizontalGravityDelta(Column column, float colWidth, float contentWidth,
			float cellXMargin) {
		// 2 * cellXMargin is also subtracted from colWidth, so that the newly aligned
		// content will also maintain horizontal margin
		if (column.getHorizontalGravity().equals(PdfConstants.COLUMN_GRAVITY_CENTER.getValue()))
			return (colWidth - contentWidth - cellXMargin * 2) / 2;
		else if (column.getHorizontalGravity().equals(PdfConstants.COLUMN_GRAVITY_RIGHT.getValue()))
			return (colWidth - contentWidth - cellXMargin * 2);
		else
			return 0;
	}

	private float calculateVerticalGravityDelta(Column column, float colHeight, float contentHeight,
			float cellYMargin) {
		// 2 * cellYMargin is also subtracted from colHeight, so that the newly aligned
		// content will also maintain vertical margin
		if (column.getVerticalGravity().equals(PdfConstants.COLUMN_GRAVITY_CENTER.getValue()))
			return (colHeight - contentHeight - cellYMargin * 2) / 2;
		else if (column.getVerticalGravity().equals(PdfConstants.COLUMN_GRAVITY_BOTTOM.getValue()))
			return (colHeight - contentHeight - cellYMargin * 2);
		else
			return 0;
	}

	private PDFont getFontType(Column column, boolean isHeaderRow) {
		if (isHeaderRow)
			return PDType1Font.TIMES_BOLD;
		else if (column.getIsBold() && column.getIsItalic())
			return PDType1Font.TIMES_BOLD_ITALIC;
		else if (column.getIsBold())
			return PDType1Font.TIMES_BOLD;
		else if (column.getIsItalic())
			return PDType1Font.TIMES_ITALIC;
		else
			return PDType1Font.TIMES_ROMAN;
	}

	private List<String> splitTextToMultiLines(PDFont pdfFont, float fontSize, float colTextMaxWidth, String text)
			throws IOException {
		List<String> textParts = new ArrayList<>();
		String[] nextlineTexts = null;
		if (text.contains("\n"))
			nextlineTexts = text.split("\n");
		else
			nextlineTexts = new String[] { text };
		for (String newText : nextlineTexts) {
			char[] textCharArray = newText.toCharArray();
			StringBuilder textPartBuilder = new StringBuilder();
			for (int charPosition = 0; charPosition < textCharArray.length; charPosition++) {
				textPartBuilder.append(textCharArray[charPosition]);
				if ((fontSize * pdfFont.getStringWidth(textPartBuilder.toString()) / 1000) > colTextMaxWidth) {
					// to word wrap based on space between words
					if (textPartBuilder.toString().contains(" ") && charPosition + 1 < textCharArray.length
							&& textCharArray[charPosition + 1] != ' ') {
						textParts.add(textPartBuilder.substring(0, textPartBuilder.lastIndexOf(" ")).trim());
						textPartBuilder.delete(0, textPartBuilder.lastIndexOf(" "));
					} else {
						textParts.add(textPartBuilder.toString().trim());
						textPartBuilder.delete(0, textPartBuilder.length());
					}
				}
			}
			// adding the last part
			if (textPartBuilder.length() > 0)
				textParts.add(textPartBuilder.toString().trim());
		}

		return textParts;

	}

	private void drawTableBorder(Table table, float xPositionFromLeft, float yPositionFromBottom, float tableWidth,
			float tableHeight) throws IOException {
		if (table.getDrawBoundary()) {
			// table top horizontal border
			drawLine(xPositionFromLeft, yPositionFromBottom, xPositionFromLeft + tableWidth, yPositionFromBottom,
					table.getBoundaryColorComponents(), table.getBoundaryThickness());
			// table bottom horizontal border
			drawLine(xPositionFromLeft, yPositionFromBottom - tableHeight, xPositionFromLeft + tableWidth,
					yPositionFromBottom - tableHeight, table.getBoundaryColorComponents(),
					table.getBoundaryThickness());
			// table left vertical border
			drawLine(xPositionFromLeft, yPositionFromBottom, xPositionFromLeft, yPositionFromBottom - tableHeight,
					table.getBoundaryColorComponents(), table.getBoundaryThickness());
			// table right vertical border
			drawLine(xPositionFromLeft + tableWidth, yPositionFromBottom, xPositionFromLeft + tableWidth,
					yPositionFromBottom - tableHeight, table.getBoundaryColorComponents(),
					table.getBoundaryThickness());

		}
	}

	private void drawCellBorder(Row row, float xPositionFromLeft, float yPositionFromBottom, float tableWidth,
			HashMap<Integer, Float> columnWidthMap) throws IOException {
		if (row.getDrawBottomLine())
			drawLine(xPositionFromLeft, yPositionFromBottom, xPositionFromLeft + tableWidth, yPositionFromBottom,
					row.getLineColorComponents(), row.getLineThickness());

		float widthIncrement = 0;
		for (int j = 0; j < row.getColumns().size(); j++) {
			Column column = row.getColumns().get(j);
			widthIncrement += columnWidthMap.get(j);
			if (column.getDrawVerticalLine())
				drawLine(xPositionFromLeft + widthIncrement, yPositionFromBottom, xPositionFromLeft + widthIncrement,
						yPositionFromBottom + row.getHeight(), column.getLineColorComponents(),
						column.getLineThickness());
		}
	}

	private void writeText(float xPositionFromLeft, float yPositionFromBottom, String text, float[] colorComponents)
			throws IOException, PdfPageSizeException {
		logger.debug("writing text :: xPositionFromLeft = {} yPositionFromBottom = {}", xPositionFromLeft,
				yPositionFromBottom);
		// Writing the current text is exceeding the page height limit. So throw
		// exception to handle it
		if (yPositionFromBottom <= pageBottomMargin)
			throw new PdfPageSizeException();

		if (colorComponents != null) {
			if (colorComponents.length != 3 || colorComponents[0] < 0 || colorComponents[0] > 255
					|| colorComponents[1] < 0 || colorComponents[1] > 255 || colorComponents[2] < 0
					|| colorComponents[2] > 255)
				throw new PdfTemplateException(PdfErrorDescription.INVALID_RGB_COMPONENTS.getErrorDescription());
			float[] components = new float[] { colorComponents[0] / 255f, colorComponents[1] / 255f,
					colorComponents[2] / 255f };
			PDColor color = new PDColor(components, PDDeviceRGB.INSTANCE);
			contentStream.setNonStrokingColor(color);
		} else {
			PDColor color = new PDColor(new float[] { 0, 0, 0 }, PDDeviceRGB.INSTANCE);
			contentStream.setNonStrokingColor(color);
		}
		contentStream.beginText();
		contentStream.newLineAtOffset(xPositionFromLeft, yPositionFromBottom);
		contentStream.showText(text);
		contentStream.endText();
	}

	private void drawLine(float fromX, float fromY, float toX, float toY, float[] colorComponents, float lineWidth)
			throws IOException {
		if (colorComponents != null) {
			if (colorComponents.length != 3 || colorComponents[0] < 0 || colorComponents[0] > 255
					|| colorComponents[1] < 0 || colorComponents[1] > 255 || colorComponents[2] < 0
					|| colorComponents[2] > 255)
				throw new PdfTemplateException(PdfErrorDescription.INVALID_RGB_COMPONENTS.getErrorDescription());
			float[] components = new float[] { colorComponents[0] / 255f, colorComponents[1] / 255f,
					colorComponents[2] / 255f };
			PDColor color = new PDColor(components, PDDeviceRGB.INSTANCE);
			contentStream.setStrokingColor(color);
		} else {
			PDColor color = new PDColor(new float[] { 0, 0, 0 }, PDDeviceRGB.INSTANCE);
			contentStream.setStrokingColor(color);
		}
		if (lineWidth > 0)
			contentStream.setLineWidth(lineWidth);
		else
			contentStream.setLineWidth(PdfTemplateDefaultValues.LINE_WIDTH.getFloatValue());
		contentStream.moveTo(fromX, fromY);
		contentStream.lineTo(toX, toY);
		contentStream.stroke();
	}

	private void drawImage(float xPositionFromLeft, float yPositionFromBottom, String imageUrl, String imageFile,
			float width, float height) throws IOException {
		logger.debug("drawing image :: width = {}, height = {}", width, height);
		PDImageXObject pdImage = null;
		if (imageFile != null)
			pdImage = PDImageXObject.createFromFile(imageFile, pdfDoc);
		else if (imageUrl != null) {
			byte[] imageBytes = Util.downloadFile(imageUrl);
			if (imageBytes != null)
				pdImage = PDImageXObject.createFromByteArray(pdfDoc, imageBytes, null);
		}
		if (pdImage != null)
			contentStream.drawImage(pdImage, xPositionFromLeft, yPositionFromBottom, width, height);
		else
			logger.warn("unable to draw image for image url :: {} , file :: {}", imageUrl, imageFile);
	}

	@Override
	public void close() throws IOException {
		if (pdfDoc != null)
			pdfDoc.close();
		logger.info("Document closed!!!");
	}

}
