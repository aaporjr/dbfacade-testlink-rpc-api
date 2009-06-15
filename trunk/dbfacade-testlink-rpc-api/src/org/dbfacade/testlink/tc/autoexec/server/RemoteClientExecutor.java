package org.dbfacade.testlink.tc.autoexec.server;


import java.io.*;
import java.net.*;

import org.dbfacade.testlink.tc.autoexec.EmptyExecutor;
import org.dbfacade.testlink.tc.autoexec.TestCase;
import org.dbfacade.testlink.tc.autoexec.TestCaseExecutor;


public class RemoteClientExecutor extends EmptyExecutor
{
	private ExecutionProtocol ep = new ExecutionProtocol();
	private BufferedReader stdIn;
	private String fromServer;
	private Socket fSocket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private boolean isOpen = false;
	private int port=0;

	/**
	 * Requires that the remote port be provided.
	 * 
	 * @param port
	 */
	public RemoteClientExecutor(
		int port)
	{	
		this.port = port;
	}
    
	/**
	 * Make a request for remote execution and then shut the test down.
	 */
	public void execute(TestCase tc)
	{
		try {
			int retry=0;
			while ( retry < 3 ) {
				try {
					retry++;
					Thread.sleep(500);
					openConnection(port);
					out.println(ExecutionProtocol.STR_PING);
					isOpen = true;
					ExecutionProtocol.debug("Opened connection to localhost port: " + port);
					break;
				} catch (Exception e) {
					retry++;
					if ( retry >= 3 ) {
						e.printStackTrace();
					}
					try {
						Thread.sleep(500);
					} catch (Exception se) {}
					continue;
				}
			}
			
			// Build and send the request
			sendTestCaseRequest(tc);
			
			// Wait for the response
			stdIn = new BufferedReader(new InputStreamReader(System.in));
			while ( isOpen && (fromServer = in.readLine()) != null ) {
				ep.processInput(fromServer);
				ExecutionProtocol.debug("Server: " + fromServer);
				if ( fromServer.startsWith(ExecutionProtocol.STR_RESULT) ) {
					ExecutionProtocol.debug("Result from server: " + fromServer);
					break;
				}
			}
			try {
				closeConnection();
			} catch (Exception e) {} // The server will be asked to shutdown later anyway
			processTestCaseResult(fromServer);
		} catch ( Exception e ) {
			setExecutionResult(TestCaseExecutor.RESULT_FAILED);
			setExecutionState(TestCaseExecutor.STATE_BOMBED);
			setExecutionNotes("Unable to complete the remote test request. " + e.toString());
			ExecutionProtocol.debug("Unable to complete the remote test request. " + e.toString());
		} 
	}
	
	/**
	 * Send the request for remote test case execution
	 * 
	 * @param tc
	 */
	public void sendTestCaseRequest(TestCase tc) {
		String request = ExecutionProtocol.STR_REQUEST +  ExecutionProtocol.STR_REQUEST_TYPE_TC_EXEC + tc.getTestCaseInternalID().toString();
		out.print(request);
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
	public void processTestCaseResult(String result) {
		
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
	
	public void openConnection(int port) throws Exception {
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
		} catch (Exception e) {
			System.err.println("Couldn't get connection established.");
			throw e;
		}
	}
	
	public void closeConnection() throws Exception
	{
		isOpen = false;
		out.close();
		in.close();
		stdIn.close();
		fSocket.close();
	}
}
