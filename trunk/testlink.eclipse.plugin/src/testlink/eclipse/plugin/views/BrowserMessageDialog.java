package testlink.eclipse.plugin.views;


import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


public class BrowserMessageDialog extends Dialog
{

	/**
	 * The title of the dialog.
	 */
	private String title;
	private String html;
	private String url;
	private boolean useUrl = false;
	 
	public BrowserMessageDialog(
		Shell shell,
		String title)
	{
		super(shell);
		this.title = title;
	}
	
	public void setHtml(
		String html)
	{
		this.html = html;
		this.url = null;
		useUrl = false;
	}

	public void setUrl(
		String url)
	{
		this.html = null;
		this.url = url;
		useUrl = true;
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	protected void buttonPressed(
		int buttonId)
	{
		
		close();
	}
 
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(
		Shell shell)
	{
		super.configureShell(shell);
		if ( title != null ) {
			shell.setText(title);
		}
	}
 
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	private Button okButton;
	protected void createButtonsForButtonBar(
		Composite parent)
	{
		// create OK and Cancel buttons by default
		okButton = createButton(parent, IDialogConstants.OK_ID, "OK", false);
		
		// Set focus here
		okButton.setFocus();
	}
 
	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	protected Control createDialogArea(
		Composite parent)
	{
		// create composite
		Composite composite = (Composite) super.createDialogArea(parent);
		
		// create label
		Label categoryLabel = new Label(composite, SWT.WRAP);
		categoryLabel.setText("");
		GridData cData = new GridData(
			GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL
			| GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
		cData.widthHint = convertHorizontalDLUsToPixels(
			IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
		categoryLabel.setLayoutData(cData);
		categoryLabel.setFont(parent.getFont());
		
		
		// create browser
		Browser browser = new Browser(composite, SWT.NONE);
		GridData grid = new GridData(SWT.FILL, SWT.FILL, true, true, 300, 40);
		browser.setLayoutData(grid);
		if ( useUrl ) {
			browser.setUrl(url);
		} else {
			browser.setText(html);
		}
		browser.setVisible(true);
		applyDialogFont(composite);
		return composite;
	}
} 
