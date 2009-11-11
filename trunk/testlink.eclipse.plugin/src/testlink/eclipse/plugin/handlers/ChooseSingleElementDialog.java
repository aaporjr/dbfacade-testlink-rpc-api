package testlink.eclipse.plugin.handlers;


import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


public class ChooseSingleElementDialog extends Dialog
{

	/**
	 * The title of the dialog.
	 */
	private String title;
	private String message;
	private String[] choices;
	private Combo combo;
	private String value=null;

	 
	public ChooseSingleElementDialog(
		Shell shell,
		String title,
		String message,
		String[] choices)
	{
		super(shell);
		this.title = title;
		this.message = message;
		this.choices = choices;
	}
	

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	protected void buttonPressed(
		int buttonId)
	{
		value = combo.getText();
		super.buttonPressed(buttonId);
		close();
	}
	
	public String getChoice() {
		return value;
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
	 * (non-Javadoc) Method declared on Dialog.
	 */
	protected Control createDialogArea(
		Composite parent)
	{
		// create composite
		Composite composite = (Composite) super.createDialogArea(parent);
		
		// create label
		Label categoryLabel = new Label(composite, SWT.WRAP);
		categoryLabel.setText(message);
		GridData cData = new GridData(
			GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL
			| GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
		cData.widthHint = convertHorizontalDLUsToPixels(
			IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
		categoryLabel.setLayoutData(cData);
		categoryLabel.setFont(parent.getFont());
		
		// create combo box
		combo = new Combo(parent,  SWT.SINGLE | SWT.READ_ONLY);
		combo.setLayoutData(cData);
		combo.setItems(choices);
	    

		applyDialogFont(composite);
		return composite;
	}
} 
