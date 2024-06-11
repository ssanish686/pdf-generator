package com.sughelp.pdf.generator;

import java.util.ArrayList;
import java.util.List;

import com.sughelp.pdf.generator.constants.PdfConstants;
import com.sughelp.pdf.generator.constants.PdfErrorDescription;
import com.sughelp.pdf.generator.constants.PdfTemplateDefaultValues;
import com.sughelp.pdf.generator.exception.PdfTemplateException;

/**
 * Column defines vertical section inside a row {@link Row}. So for a row which
 * is the horizontal section in a table, column corresponds to a vertical part
 * of it. So column can be defined as the base unit of a template.<br>
 * The column contains the actual content to be displayed in pdf. The content
 * can be a text or image.
 * 
 * <br>
 * <br>
 * 
 * 
 * @author Anish
 *
 * @since 08-May-2020
 */
public class Column {

	private String contentType;
	private String text;
	private String imageUrl;
	private String imageFile;
	private float imageWidth;
	private float imageHeight;
	private float[] textColorComponents;
	private boolean drawVerticalLine;
	private float[] lineColorComponents;
	private float lineThickness;
	private boolean isBold;
	private boolean isItalic;
	private float fontSize;
	private List<String> textLines;
	private float height;
	private String horizontalGravity;
	private String verticalGravity;

	public String getText() {
		return text;
	}

	/**
	 * Set the text value if the content type is <b>"text"</b>. If the content type
	 * is not text then this value has no relevance.
	 * 
	 */
	public void setText(String text) {
		this.text = text;
	}

	public boolean getDrawVerticalLine() {
		return drawVerticalLine;
	}

	/**
	 * If boundary is required for this column, set it to true. Column boundary
	 * includes only the vertical line at the right side of a column.
	 */
	public void setDrawVerticalLine(boolean drawEndLine) {
		this.drawVerticalLine = drawEndLine;
	}

	public boolean getIsBold() {
		return isBold;
	}

	/**
	 * If the text has to displayed in <b>Bold font</b>, set this flag to true.
	 */
	public void setIsBold(boolean isBold) {
		this.isBold = isBold;
	}

	public boolean getIsItalic() {
		return isItalic;
	}

	/**
	 * If the text has to displayed in <i>Italic font</i>, set this flag to true.
	 */
	public void setIsItalic(boolean isItalic) {
		this.isItalic = isItalic;
	}

	public float getFontSize() {
		if (fontSize == 0)
			return PdfTemplateDefaultValues.TEXT_FONT_SIZE.getFloatValue();
		return fontSize;
	}

	/**
	 * Set the font size of the text.<br>
	 * Default value : {@link PdfTemplateDefaultValues#TEXT_FONT_SIZE}
	 */
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	public float[] getLineColorComponents() {
		return lineColorComponents;
	}

	/**
	 * Set the color of the border line.
	 * 
	 * @param lineColorComponents
	 *            float Array which should contain RGB decimal code from (0,0,0) to
	 *            (255,255,255)
	 * @throws PdfTemplateException
	 *             if the RGB code is not set properly
	 */
	public void setLineColorComponents(float[] lineColorComponents) {
		if (lineColorComponents!= null && (lineColorComponents.length != 3 || lineColorComponents[0] < 0 || lineColorComponents[0] > 255
				|| lineColorComponents[1] < 0 || lineColorComponents[1] > 255 || lineColorComponents[2] < 0
				|| lineColorComponents[2] > 255))
			throw new PdfTemplateException(PdfErrorDescription.INVALID_RGB_COMPONENTS.getErrorDescription());
		this.lineColorComponents = lineColorComponents;
	}

	public float getLineThickness() {
		return lineThickness;
	}

	/**
	 * Set the thickness of the border line. <br>
	 * Default value : {@link PdfTemplateDefaultValues#LINE_WIDTH}
	 *
	 * @param lineThickness
	 */
	public void setLineThickness(float lineThickness) {
		this.lineThickness = lineThickness;
	}

	public float[] getTextColorComponents() {
		return textColorComponents;
	}

	/**
	 * Set the color of the text.
	 * 
	 * @param textColorComponents
	 *            float Array which should contain RGB decimal code from (0,0,0) to
	 *            (255,255,255)
	 * @throws PdfTemplateException
	 *             if the RGB code is not set properly
	 */
	public void setTextColorComponents(float[] textColorComponents) {
		if (textColorComponents != null && (textColorComponents.length != 3 || textColorComponents[0] < 0
				|| textColorComponents[0] > 255 || textColorComponents[1] < 0 || textColorComponents[1] > 255
				|| textColorComponents[2] < 0 || textColorComponents[2] > 255))
			throw new PdfTemplateException(PdfErrorDescription.INVALID_RGB_COMPONENTS.getErrorDescription());
		this.textColorComponents = textColorComponents;
	}

