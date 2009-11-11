package testlink.eclipse.plugin.views;


import org.eclipse.jface.dialogs.IInputValidator;

import testlink.api.java.client.tc.autoexec.TestCase;
import testlink.api.java.client.tc.autoexec.TestCaseExecutor;


public class ManualResultsInput
{

	/**
	 * The application entry point
	 * 
	 * @param args the command line arguments
	 */
	public static void setManualTestResults(
		TestCase tc,
		String htmlDescription, 
		TestCaseExecutor te)
	{
		// Open the dialog and get the information
		ManualResultsDialog dlg = new ManualResultsDialog(tc.getTestCaseName(),
			htmlDescription, new LengthValidator());
		dlg.open();
		
		// Assign the text
		String note = dlg.getValue();
		note = htmlFormat(note);
		te.setExecutionNotes(note);
		
		// Assign the status results
		int ret = dlg.getReturnCode();
		if ( ret == ManualResultsDialog.PASSED ) {
			te.setExecutionResult(TestCaseExecutor.RESULT_PASSED);
		} else if ( ret == ManualResultsDialog.BLOCKED ) {
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
	
	private static String htmlFormat(
		String note)
	{
		char cret = new String("\r").charAt(0);
		char nl = new String("\n").charAt(0);
		StringBuffer buffer = new StringBuffer("");
		for ( int i = 0; i < note.length(); i++ ) {
			char c = note.charAt(i);
			if ( c == cret ) {
				buffer.append("</pre></p>");
			} else if ( c == nl ) {
				buffer.append("<p><pre>");
			} else {
				buffer.append(c);
			}
		}
		note = buffer.toString();
		return "<p><pre>" + note + "</pre></p>";
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

