package org.dbfacade.testlink.tc.autoexec;


import org.dbfacade.testlink.api.client.TestLinkAPIException;


public class EmptyExecutor implements TestCaseExecutor
{
	private short testResult = FAILED;
	/**
	 * Return FAILED result state of the test case execution.
	 * 
	 * @return The result of the test case. Implementers should set the initial status to UNKNOWN.
	 */
	public short getExecutionResult()
	{
		return testResult;
	}
	/**
	 * Set the results of the test from an external source.
	 * 
	 * @param result
	 */
	public void setExecutionResult(short result) {
		if ( result == FAILED || result == BLOCKED ) {
			testResult = result;
		}
	}
	
	/**
	 * Information about the results of the execution.
	 * 
	 * @return Information about the results of the execution.
	 */
	public String getExecutionNotes()
	{
		return "Empty executor generated to report executor missing from test case.";
	}
	
	/**
	 * Execute the test case that has been passed into the execute method.
	 * 
	 * @param testCase
	 * @throws TestLinkAPIException
	 */
	public void execute(
		TestCase testCase) throws TestLinkAPIException
	{
		throw new TestLinkAPIException(
			"This is an empty executor to hold failed test result.");
	}

}
