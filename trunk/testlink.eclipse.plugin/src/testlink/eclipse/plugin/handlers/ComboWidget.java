package testlink.eclipse.plugin.handlers;


import java.util.Arrays;

import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


public class ComboWidget
{
	Combo wCombo;
	
	public ComboWidget(
		Composite comp,
		String labelText,
		String[] comboSelections,
		final ILaunchConfigurationDialog launchDialog)
	{
		if ( comboSelections == null || comboSelections.length == 0 ) {
			return;
		}
		Label titleLabel = new Label(comp, SWT.NONE);
		titleLabel.setText(labelText);
		GridData gd = new GridData();
		gd.horizontalIndent = 25;
		titleLabel.setLayoutData(gd);
		        
		wCombo = new Combo(comp, SWT.NULL);
			    
		for ( int i = 0; i < comboSelections.length; i++ ) {
			wCombo.add(comboSelections[i]);
		}
		wCombo.select(0);
			    
		wCombo.addSelectionListener(
			new SelectionListener()
		{
			public void widgetSelected(
				SelectionEvent e)
			{
			}

			public void widgetDefaultSelected(
				SelectionEvent e)
			{
				String text = wCombo.getText();
				if ( wCombo.indexOf(text) < 0 ) { 
					wCombo.add(text);
					// Re-sort
					String[] items = wCombo.getItems();
					Arrays.sort(items);
					wCombo.setItems(items);
				}
			}
		});
		
		wCombo.addModifyListener(new ModifyListener()
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
		     
		Label label = new Label(comp, SWT.NONE);
		gd = new GridData();
		gd.horizontalSpan = 3;
		label.setLayoutData(gd);
	}
	
	public Combo getComboWidget() {
		return wCombo;
	}
	
	public String getText() {
		return wCombo.getText();
	}

}
