package photobooks.business;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;

import photobooks.application.Utility;
import photobooks.objects.Bill;
import photobooks.objects.BillPackage;
import photobooks.objects.IBillItem;
import photobooks.objects.ProductPackage;

public class PDFHelper {
	
	public enum HorizontalAlignment { LEFT, RIGHT, CENTER }
	public enum VerticalAlignment { TOP, BOTTOM, CENTER }

	public final static String[] fontPaths = new String[] { "./", "/Library/Fonts/", "/System/Library/Fonts/", "$HOME/Library/Fonts/", "C:/Windows/Fonts/" };
	
	public static final float aspectRatio = 11.0f / 8.5f;
	public static final float layoutDpi = 250;
	
	public static float dpiScale(float dpi) { return dpi / layoutDpi; }
	
	public static float pageWidth(float dpi) { return 8.5f * dpi; }
	public static float pageHeight(float dpi) { return pageWidth(dpi) * aspectRatio; }
	
	public static void drawTableHeader(PDPageContentStream stream, float x, float y, float rowSize, PDFont font, float fontSize, float[] columnWidths, String[] items) throws IOException {
		float offsetX = x;

		stream.setNonStrokingColor(255, 255, 255);
		
		for (int i = 0; i < columnWidths.length; ++i) {
			PDRectangle rect = new PDRectangle(columnWidths[i], rowSize);
			
			rect.move(offsetX, y);
			//drawBox(stream, rect);
			
			if (i < items.length)
			{
				stream.beginText();
				drawString(stream, rect, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, items[i], font, fontSize);
				stream.endText();
			}
			
			offsetX += columnWidths[i];
		}
	}
	
	public static float drawBillItem(PDPageContentStream stream, float x, float y, float rowSize, PDFont font, float fontSize, float[] columnWidths, String[] items, float margin) throws IOException {
		float result = rowSize;
		float dollarSize = getStringWidth("  $", font, fontSize);
		PDRectangle rect = new PDRectangle(1, rowSize);
		HorizontalAlignment hAlign = HorizontalAlignment.CENTER;
		
		rect.move(x, y);

		stream.setNonStrokingColor(0);
		
		for (int i = 0; i < columnWidths.length; ++i) {
			rect.setUpperRightX(rect.getLowerLeftX() + columnWidths[i]);
			
			//drawBox(stream, rect);
			
			if (i < items.length)
			{
				ArrayList<String> lines;
				float offset = 0.0f;
				PDRectangle drawRect = new PDRectangle(rect.getWidth() - (2.0f * margin), rect.getHeight() - (2.0f * margin));
				drawRect.move(rect.getLowerLeftX() + margin, rect.getLowerLeftY() + margin);
				
				if (i > 1)
					offset = dollarSize;
				
				lines = fitStringToWidth(items[i], font, fontSize, drawRect.getWidth() - offset);
				result = Math.max(result, rowSize * lines.size());
				
				stream.beginText();
				
				if (i > 1)
					drawString(stream, drawRect, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, "  $", font, fontSize);
				
				for (String str : lines)
				{
					drawString(stream, drawRect, hAlign, VerticalAlignment.CENTER, str, font, fontSize);
					drawRect.move(0, -rowSize);
				}
				
				stream.endText();
			}
			
			if (i == 0)
				hAlign = HorizontalAlignment.LEFT;
			else
				hAlign = HorizontalAlignment.RIGHT;
			
			rect.move(columnWidths[i], 0);
		}
		
		return result;
	}
	
	public static int linesRequiredForBillItem(String[] content, float[] columnWidths, PDFont font, float fontSize, float margin) throws IOException {
		int lines = 1;
		int i = 0;
		
		for (String str : content)
		{
			if (i < columnWidths.length)
			{
				lines = Math.max(lines, fitStringToWidth(str, font, fontSize, columnWidths[i] - (margin * 2.0f)).size());
			}
			
			++i;
		}
		
		return lines;
	}
	
	public static int linesRequiredForAllBillItems(ArrayList<String[]> content, float[] columnWidths, PDFont font, float fontSize, float margin) throws IOException {
		int lines = 0;
		
		for (String[] strings : content)
		{
			lines += linesRequiredForBillItem(strings, columnWidths, font, fontSize, margin);
		}
		
		return lines;
	}
	
