package org.dbfacade.testlink.eclipse.plugin.views;


import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class ManualResultsDialog extends Dialog
{

	public static final int PASSED = 0;
	public static final int FAILED = 1;
	public static final int BLOCKED = 2;
	
	/**
	 * The title of the dialog.
	 */
	private String title;
	 
	/**
	 * The message to display, or <code>null</code> if none.
	 */
	private String message;
 
	/**
	 * The input value; the empty string by default.
	 */
	private String value = ""; // $NON-NLS-1$
 
	/**
	 * The input validator, or <code>null</code> if none.
	 */
	private IInputValidator validator;
 
	/**
	 * Ok button widget.
	 */
	private Button okButton;
 
	/**
	 * Input text widget.
	 */
	private Text text;
 
	/**
	 * Error message label widget.
	 */
	private Text errorMessageText;
	
	/**
	 * The test case descriptions from test link are in html
	 */
	private String html;
	
	/**
	 * Creates an input dialog with OK and Cancel buttons. Note that the dialog
	 * will have no visual representation (no widgets) until it is told to open.
	 * <p>
	 * Note that the <code>open</code> method blocks for input dialogs.
	 * </p>
	 * 
	 * @param parentShell
	 *            the parent shell, or <code>null</code> to create a top-level
	 *            shell
	 * @param dialogTitle
	 *            the dialog title, or <code>null</code> if none
	 * @param dialogMessage
	 *            the dialog message, or <code>null</code> if none
	 * @param initialValue
	 *            the initial input value, or <code>null</code> if none
	 *            (equivalent to the empty string)
	 * @param validator
	 *            an input validator, or <code>null</code> if none
	 */
	public ManualResultsDialog(
		Shell parentShell,
		String dialogTitle,
		String dialogMessage,
		String initialValue,
		String htmlDescription,
		IInputValidator validator)
	{
		super(parentShell);
		this.title = dialogTitle;
		message = dialogMessage;
		if ( initialValue == null ) {
			value = "";
		} // $NON-NLS-1$
		else {
			value = initialValue;
		}
		this.validator = validator;
		this.html = htmlDescription;
	}

	/**
	 * Implemented with InputDialog which is extremely limiting and 
	 * will need to be extended to handle a proper dialog.
	 * 
	 * @param tc
	 * @param validator
	 */
	// TODO: The standard InputDialog is just not good enough 
	public ManualResultsDialog(
		String testCaseName,
		String html,
		IInputValidator validator)
	{
		this(TestLinkView.viewer.getControl().getShell(), testCaseName,
			"Enter test case results note:", "", html, validator);
		this.create();
		this.setBlockOnOpen(true);
		Button ok = this.getButton(IDialogConstants.OK_ID);
		Button cancel = this.getButton(IDialogConstants.CANCEL_ID);
		ok.setText("Passed");
		cancel.setText("Failed");
	}
	
	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	protected void buttonPressed(
		int buttonId)
	{
		value = text.getText();
		if ( value != null && value.length() > 10 ) {
			int ret = FAILED;
			if ( buttonId == IDialogConstants.OK_ID ) {
				ret = PASSED;
			} else if ( buttonId == IDialogConstants.CLOSE_ID ) {
				ret = BLOCKED;
			}
			setReturnCode(ret);
			close();
		}
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
	protected void createButtonsForButtonBar(
		Composite parent)
	{
		// create OK and Cancel buttons by default
		okButton = createButton(parent, IDialogConstants.OK_ID, "Passed", false);
		createButton(parent, IDialogConstants.CANCEL_ID, "Failed", true);
		createButton(parent, IDialogConstants.CLOSE_ID, "Blocked", false);
		// do this here 
		text.setFocus();
		if ( value != null ) {
			text.setText(value);
			text.selectAll();
		}
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
		browser.setText(html);
		browser.setVisible(true);
		
		
		// create entry request message
		if ( message != null ) {
			Label label = new Label(composite, SWT.WRAP);
			label.setText(message);
			GridData data = new GridData(
				GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL
				| GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
			data.widthHint = convertHorizontalDLUsToPixels(
				IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
			label.setLayoutData(data);
			label.setFont(parent.getFont());
		}
		

		// Data entry text area
		text = new Text(composite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		grid = new GridData(SWT.FILL, SWT.FILL, true, true, 80, 8);
		text.setLayoutData(grid);
		text.addModifyListener(new ModifyListener()
		{
			public void modifyText(
				ModifyEvent e)
			{
				validateInput();
			}
		});
		
		
		// Error message text area
		errorMessageText = new Text(composite, SWT.READ_ONLY);
		errorMessageText.setLayoutData(
			new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		errorMessageText.setBackground(errorMessageText.getDisplay()
			.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
 
		applyDialogFont(composite);
		return composite;
	}
 
	/**
	 * Returns the ok button.
	 * 
	 * @return the ok button
	 */
	protected Button getOkButton()
	{
		return okButton;
	}
 
	/**
	 * Returns the text area.
	 * 
	 * @return the text area
	 */
	protected Text getText()
	{
		return text;
	}
 
	/**
	 * Returns the validator.
	 * 
	 * @return the validator
	 */
	protected IInputValidator getValidator()
	{
		return validator;
	}
 
	/**
	 * Returns the string typed into this input dialog.
	 * 
	 * @return the input string
	 */
	public String getValue()
	{
		return value;
	}
 
	/**
	 * Validates the input.
	 * <p>
	 * The default implementation of this framework method delegates the request
	 * to the supplied input validator object; if it finds the input invalid,
	 * the error message is displayed in the dialog's message line. This hook
	 * method is called whenever the text changes in the input field.
	 * </p>
	 */
	protected void validateInput()
	{
		String errorMessage = null;
		if ( validator != null ) {
			errorMessage = validator.isValid(text.getText());
		}
		// Bug 16256: important not to treat "" (blank error) the same as null
		// (no error)
		setErrorMessage(errorMessage);
	}
 
	/**
	 * Sets or clears the error message.
	 * If not <code>null</code>, the OK button is disabled.
	 * 
	 * @param errorMessage
	 *            the error message, or <code>null</code> to clear
	 * @since 3.0
	 */
	public void setErrorMessage(
		String errorMessage)
	{
		errorMessageText.setText(errorMessage == null ? "" : errorMessage); // $NON-NLS-1$
		okButton.setEnabled(errorMessage == null);
		errorMessageText.getParent().update();
	}
} 
