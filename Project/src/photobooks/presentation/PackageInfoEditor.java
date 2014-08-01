package photobooks.presentation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class PackageInfoEditor extends Composite {

	public Text nameBox;
	public Text descripBox;
	public Text priceBox;
	public Label descripLbl, nameLbl, priceLbl;
	
	public Group infoBox;

	public PackageInfoEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		
		int labelDistance = 10;
		
		int labelWidth = 80;
		int labelHeight = 20;
		
		int boxOffsetY = -2;
		int boxHeight = 22;

		infoBox = new Group(this, SWT.NONE);
		infoBox.setLayout(new FormLayout());
		FormData fd_infoBox = new FormData();
		fd_infoBox.left = new FormAttachment(0, 0);
		fd_infoBox.right = new FormAttachment(100, 0);
		fd_infoBox.top = new FormAttachment(0, 0);
		fd_infoBox.bottom = new FormAttachment(100, 0);
		infoBox.setLayoutData(fd_infoBox);
		infoBox.setText("Package Information");
		
		nameLbl = new Label(infoBox, SWT.NONE);
		FormData fd_nameLbl = new FormData();
		fd_nameLbl.top = new FormAttachment(0, 6);
		fd_nameLbl.left = new FormAttachment(0, 6);
		fd_nameLbl.width = labelWidth;
		fd_nameLbl.height = labelHeight;
		nameLbl.setLayoutData(fd_nameLbl);
		nameLbl.setText("Name");
		
		priceLbl = new Label(infoBox, SWT.NONE);
		FormData fd_priceLbl = new FormData();
		fd_priceLbl.top = new FormAttachment(nameLbl, labelDistance);
		fd_priceLbl.left = new FormAttachment(nameLbl, 0, SWT.LEFT);
		fd_priceLbl.width = labelWidth;
		fd_priceLbl.height = labelHeight;
		priceLbl.setLayoutData(fd_priceLbl);
		priceLbl.setText("Price");
		
		descripLbl = new Label(infoBox, SWT.NONE);
		FormData fd_descripLbl = new FormData();
		fd_descripLbl.top = new FormAttachment(priceLbl, labelDistance);
		fd_descripLbl.left = new FormAttachment(nameLbl, 0, SWT.LEFT);
		fd_descripLbl.width = labelWidth;
		fd_descripLbl.height = labelHeight;
		descripLbl.setLayoutData(fd_descripLbl);
		descripLbl.setText("Description");
		
		nameBox = new Text(infoBox, SWT.BORDER);
		FormData fd_nameBox = new FormData();
		fd_nameBox.right = new FormAttachment(100, -6);
		fd_nameBox.top = new FormAttachment(nameLbl, boxOffsetY, SWT.TOP);
		fd_nameBox.left = new FormAttachment(nameLbl, 6);
		fd_nameBox.height = boxHeight;
		nameBox.setLayoutData(fd_nameBox);
		nameBox.setEditable(false);
		
		priceBox = new Text(infoBox, SWT.BORDER);
		FormData fd_priceBox = new FormData();
		fd_priceBox.left = new FormAttachment(nameBox, 0, SWT.LEFT);
		fd_priceBox.right = new FormAttachment(nameBox, 0, SWT.RIGHT);
		fd_priceBox.top = new FormAttachment(priceLbl, boxOffsetY, SWT.TOP);
		fd_priceBox.height = boxHeight;
		priceBox.setLayoutData(fd_priceBox);
		priceBox.setEditable(false);
		
		descripBox = new Text(infoBox, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		FormData fd_descripBox = new FormData();
		fd_descripBox.top = new FormAttachment(descripLbl, 6);
		fd_descripBox.right = new FormAttachment(nameBox, 0, SWT.RIGHT);
		fd_descripBox.left = new FormAttachment(nameLbl, 0, SWT.LEFT);
		fd_descripBox.bottom = new FormAttachment(100, -6);
		descripBox.setLayoutData(fd_descripBox);
		descripBox.setEditable(false);
	}

	/*@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}*/
}
