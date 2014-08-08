package photobooks.business;

import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;

public class PDFHelper {

	public final static String[] fontPaths = new String[] { "/Library/Fonts/", "/System/Library/Fonts/", "$HOME/Library/Fonts/", "C:/Windows/Fonts/" };
	
	public static final float aspectRatio = 11.0f / 8.5f;
	public static final float layoutDpi = 250;
	
	public static float dpiScale(float dpi) { return dpi / layoutDpi; }
	
	public static float pageWidth(float dpi) { return 8.5f * dpi; }
	public static float pageHeight(float dpi) { return pageWidth(dpi) * aspectRatio; }
	
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
