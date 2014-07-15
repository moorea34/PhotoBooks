package photobooks.business;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.Calendar;

import photobooks.objects.Address;
import photobooks.objects.Bill;
import photobooks.objects.BillPackage;
import photobooks.objects.BillProduct;
import photobooks.objects.Client;
import photobooks.objects.Payment;
import photobooks.objects.PhoneNumber;

public class HtmlGenerator
{
	private static DecimalFormat format = new DecimalFormat("0.00");
	private static String _billTemplate = null;
	
	private static void loadTemplate()
	{
		_billTemplate = "";
		
		try
		{
			FileReader fileReader = new FileReader("templates/billtemplate.html");
			BufferedReader reader = new BufferedReader(fileReader);
			
			String line = reader.readLine();
			
			while (line != null)
			{
				_billTemplate += line + "\r\n";
				line = reader.readLine();
			}
			
			reader.close();
			fileReader.close();
		}
		catch (Exception e)
		{
			System.out.println("Error loading bill template: " + e.toString());
			_billTemplate = null;
		}
	}
	
	private static String formatDate(Calendar calendar)
	{
		return String.format("%d/%d/%d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
	}
	
	private static String formatMoney(double price)
	{
		if (Math.abs(price) < 0.01)
			return "0.00";
		else
			return format.format(price);
	}
	
	private static String formatPhoneNumber(String number)
	{
		String result = "";
		char c;
		int numberCount = 0;
		
		if (number != null)
		{
			for (int i = number.length() - 1; i >= 0; --i)
			{
				c = number.charAt(i);
				
				if (c >= '0' && c <= '9')
				{
					if (numberCount == 4 || numberCount == 7 || numberCount == 10)
						result = "." + result;
					
					result = c + result;
					numberCount++;
				}
			}
		}
		
		return result;
	}
	
	private static String clientData(String data)
	{
		return "<p class=\"headerbottomright\">" + data + "</p>";
	}
	
	private static String createClient(Client _client)
	{
		String clientInfo = "";
		
		if (_client != null)
		{
			clientInfo = clientData(_client.getFullName());
			String temp;
			boolean first;
		
			temp = "";
			first = true;
	
			for (Address addr : _client.getAddresses())
			{
				if (!first)
					temp += ", ";
				else
					first = false;
		
				temp += addr.getAddress();
			}
	
			clientInfo += clientData(temp);
		
			temp = "";
			first = true;
		
			for (PhoneNumber phone : _client.getNumbers())
			{
				if (!first)
					temp += ", ";
				else
					first = false;
		
				temp += formatPhoneNumber(phone.getNumber());
			}
	
			clientInfo += clientData(temp);
		}
		
		return clientInfo;
	}
	
	private static String createBillContent(Bill bill)
	{
		String billInfo = "";
		
		if (bill != null)
		{
			billInfo = "<p>" + bill.getType().toString() + " # " + bill.getID() + "</p>";
			
			billInfo += "<table> <tr> <th>Item</th> <th>Amount</th> <th>Price</th> <th>Total</th> </tr>";
			
			for (BillPackage billPackage : bill.getPackages())
			{
				billInfo += String.format("<tr> <td class=\"tdleft\">%s</td> <td class=\"tdleft\">%d</td> <td>$%s</td> <td>$%s</td> </tr>", billPackage.getPackage().getName(), billPackage.getAmount(), formatMoney(billPackage.getPrice()), formatMoney(billPackage.getPrice() * billPackage.getAmount()));
			}
			
			for (BillProduct billProduct : bill.getProducts())
			{
				billInfo += String.format("<tr> <td class=\"tdleft\">%s</td> <td class=\"tdleft\">%d</td> <td>$%s</td> <td>$%s</td> </tr>", billProduct.getProduct().getName(), billProduct.getAmount(), formatMoney(billProduct.getPrice()), formatMoney(billProduct.getPrice() * billProduct.getAmount()));
			}
			
			billInfo += "</table>";
			
			billInfo += "<div class=\"totals\">";
			
			//Write out values for totals
			billInfo += "<div class=\"totalsvalues\">";
			billInfo += "<p>$" + formatMoney(bill.total()) + "</p>";
			billInfo += "<p>$" + formatMoney(bill.getGst() * bill.total()) + "</p>";
			billInfo += "<p>$" + formatMoney(bill.getPst() * bill.total()) + "</p>";
			billInfo += "<p>$" + formatMoney(bill.total() + bill.getTaxes()) + "</p>";
			billInfo += "</div>";
			
			//Write out labels for totals
			billInfo += "<div class=\"totalslabels\">";
			billInfo += "<p>Sub Total: </p>";
			billInfo += "<p>Gst: </p>";
			billInfo += "<p>Pst: </p>";
			billInfo += "<p>Total: </p>";
			billInfo += "</div>";
			
			billInfo += "</div>";
			
			if (bill.getPayments().size() > 0)
			{
				billInfo += "<p>Payments:</p>";
				
				billInfo += "<table> <tr> <th>Date</th> <th>Type</th> <th>Amount</th> </tr>";
				
				for (Payment payment : bill.getPayments())
				{
					billInfo += String.format("<tr> <td class=\"tdleft\">%s</td> <td class=\"tdleft\">%s</td> <td>$%s</td> </tr>", formatDate(payment.getDate()), payment.getTenderType().toString(), formatMoney(payment.getAmount()));
				}
				
				billInfo += "</table>";
				
				billInfo += "<div class=\"paymenttotals\">";
				
				//Write out values for totals
				billInfo += "<div class=\"totalsvalues\">";
				billInfo += "<p>$" + formatMoney(bill.totalPayments()) + "</p>";
				billInfo += "<p>$" + formatMoney((bill.total() + bill.getTaxes()) - bill.totalPayments()) + "</p>";
				billInfo += "</div>";
				
				//Write out labels for totals
				billInfo += "<div class=\"totalslabels\">";
				billInfo += "<p>Total: </p>";
				billInfo += "<p>Amount Due: </p>";
				billInfo += "</div>";
				
				billInfo += "</div>";
			}
		}
		
		return billInfo;
	}
	
	public static String createBill(Bill _bill)
	{
		String html = "";
		
		if (_billTemplate == null)
			loadTemplate();
		
		if (_bill != null && _billTemplate != null)
		{
			String date = formatDate(_bill.getDate());
			String clientInfo = createClient(_bill.getClient());
			String content = createBillContent(_bill);
		
			html = String.format(_billTemplate, date, clientInfo, content);
		}
		
		return html;
	}
}