	List<String> getTextLines() {
		return textLines;
	}

	void setTextLines(List<String> textLines) {
		this.textLines = textLines;
	}

	void removeTextLines(int endIndex) {
		if (textLines != null) {
			List<String> tempList = new ArrayList<>();
			for (int i = 0; i < endIndex; i++)
				tempList.add(textLines.get(i));
			textLines.removeAll(tempList);
		}
	}

	float getHeight() {
		return height;
	}

	void setHeight(float height) {
		this.height = height;
	}

	public String getContentType() {
		if (contentType == null)
			return PdfConstants.CONTENT_TYPE_TEXT.getValue();
		return contentType;
	}

	/**
	 * Set the content type for the value in this column<br>
	 * Default content type : {@link PdfConstants#CONTENT_TYPE_TEXT}
	 * 
	 * @param contentType
	 *            The content type as specified in {@link PdfConstants} <br>
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getImageFile() {
		if (imageUrl == null)
			return imageFile;
		else
			return null;
	}

	public float getImageHeight() {
		if (imageHeight == 0)
			return PdfTemplateDefaultValues.IMAGE_HEIGHT.getFloatValue();
		return imageHeight;
	}

	public float getImageWidth() {
		if (imageWidth == 0)
			return PdfTemplateDefaultValues.IMAGE_WIDTH.getFloatValue();
		return imageWidth;
	}

	/**
	 * Set the image url for this column if the content type is <b>"image"</b>. If
	 * the content type is not image then this values has no relevance. If this
	 * value is set then imageFile will be ignored. Set this as null to use
	 * imageFile.
	 * 
	 * @param imageUrl
	 *            The url of the image file.
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * Set the image file for this column if the content type is <b>"image"</b>. If
	 * the content type is not image then this values has no relevance. If imageUrl
	 * value is set then value will be ignored.
	 * 
	 * @param imageFile
	 *            The file path of the image file.
	 */
	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}

	/**
	 * Set the width of the image for this column if the content type is
	 * <b>"image"</b>. If the content type is not image then this values has no
	 * relevance <br>
	 * Default value : {@link PdfTemplateDefaultValues#IMAGE_WIDTH}
	 * 
	 * @param width
	 *            Width of the image to be set in pdf.
	 */
	public void setImageWidth(float width) {
		this.imageWidth = width;
	}

	/**
	 * Set the height of the image for this column if the content type is
	 * <b>"image"</b>. If the content type is not image then this values has no
	 * relevance.<br>
	 * Default value : {@link PdfTemplateDefaultValues#IMAGE_HEIGHT}
	 * 
	 * @param height
	 *            Height of the image to be set in pdf.
	 */
	public void setImageHeight(float height) {
		this.imageHeight = height;
	}

	public String getHorizontalGravity() {
		if (horizontalGravity == null)
			return PdfConstants.COLUMN_GRAVITY_LEFT.getValue();
		return horizontalGravity;
	}

	/**
	 * Set the horizontal gravity of the content inside this column. Based on this
	 * value the content will horizontally align to left, center, or right of the
	 * column. If nothing is set the default gravity will be
	 * {@link PdfConstants#COLUMN_GRAVITY_LEFT}
	 * 
	 * @param gravity
	 *            The possible values are
	 *            {@link PdfConstants#COLUMN_GRAVITY_LEFT},{@link PdfConstants#COLUMN_GRAVITY_CENTER},
	 *            {@link PdfConstants#COLUMN_GRAVITY_RIGHT}
	 */
	public void setHorizontalGravity(String gravity) {
		this.horizontalGravity = gravity;
	}

	public String getVerticalGravity() {
		if (verticalGravity == null)
			return PdfConstants.COLUMN_GRAVITY_TOP.getValue();
		return verticalGravity;
	}

	/**
	 * Set the vertical gravity of the content inside this column. Based on this
	 * value the content will vertically align to top, center, or bottom of the
	 * column. If nothing is set the default gravity will be
	 * {@link PdfConstants#COLUMN_GRAVITY_TOP}. <br>
	 * The vertical gravity will be applied based on the row height. If all the
	 * columns in a row have same height then the vertical gravity will not have any
	 * impact. Only if any column height is greater than other, then all other
	 * column contents will be aligned to top, center or bottom based on the value
	 * set here.
	 * 
	 * @param verticalGravity
	 *            The possible values are
	 *            {@link PdfConstants#COLUMN_GRAVITY_TOP},{@link PdfConstants#COLUMN_GRAVITY_CENTER},
	 *            {@link PdfConstants#COLUMN_GRAVITY_BOTTOM}
	 */
	public void setVerticalGravity(String verticalGravity) {
		this.verticalGravity = verticalGravity;
	}

	void resetImage() {
		this.imageFile = null;
		this.imageUrl = null;
		this.contentType = null;
	}
}
