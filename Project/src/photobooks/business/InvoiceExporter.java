package photobooks.business;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.*;
import org.apache.pdfbox.pdmodel.edit.*;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import photobooks.application.Utility;
import photobooks.objects.Bill;
import photobooks.objects.Client;
import photobooks.objects.PhoneNumber;

public class InvoiceExporter {
	
	public static String formatDate(Calendar date) {
		String result = "";
		
		if (date != null)
			result = String.format("%d-%s-%d", date.get(Calendar.DAY_OF_MONTH), Utility.monthToString(date.get(Calendar.MONTH)), date.get(Calendar.YEAR));
		
		return result;
	}

	public static void export(Bill bill, String fileName) throws Exception {
		float dpi = 250;
		float scale = PDFHelper.dpiScale(dpi);
		float pageWidth = PDFHelper.pageWidth(dpi);
		float pageHeight = PDFHelper.pageHeight(dpi);
		float fontSize = 47.0f * scale;
		float titleFontSize = fontSize * 2.0f;
		float margin = dpi * 0.7f * scale;
		float bannerWidth = pageWidth - (margin * 2);
		float bannerHeight, bannerY;
		float barHeight = 60 * 0.8f * scale;
		float barColorG = 0.28f;
		float line1y, line2y;
		Color barColor = new Color(barColorG, barColorG, barColorG);
		String date = formatDate(bill.getDate());
		String type = bill.getType().toString();
		float typeStringLength;
		float dateWidth;
		
		float offset, offset2, offsetX, offsetY, oldX, oldY, newX, newY;
		ArrayList<String> customerLines = new ArrayList<String>();
		Client client = bill.getClient();
		String addressLine2 = "";
		float addressLineHeight = fontSize + (5.0f * scale);
		float addressHeight;
		
		customerLines.add(client.getFullName());
		
		if (client.getAddress() != null && client.getAddress().length() > 0)
			customerLines.add(client.getAddress());
		
		//Construct city, province, postal code
		if (client.getCity() != null && client.getCity().length() > 0)
			addressLine2 += client.getCity() + " ";
		
		if (client.getProvince() != null && client.getProvince().length() > 0)
			addressLine2 += client.getProvince() + " ";
		
		if (client.getPostalCode() != null && client.getPostalCode().length() > 0)
			addressLine2 += client.getPostalCode();
		
		addressLine2 = addressLine2.trim();
		
		if (addressLine2.length() > 0)
			customerLines.add(addressLine2);
		
		//Construct phone numbers
		if (client.getNumbers().size() > 0)
		{
			String phone = "Phone";
			
			for (PhoneNumber number : client.getNumbers())
			{
				phone += " " + Utility.formatPhoneNumber(number.getNumber());
			}
			
			customerLines.add(phone);
		}
		
		PDDocument document = new PDDocument();
		PDPage page = new PDPage(new PDRectangle(pageWidth, pageHeight));
		
		document.addPage(page);
		
		InputStream bannerIn = new FileInputStream("Banner.jpg");
		PDXObjectImage banner = new PDJpeg(document, bannerIn);
		
		bannerHeight = ((float)banner.getHeight() / (float)banner.getWidth()) * bannerWidth;
		bannerY = pageHeight - (bannerHeight + margin);
		
		PDFont font = PDFHelper.loadFont(document, "Arial.ttf");
		//PDFont fontBd = PDFHelper.loadFont(document, "Arialbd.ttf");
		
		dateWidth = font.getStringWidth(date) / 1000.0f;
		
		PDPageContentStream contentStream = new PDPageContentStream(document, page);

		// Define a text content stream using the selected font, moving the cursor and drawing the text "Hello World"
		//Draw Banner image
		contentStream.drawXObject(banner, margin, bannerY, bannerWidth, bannerHeight);
		
		//Draw bar below banner
		contentStream.setNonStrokingColor(barColor);
		contentStream.fillRect(margin, bannerY - barHeight, bannerWidth, barHeight);
		
		offset = 10.0f * scale;
		offset2 = 60.0f * scale;
		
		//Draw lines around client information
		offsetY = barHeight * 3;
		typeStringLength = titleFontSize * font.getStringWidth(type) / 1000.0f;
		
		line1y = bannerY - offsetY;
		line2y = line1y - (350.0f * scale);
		
		contentStream.drawLine(margin, line1y, pageWidth - (margin + (typeStringLength * 2) + offset2), line1y);
		contentStream.drawLine(pageWidth + offset2 - (margin + typeStringLength), line1y, pageWidth - margin, line1y);
		contentStream.drawLine(margin, line2y, pageWidth - margin, line2y);
		
		contentStream.beginText();
		
		//Draw Invoice/Quote
		oldX = pageWidth - (margin + (typeStringLength * 2));
		oldY = line1y - (30.0f * scale);
				
		contentStream.setFont(font, titleFontSize);
		contentStream.setNonStrokingColor(0, 0, 0);
		contentStream.moveTextPositionByAmount(oldX, oldY);
		contentStream.drawString(type);
		contentStream.moveTextPositionByAmount(-oldX, -oldY);
		
		//Draw date on bar below banner
		oldX = margin + bannerWidth - ((dateWidth * fontSize) + offset);
		oldY = bannerY + offset - barHeight;
		
		contentStream.setFont(font, fontSize);
		contentStream.setNonStrokingColor(255, 255, 255);
		contentStream.moveTextPositionByAmount(oldX, oldY);
		contentStream.drawString(date);
		contentStream.moveTextPositionByAmount(-oldX, -oldY);//Move to origin
		
		contentStream.endText();

		// Make sure that the content stream is closed:
		contentStream.close();
		
		document.save(fileName);
		document.close();
		
		bannerIn.close();
	}
}
