package photobooks.business;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Calendar;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.*;
import org.apache.pdfbox.pdmodel.edit.*;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import photobooks.application.Utility;
import photobooks.objects.Bill;

public class InvoiceExporter {
	
	public static String formatDate(Calendar date) {
		String result = "";
		
		if (date != null)
			result = String.format("%d-%s-%d", date.get(Calendar.DAY_OF_MONTH), Utility.monthToString(date.get(Calendar.MONTH)), date.get(Calendar.YEAR));
		
		return result;
	}

	public static void export(Bill bill, String fileName) throws Exception {
		float dpi = 72;
		float scale = PDFHelper.dpiScale(dpi);
		float pageWidth = PDFHelper.pageWidth(dpi);
		float pageHeight = PDFHelper.pageHeight(dpi);
		float fontSize;
		float margin = 40 * scale;
		float bannerWidth = pageWidth - (margin * 2);
		float bannerHeight, bannerY;
		float barHeight = 15 * scale;
		float barColorG = 0.28f;
		Color barColor = new Color(barColorG, barColorG, barColorG);
		String date = formatDate(bill.getDate());
		float dateWidth;
		float offset;
		
		PDDocument document = new PDDocument();
		PDPage page = new PDPage(new PDRectangle(pageWidth, pageHeight));
		
		document.addPage(page);
		
		InputStream bannerIn = new FileInputStream("Banner.jpg");
		PDXObjectImage banner = new PDJpeg(document, bannerIn);
		
		bannerHeight = ((float)banner.getHeight() / (float)banner.getWidth()) * bannerWidth;
		bannerY = pageHeight - (bannerHeight + margin);
		
		PDFont font = PDFHelper.loadFont(document, "Arial.ttf");
		PDFont fontBd = PDFHelper.loadFont(document, "Arialbd.ttf");
		
		dateWidth = font.getStringWidth(date) / 1000.0f;
		
		PDPageContentStream contentStream = new PDPageContentStream(document, page);

		// Define a text content stream using the selected font, moving the cursor and drawing the text "Hello World"
		//Draw Banner image
		contentStream.drawXObject(banner, margin, bannerY, bannerWidth, bannerHeight);
		
		//Draw bar below banner
		contentStream.setNonStrokingColor(barColor);
		contentStream.fillRect(margin, bannerY - barHeight, bannerWidth, barHeight);
		
		contentStream.beginText();
		
		fontSize = 14 * scale;
		offset = 3.0f * scale;
		
		//Draw date on bar below banner
		contentStream.setFont(font, fontSize);
		contentStream.setNonStrokingColor(255, 255, 255);
		contentStream.moveTextPositionByAmount(margin + bannerWidth - ((dateWidth * fontSize) + offset), bannerY + offset - barHeight);
		contentStream.drawString(date);
		contentStream.moveTextPositionByAmount(-bannerWidth + ((dateWidth * fontSize) + offset), -offset);//Move to bottom left of bar
		
		contentStream.endText();

		// Make sure that the content stream is closed:
		contentStream.close();
		
		document.save(fileName);
		document.close();
		
		bannerIn.close();
	}
}
