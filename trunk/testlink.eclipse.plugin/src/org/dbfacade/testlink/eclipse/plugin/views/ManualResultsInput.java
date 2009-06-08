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
	public static void setManualTestResults(
		TestCase tc,
		TestCaseExecutor te)
	{
		// Open the dialog and get the information
		ManualResultsDialog dlg = new ManualResultsDialog(tc, new LengthValidator());
		dlg.open();
		
		// Assign the text
		String note = dlg.getValue();
		te.setExecutionNotes(note);
		
		// Assign the status results
		int ret = dlg.getReturnCode();
		if ( ret == ManualResultsDialog.PASSED) {
			te.setExecutionResult(TestCaseExecutor.RESULT_PASSED);
		} else if ( ret == ManualResultsDialog.BLOCKED) {
			te.setExecutionResult(TestCaseExecutor.RESULT_BLOCKED);
		} else {
			te.setExecutionResult(TestCaseExecutor.RESULT_FAILED);
		}
		
		if ( te.getExecutionState() != TestCaseExecutor.STATE_BOMBED ) {
			te.setExecutionState(TestCaseExecutor.STATE_COMPLETED);
		} else {
			te.setExecutionResult(TestCaseExecutor.RESULT_FAILED);
		}
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

		// Determine if input is too short 
		if ( len < 11 ) {
			return "Too short";
		}


		// Input must be OK
		return null;
	}
}