	public static void drawBill(PDPageContentStream stream, PDRectangle rect, PDFont font, float fontSize, float scale, Bill bill) throws IOException {
		float qtyWidth = 200.0f * scale, priceWidth = 300.0f * scale, totalWidth = 300.0f * scale;
		float itemWidth = rect.getWidth() - (qtyWidth + priceWidth + totalWidth), margin = 5.0f * scale;
		String[] headerRow = new String[] { "QTY", "ITEM", "PRICE", "TOTAL" };
		float[] columnWidths = new float[] { qtyWidth, itemWidth, priceWidth, totalWidth };
		float rowSize = fontSize + (margin * 2.0f);
		ArrayList<IBillItem> items = bill.getItems();
		ArrayList<String[]> content = new ArrayList<String[]>();
		float y;
		int i;
		int numRows, totalRows = Math.round(rect.getHeight() / rowSize), remainingRows;
		
		for (IBillItem item : items) {
			content.add(new String[] { String.valueOf(item.getAmount()), item.getItem().getName(), Utility.formatMoneyExport(item.getPrice()), Utility.formatMoneyExport(item.total()) });
			
			if (item.isPackage())
			{
				BillPackage pack = (BillPackage)item;
				ArrayList<ProductPackage> products = pack.getPackage().getProducts();
				
				for (ProductPackage prod : products) {
					content.add(new String[] { String.valueOf(prod.getAmount()), "  " + prod.getProduct().getName() });
				}
			}
		}
		
		numRows = linesRequiredForAllBillItems(content, columnWidths, font, fontSize, margin);
		
		if (numRows > totalRows - 2) {
			float newRowSize = rect.getHeight() / (numRows + 2);
			float rowScale = newRowSize / rowSize;
			
			rowSize = newRowSize;
			margin *= rowScale;
			
			fontSize = rowSize - (margin * 2.0f);
			totalRows = Math.round(rect.getHeight() / rowSize);
			
			numRows = linesRequiredForAllBillItems(content, columnWidths, font, fontSize, margin);
		}
		
		remainingRows = Math.max(totalRows - (numRows + 2), 0);
		
		if (remainingRows > numRows) {
			ArrayList<String[]> newContent = new ArrayList<String[]>();
			int j = 0;
			float rowsPerItem = remainingRows / (float)numRows;
			float rowsLeft = rowsPerItem;
			int rowsAfter;
			int rowsBefore;
			
			System.out.println("RowsPerItem = " + rowsPerItem);
			
			for (j = 0; j < content.size(); ++j) {
				rowsAfter = Math.round(rowsLeft / 2.0f);
				rowsBefore = (int)rowsLeft - rowsAfter;
				
				for (i = 0; i < rowsBefore; ++i)
					newContent.add(new String[] {});
				
				newContent.add(content.get(j));
				
				for (i = 0; i < rowsAfter; ++i)
					newContent.add(new String[] {});
				
				rowsLeft += rowsPerItem - (rowsAfter + rowsBefore);
			}
			
			content = newContent;
		}
		else {
			ArrayList<String[]> newContent = new ArrayList<String[]>();
			float rowOffset = numRows / (float)remainingRows;
			float currRow = rowOffset;
			
			for (int j = 0; j < content.size(); ++j) {
				newContent.add(content.get(j));

				if (j >= currRow) {
					newContent.add(new String[] {});
					currRow += rowOffset;
				}
			}
			
			content = newContent;
		}
		
		stream.setNonStrokingColor(InvoiceExporter.barColorG);
		stream.fillRect(rect.getLowerLeftX(), rect.getUpperRightY() - rowSize, rect.getWidth(), rowSize);
		stream.fillRect(rect.getLowerLeftX(), rect.getLowerLeftY(), rect.getWidth(), rowSize);
		
		stream.setFont(font, fontSize);
		drawTableHeader(stream, rect.getLowerLeftX(), rect.getUpperRightY() - rowSize, rowSize, font, fontSize, columnWidths, headerRow);

		stream.setNonStrokingColor(0);
		drawBox(stream, rect);
		
		i = 0;
		y = rect.getUpperRightY() - rowSize;
		
		stream.drawLine(rect.getLowerLeftX(), y, rect.getUpperRightX(), y);
		y -= rowSize;
		
		for (String[] strings : content) {
			y -= drawBillItem(stream, rect.getLowerLeftX(), y, rowSize, font, fontSize, columnWidths, strings, margin);
			stream.drawLine(rect.getLowerLeftX(), y + rowSize, rect.getUpperRightX(), y + rowSize);
			++i;
		}

		stream.drawLine(rect.getLowerLeftX(), y + rowSize, rect.getUpperRightX(), y + rowSize);
		
		for (; y >= rect.getLowerLeftY(); y -= rowSize)
		{
			stream.drawLine(rect.getLowerLeftX(), y, rect.getUpperRightX(), y);
		}
	}
	
