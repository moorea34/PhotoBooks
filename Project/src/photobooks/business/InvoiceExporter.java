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

	public static float barColorG = 0.28f;
	
	public static String formatDate(Calendar date) {
		String result = "";
		
		if (date != null)
			result = String.format("%d-%s-%d", date.get(Calendar.DAY_OF_MONTH), Utility.monthToString(date.get(Calendar.MONTH)), date.get(Calendar.YEAR));
		
		return result;
	}

	public static void export(Bill bill, String fileName) throws Exception {
		//Page vars
		float dpi = 250;
		float scale = PDFHelper.dpiScale(dpi);
		float pageWidth = PDFHelper.pageWidth(dpi);
		float pageHeight = PDFHelper.pageHeight(dpi);
		float fontSize = 40.0f * scale;
		float titleFontSize = fontSize * 2.0f;
		float margin = 180.0f * scale;
		
		//BannerVars
		float bannerWidth = pageWidth - (margin * 2);
		float bannerHeight, bannerY;
		float barHeight = 60 * 0.8f * scale;
		float line1y, line2y;
		Color barColor = new Color(barColorG, barColorG, barColorG);
		String date = formatDate(bill.getDate());
		String type = bill.getType().toString();
		String number = "#" + bill.getID();
		float typeStringLength, numberLength;
		float dateWidth;
		
		//Client info vars
		float offset, offset2, offsetX, offsetY, oldX, oldY;
		ArrayList<String> customerLines = new ArrayList<String>();
		Client client = bill.getClient();
		String addressLine2 = "";
		float addressLineHeight = fontSize, addressLineOffset = 20.0f * scale;
		float addressHeight = 0, addressBoxHeight, addressY = 0;
		
		//Footer vars
		String[] footerLines = new String[] { "204-997-8080", "ashley@kaboha.com", "www.kaboha.com" };
		float footerLineHeight = fontSize + (20.0f * scale);
		float footerBarY = margin + (footerLineHeight * footerLines.length);
		float footerBarHeight = barHeight / 2.0f;
		PDRectangle footerLine = new PDRectangle(pageWidth - (margin * 2.0f), footerLineHeight);
		
		//Table vars
		PDRectangle tableBox = new PDRectangle(bannerWidth, 1000.0f * scale);
		PDRectangle totalBox, taxesBox, labelBox;
		
		footerLine.move(margin, margin);
		
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
			int i = 0;
			
			for (PhoneNumber phoneNumber : client.getNumbers())
			{
				if (i > 0)
					phone += ", ";
				else
					phone += " ";
				
				phone += Utility.formatPhoneNumber(phoneNumber.getNumber());
				++i;
			}
			
			customerLines.add(phone);
		}
		
		addressHeight = (customerLines.size() * addressLineHeight) + ((customerLines.size() - 1) * addressLineOffset);
		
		PDDocument document = new PDDocument();
		PDPage page = new PDPage(new PDRectangle(pageWidth, pageHeight));
		
		document.addPage(page);
		
		InputStream bannerIn = new FileInputStream("Banner.jpg");
		PDXObjectImage banner = new PDJpeg(document, bannerIn);
		
		bannerHeight = ((float)banner.getHeight() / (float)banner.getWidth()) * bannerWidth;
		bannerY = pageHeight - (bannerHeight + margin);
		
		PDFont font = PDFHelper.loadFont(document, "Arial.ttf");
		//PDFont fontBd = PDFHelper.loadFont(document, "Arialbd.ttf");
		
		if (font == null)
		{
			font = PDType1Font.COURIER;
			System.out.println("Font not found! Using courier instead!");
		}
		
		dateWidth = font.getStringWidth(date) / 1000.0f;
		
		PDPageContentStream contentStream = new PDPageContentStream(document, page);

		// Define a text content stream using the selected font, moving the cursor and drawing the text "Hello World"
		//Draw Banner image
		contentStream.drawXObject(banner, margin, bannerY, bannerWidth, bannerHeight);
		
		//Draw bar below banner
		contentStream.setNonStrokingColor(barColor);
		contentStream.fillRect(margin, bannerY - barHeight, bannerWidth, barHeight);
		
		//Draw footer bar
		contentStream.fillRect(margin, footerBarY, bannerWidth, footerBarHeight);
		
		offset = 12.0f * scale;
		offset2 = 60.0f * scale;
		
		//Draw lines around client information
		offsetY = barHeight * 3;
		typeStringLength = titleFontSize * font.getStringWidth(type) / 1000.0f;
		numberLength = fontSize * font.getStringWidth(number) / 1000.0f;
		
		line1y = bannerY - offsetY;
		line2y = line1y - (325.0f * scale);
		
		tableBox.move(margin, line2y - (tableBox.getHeight() + (40.0f * scale)));
		
		totalBox = new PDRectangle(tableBox.getWidth(), (tableBox.getLowerLeftY() - (footerBarY + footerBarHeight)) / 2.0f);
		totalBox.move(margin, footerBarY + footerBarHeight);
		
		taxesBox = new PDRectangle(tableBox.getWidth(), totalBox.getHeight());
		taxesBox.move(margin, totalBox.getUpperRightY());
		
		labelBox = new PDRectangle(600.0f * scale, totalBox.getHeight());
		labelBox.move(pageWidth - (margin + labelBox.getWidth()), taxesBox.getLowerLeftY());
		
		addressBoxHeight = line1y - line2y;
		addressY = (5.0f * scale) + line1y - (fontSize + ((addressBoxHeight - addressHeight) / 2.0f));
		
		contentStream.drawLine(margin, line1y, pageWidth - (margin + (typeStringLength * 2) + offset2), line1y);
		contentStream.drawLine(pageWidth + offset2 - (margin + typeStringLength), line1y, pageWidth - margin, line1y);
		contentStream.drawLine(margin, line2y, pageWidth - margin, line2y);
		
		contentStream.beginText();
		//contentStream.setFont(font, fontSize);
		//contentStream.setNonStrokingColor(0, 0, 0);
		//contentStream.drawString("Start.");
		//contentStream.drawString("End.");
		
		//Draw Invoice/Quote
		oldX = pageWidth - (margin + (typeStringLength * 2));
		oldY = line1y - (30.0f * scale);
				
		contentStream.setFont(font, titleFontSize);
		contentStream.setNonStrokingColor(0, 0, 0);
		contentStream.moveTextPositionByAmount(oldX, oldY);
		contentStream.drawString(type);
		contentStream.moveTextPositionByAmount(0, 0);
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

		//Draw client info
		offsetX = pageWidth - (numberLength + (2 * margin));
		
		contentStream.setNonStrokingColor(0, 0, 0);
		contentStream.moveTextPositionByAmount(margin, addressY);
		contentStream.drawString("To:");
		contentStream.moveTextPositionByAmount(offsetX, 0);
		contentStream.drawString(number);
		contentStream.moveTextPositionByAmount(-offsetX, 0);
		
		offsetX = 200.0f * scale;
		contentStream.moveTextPositionByAmount(offsetX, 0);
		
		for (String str : customerLines)
		{
			contentStream.drawString(str);
			contentStream.moveTextPositionByAmount(0, -(addressLineHeight + addressLineOffset));
		}

		contentStream.moveTextPositionByAmount(-(margin + offsetX), ((addressLineHeight + addressLineOffset) * customerLines.size()) - addressY);
		
		for (String str : footerLines)
		{
			PDFHelper.drawString(contentStream, footerLine, PDFHelper.HorizontalAlignment.CENTER, PDFHelper.VerticalAlignment.BOTTOM, str, font, fontSize);
			footerLine.move(0, footerLineHeight);
		}
		
		PDFHelper.drawString(contentStream, labelBox, PDFHelper.HorizontalAlignment.LEFT, PDFHelper.VerticalAlignment.CENTER, "  TAXES", font, fontSize);
		labelBox.move(0, -taxesBox.getHeight());
		PDFHelper.drawString(contentStream, labelBox, PDFHelper.HorizontalAlignment.LEFT, PDFHelper.VerticalAlignment.CENTER, "  TOTAL", font, fontSize);
		
		PDFHelper.drawString(contentStream, taxesBox, PDFHelper.HorizontalAlignment.RIGHT, PDFHelper.VerticalAlignment.CENTER, String.format("$%s", Utility.formatMoneyExport(bill.getTaxes())), font, fontSize);
		PDFHelper.drawString(contentStream, totalBox, PDFHelper.HorizontalAlignment.RIGHT, PDFHelper.VerticalAlignment.CENTER, String.format("$%s", Utility.formatMoneyExport(bill.total())), font, fontSize);
		
		//contentStream.drawString("End.");
		
		contentStream.endText();

		PDFHelper.drawBill(contentStream, tableBox, font, fontSize, scale, bill);

		// Make sure that the content stream is closed:
		contentStream.close();
		
		document.save(fileName);
		document.close();
		
		bannerIn.close();
	}
}
