package org.dbfacade.testlink.eclipse.plugin;


import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;


public class UserMsg
{
	public static final String title = "TestLink View";
	public static Shell shell; 
	public static String packageName = "org.dbfacade.testlink.eclipse.plugin";
	
	public static void error(
		String message)
	{
		error(null, message);
	}

	public static void error(
		Exception e,
		String message)
	{		
		// Show error
		try {
			Exception show = new Exception("No details available.");
			String details = getFirstPluginStackTraceElement(e);
			if ( details != null ) {
				show = new Exception(details);
			} 
			Status status = new Status(IStatus.ERROR, title, 0, e.toString(), show);
			ErrorDialog.openError(shell, title, message, status);
		} catch ( Exception de ) {
			// Build exception message portion
			String emsg = buildExceptionMessage(e);
			
			// Build the final message
			if ( emsg != null ) {
				message = message + "\n\nDetails:\n\n" + emsg;
			}
			MessageDialog.openError(shell, title, message);
		}
	}
	
	public static void message(
		String message)
	{
		MessageDialog.openInformation(shell, title, message);
	}
	
	/*
	 * Private methods
	 */
	private static String buildExceptionMessage(
		Exception e)
	{
		String emsg = null;
		if ( e != null ) {
			
			// Setup Exception message
			emsg = e.toString();
			if ( e.getMessage() != null ) {
				emsg += "\n" + e.getMessage();
			} 
			
			// Add some trace details
			StackTraceElement[] elements = e.getStackTrace();
			if ( elements.length > 0 ) {
				emsg += "\n\nTrace\n";
			}
			int lines = 0;
			for ( int i = 0; i < elements.length && lines < 5; i++ ) {
				StackTraceElement element = elements[i];
				// Print final line and those that are of importance to the package
				if ( element.toString().contains(packageName) || lines < 2 ) {
					lines++;
					emsg += element.toString() + "\n";
				}
			}
		}
		return emsg;
	}
	
	private static String getFirstPluginStackTraceElement(
		Exception e)
	{
		String first = null;
		StackTraceElement[] elements = e.getStackTrace();
		if ( elements.length > 0 ) {
			first = elements[0].toString();
		}
		for ( int i = 0; i < elements.length; i++ ) {
			StackTraceElement element = elements[i];
			if ( element.toString().contains(packageName) ) {
				return element.toString();
			}
		}
		return first;
	}
}