	public static void drawBox(PDPageContentStream stream, PDRectangle rect) throws IOException {
		//Draw top, bottom, left, right
		stream.drawLine(rect.getLowerLeftX(), rect.getUpperRightY(), rect.getUpperRightX(), rect.getUpperRightY());
		stream.drawLine(rect.getLowerLeftX(), rect.getLowerLeftY(), rect.getUpperRightX(), rect.getLowerLeftY());
		stream.drawLine(rect.getLowerLeftX(), rect.getLowerLeftY(), rect.getLowerLeftX(), rect.getUpperRightY());
		stream.drawLine(rect.getUpperRightX(), rect.getLowerLeftY(), rect.getUpperRightX(), rect.getUpperRightY());
	}
	
	public static void drawString(PDPageContentStream stream, float x, float y, String str) throws IOException {
		stream.moveTextPositionByAmount(x, y);
		stream.drawString(str);
		stream.moveTextPositionByAmount(-x, -y);
	}
	
	public static void drawString(PDPageContentStream stream, PDRectangle rect, HorizontalAlignment horizontalAlign, VerticalAlignment verticalAlign, String str, PDFont font, float fontSize) throws IOException {
		float strLength = fontSize * font.getStringWidth(str) / 1000.0f;
		float x = 0, y = 0;
		
		x = getStringX(rect, horizontalAlign, strLength);
		y = getStringY(rect, verticalAlign, fontSize * 0.7f);
		
		drawString(stream, x, y, str);
	}
	
	public static float getStringY(PDRectangle rect, VerticalAlignment verticalAlign, float strHeight) {
		float y = 0;
		
		switch (verticalAlign)
		{
			case TOP:
				y = rect.getUpperRightY() - strHeight;
				break;
			case BOTTOM:
				y = rect.getLowerLeftY();
				break;
			case CENTER:
				y = rect.getLowerLeftY() + ((rect.getHeight() - strHeight) / 2.0f);
				break;
		}
		
		return y;
	}
	
	public static float getStringX(PDRectangle rect, HorizontalAlignment horizontalAlign, float strLength) {
		float x = 0;
		
		switch (horizontalAlign)
		{
			case LEFT:
				x = rect.getLowerLeftX();
				break;
			case RIGHT:
				x = rect.getUpperRightX() - strLength;
				break;
			case CENTER:
				x = rect.getLowerLeftX() + ((rect.getWidth() - strLength) / 2.0f);
				break;
		}
		
		return x;
	}
	
	public static ArrayList<String> fitStringToWidth(String str, PDFont font, float fontSize, float maxWidth) throws IOException {
		ArrayList<String> out = new ArrayList<String>();
		int firstChar = -1, lastWhiteSpace = -1;
		int index = 0;
		
		if (str.length() == 0 || getStringWidth(str, font, fontSize) <= maxWidth)
			out.add(str);
		else
		{
			for (char ch : str.toCharArray())
			{
				if (Character.isWhitespace(ch))
				{
					if (firstChar >= 0)
						lastWhiteSpace = index;
				}
				else if (firstChar == -1)
					firstChar = index;
				
				if (getStringWidth(str.substring(0, index + 1), font, fontSize) > maxWidth)
				{
					if (lastWhiteSpace >= 0)
					{
						out.add(str.substring(0, lastWhiteSpace));
						
						if (str.length() > lastWhiteSpace + 1)
							out.addAll(fitStringToWidth(str.substring(lastWhiteSpace + 1), font, fontSize, maxWidth));
					}
					else if (index == 0)
					{
						out.add(str.substring(0, 1));
						
						if (str.length() > 1)
							out.addAll(fitStringToWidth(str.substring(1), font, fontSize, maxWidth));
					}
					else
					{
						out.add(str.substring(0, index));
						
						if (str.length() > index)
							out.addAll(fitStringToWidth(str.substring(index), font, fontSize, maxWidth));
					}
					
					//This line should always execute if str.width > maxWidth
					break;
				}
				
				++index;
			}
		}
		
		return out;
	}
	
	public static float getStringWidth(String str, PDFont font, float fontSize) throws IOException { return fontSize * font.getStringWidth(str) / 1000.0f; }
	
	public static PDFont loadFont(PDDocument document, String fontName) {
		PDFont font = null;
		
		for (String path : fontPaths)
		{
			String str = path + fontName;
			
			try
			{
				File file = new File(str);
				
				if (file.exists())
				{
					font = PDTrueTypeFont.loadTTF(document, str);
				
					if (font != null)
						break;
				}
			}
			catch (Exception ex)
			{
				System.out.println("Error reading font: " + ex.toString());
			}
		}
		
		return font;
	}
}
