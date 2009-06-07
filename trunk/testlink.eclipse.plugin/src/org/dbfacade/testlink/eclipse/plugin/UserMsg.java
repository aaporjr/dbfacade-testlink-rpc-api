package org.dbfacade.testlink.eclipse.plugin;


import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;


public class UserMsg
{
	public static final String title = "TestLink View";
	public static Shell shell=PlatformUI.getWorkbench().getDisplay().getActiveShell();
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
		// Build exception message portion
		String emsg = buildExceptionMessage(e);
		
		// Build the final message
		if ( emsg != null ) {
			message = message + "\n\nDetails:\n\n" + emsg;
		}
		
		// Show error
		MessageDialog.openError(shell, title, message);
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
			emsg = e.getMessage();
			if ( emsg == null ) {
				emsg = "Possible null pointer exception";	
			} 
			
			if ( e.getLocalizedMessage() != null ) {
				if ( !emsg.equals(e.getLocalizedMessage()) ) {
					emsg = "\n" + e.getLocalizedMessage();
				}
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
}
