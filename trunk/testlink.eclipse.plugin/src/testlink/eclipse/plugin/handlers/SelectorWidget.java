/*
 * Daniel R Padilla
 *
 * Copyright (c) 2009, Daniel R Padilla
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package testlink.eclipse.plugin.handlers;


import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.jdt.internal.ui.util.PixelConverter;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


public class SelectorWidget
{
	Label wLabel;
	Text text;
	Button button;
		
	public SelectorWidget(
		Composite comp,
		String labelText,
		final SelectorHandler handler)
	{
		this(comp, labelText, null, handler, null);
	}

	public SelectorWidget(
		Composite comp,
		String labelText,
		String buttonText,
		final SelectorHandler handler,
		final ILaunchConfigurationDialog launchDialog)
	{
		wLabel = new Label(comp, SWT.NONE);
		wLabel.setText(labelText);
		GridData gd = new GridData();
		gd.horizontalIndent = 25;
		wLabel.setLayoutData(gd);
	        
		text = new Text(comp, SWT.SINGLE | SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addModifyListener(new ModifyListener()
		{
			public void modifyText(
				ModifyEvent evt)
			{
				if ( launchDialog != null ) {
					launchDialog.updateMessage();
					launchDialog.updateButtons();
				}
			}
		});
	     
		if ( buttonText != null && handler != null ) {
			button = new Button(comp, SWT.PUSH);
			button.setText(buttonText);
			button.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(
					SelectionEvent evt)
				{
					handler.handle(wLabel, text, button);
				}
			});
			setButtonGridData(button);
		}
	        
        Label label= new Label(comp, SWT.NONE);
        gd= new GridData();
        gd.horizontalSpan= 3;
        label.setLayoutData(gd);
	}
	
	public Label getWidgetLabel()
	{
		return wLabel;
	}
	
	public Text getWidgetText()
	{
		return text;
	}
	
	public Button getWidgetButton()
	{
		return button;
	}
		
	/*
	 * Layout util
	 */
	public static void setButtonGridData(
		Button button)
	{
		GridData gridData = new GridData();
		button.setLayoutData(gridData);
		Object gd = button.getLayoutData();
		if ( gd instanceof GridData ) {
			((GridData) gd).widthHint = getButtonWidthHint(button);
			((GridData) gd).horizontalAlignment = GridData.FILL;
		}
	}
	    
	/*
	 * Returns a width hint for a button control.
	 * @param button the button for which to set the dimension hint
	 * @return the width hint
	 */
	public static int getButtonWidthHint(
		Button button)
	{
		button.setFont(JFaceResources.getDialogFont());
		PixelConverter converter = new PixelConverter(button);
		int widthHint = converter.convertHorizontalDLUsToPixels(
			IDialogConstants.BUTTON_WIDTH);
		return Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
	}
}
