package org.dbfacade.testlink.tc.autoexec.server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.dbfacade.testlink.tc.autoexec.EmptyExecutor;
import org.dbfacade.testlink.tc.autoexec.TestCase;
import org.dbfacade.testlink.tc.autoexec.TestCaseExecutor;
import org.dbfacade.testlink.tc.autoexec.TestPlan;


public class RemoteClientExecutor extends EmptyExecutor
{
	private ExecutionProtocol ep = new ExecutionProtocol();
	private BufferedReader stdIn;
	private String fromServer;
	private Socket fSocket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private TestPlan testPlan;

	/**
	 * Requires that the remote port be provided.
	 * 
	 * @param port
	 */
	public RemoteClientExecutor(
		int port,
		TestPlan testPlan
		)
	{	
		this.testPlan = testPlan;
		int retry = 0;
		while ( retry < 3 ) {
			try {
				retry++;
				Thread.sleep(500);
				openConnection(port);
				out.println(ExecutionProtocol.STR_PING);
				ExecutionProtocol.debug("Opened connection to localhost port: " + port);
				break;
			} catch ( Exception e ) {
				retry++;
				if ( retry >= 3 ) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(500);
				} catch ( Exception se ) {}
				continue;
			}
		}
	}
    
	/**
	 * Make a request for remote execution and then shut the test down.
	 */
	public void execute(
		TestCase tc)
	{
		try {
			
			// Check connection
			if ( !fSocket.isConnected() && fSocket.isClosed() ) {
				throw new Exception("The connection is no longer available.");
			}
			
			// Reset
			setExecutionResult(TestCaseExecutor.RESULT_UNKNOWN);
			setExecutionState(TestCaseExecutor.STATE_READY);
			setExecutionNotes("");
			
			// Build and send the request
			sendTestCaseRequest(tc);
			
			// Wait for the response
			stdIn = new BufferedReader(new InputStreamReader(System.in));
			while ( (fromServer = in.readLine()) != null ) {
				ep.processInput(fromServer);
				ExecutionProtocol.debug("Server: " + fromServer);
				if ( fromServer.startsWith(ExecutionProtocol.STR_RESULT) ) {
					ExecutionProtocol.debug("Result from server: " + fromServer);
					break;
				}
			}
			
			// Process Result
			processTestCaseResult(fromServer);
			
		} catch ( Exception e ) {
			setExecutionResult(TestCaseExecutor.RESULT_FAILED);
			setExecutionState(TestCaseExecutor.STATE_BOMBED);
			setExecutionNotes(
				"Unable to complete the remote test request. " + e.toString());
			ExecutionProtocol.debug(
				"Unable to complete the remote test request. " + e.toString());
		} 
	}
	
	/**
	 * Send the request for remote test case execution
	 * 
	 * @param tc
	 */
	public void sendTestCaseRequest(
		TestCase tc) throws Exception
	{
		String request = ExecutionProtocol.STR_REQUEST 
			+ ExecutionProtocol.STR_REQUEST_PROJECT_NAME + tc.getProjectName()
			+ ExecutionProtocol.STR_REQUEST_PLAN_NAME + testPlan.getTestPlanName()
			+ ExecutionProtocol.STR_REQUEST_TC_EXEC + tc.getTestCaseInternalID().toString();
		out.println(request);
	}
	
	/**
	 * Process the result back from the server
	 * 
	 * TODO: Validate that the result is for the correct test.
	 * Right now everything is serial so it should not be an
	 * issue but this could be a costly assumption.
	 * 
	 * @param result
	 */
	public void processTestCaseResult(
		String result)
	{
		
		// Set the return state
		if ( result.contains(ExecutionProtocol.STR_EXEC_BOMBED) ) {
			setExecutionState(STATE_BOMBED);
			setExecutionResult(RESULT_FAILED);
		} else if ( result.contains(ExecutionProtocol.STR_EXEC_PASSED) ) {
			setExecutionState(STATE_COMPLETED);
			setExecutionResult(RESULT_PASSED);
		} else if ( result.contains(ExecutionProtocol.STR_EXEC_BLOCKED) ) {
			setExecutionState(STATE_COMPLETED);
			setExecutionResult(RESULT_BLOCKED);
		} else {
			setExecutionState(STATE_COMPLETED);
			setExecutionResult(RESULT_FAILED);
		}
		
		// Notes are expected at the end of the string
	}
	
	public void openConnection(
		int port) throws Exception
	{
		try {
			fSocket = new Socket("localhost", port);
			out = new PrintWriter(fSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(fSocket.getInputStream()));
		} catch ( UnknownHostException e ) {
			System.err.println("Don't know about host: localhost.");
			throw e;
		} catch ( IOException e ) {
			System.err.println("Couldn't get I/O for the connection to: localhost.");
			throw e;
		} catch ( Exception e ) {
			System.err.println("Couldn't get connection established.");
			throw e;
		}
	}
	
	/**
	 * Close all the connections. No exception is thrown. The exceptions
	 * are ignored in case the other side is already closed.
	 */
	public void closeConnection()
	{
		try {
			out.close();
		} catch ( Exception e ) {}
		
		try {
			in.close();
		} catch ( Exception e ) {}
	
		try {
			stdIn.close();
		} catch ( Exception e ) {}

		try {
			fSocket.close();
		} catch ( Exception e ) {}
	}
}
