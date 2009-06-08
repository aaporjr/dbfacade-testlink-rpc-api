package org.dbfacade.testlink.eclipse.plugin.views;


import org.dbfacade.testlink.tc.autoexec.TestCase;
import org.dbfacade.testlink.tc.autoexec.TestCaseExecutor;
import org.eclipse.jface.dialogs.IInputValidator;


public class ManualResultsInput
{

	/**
	 * The application entry point
	 * 
	 * @param args the command line arguments
	 */
	public static void getManualTestResults(
		TestCase tc,
		TestCaseExecutor te)
	{
		ManualResultsDialog dlg = new ManualResultsDialog(tc, new LengthValidator());
		dlg.open();
		dlg.getValue();
		dlg.getReturnCode();
	}
}


/**
 * This class validates a String. It makes sure that the String is between 5 and 8
 * characters
 */
class LengthValidator implements IInputValidator
{

	/**
	 * Validates the String. Returns null for no error, or an error message
	 * 
	 * @param newText the String to validate
	 * @return String
	 */
	public String isValid(
		String newText)
	{
		int len = newText.length();

		// Determine if input is too short or too long
		if ( len < 10 ) {
			return "Too short";
		}
		if ( len > 15000 ) {
			return "Too long";
		}

		// Input must be OK
		return null;
	}
}

