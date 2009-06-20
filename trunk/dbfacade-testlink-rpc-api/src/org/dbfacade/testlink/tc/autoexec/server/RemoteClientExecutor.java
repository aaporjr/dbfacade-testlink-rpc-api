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
package org.dbfacade.testlink.tc.autoexec.server;


import org.dbfacade.testlink.tc.autoexec.EmptyExecutor;
import org.dbfacade.testlink.tc.autoexec.TestCase;
import org.dbfacade.testlink.tc.autoexec.TestCaseExecutor;
import org.dbfacade.testlink.tc.autoexec.TestPlan;


public class RemoteClientExecutor extends EmptyExecutor
{
	private ExecutionProtocol ep = new ExecutionProtocol();
	private String fromServer;
	private TestPlan testPlan;
	private boolean isPreped = false;
	private String clientName = null;
	private RemoteClientConnection conn;

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
		ExecutionProtocol.inDebugMode = true;
		this.testPlan = testPlan;
		this.clientName = this.toString();
		int retry = 0;
		while ( retry < 3 ) {
			try {
				retry++;
				Thread.sleep(500);
				conn = RemoteConnectionManager.getOrCreateConnection(port);
				sendMessage(ExecutionProtocol.STR_PING);
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
	
	public void sendMessage(
		String message)
	{
		ExecutionProtocol.debug("Send Message: " + clientName + ExecutionProtocol.STR_CLIENT_SEPARATOR + message);
		conn.sendMessage(clientName, message);	
	}
    
	/**
	 * Send the server a shutdown request from the client
	 */
	public void sendServerShutdownRequest()
	{
		sendMessage(ExecutionProtocol.STR_SHUTDOWN);	
	}
	
	/**
	 * Ask the server to prepare the test plan
	 */
	public void sendPlanPrepareRequest()
	{
		try {
			String request = ExecutionProtocol.STR_PLANPREP_REQUEST 
				+ ExecutionProtocol.STR_REQUEST_PROJECT_NAME
				+ testPlan.getProject().getProjectName()
				+ ExecutionProtocol.STR_REQUEST_PLAN_NAME + testPlan.getTestPlanName();
			sendMessage(request);	
			// Wait for the response
			while ( (fromServer = conn.receiveMessage()) != null ) {
				ep.processInput("client prep " + conn.getPort(), fromServer);
				if ( ep.shutdown() ) {
					ExecutionProtocol.debug("Shutdown request sent by server");
					break;
				}
				ExecutionProtocol.debug("Server: " + fromServer);
				if ( fromServer.startsWith(
					clientName + ExecutionProtocol.STR_CLIENT_SEPARATOR)
						&& fromServer.startsWith(ExecutionProtocol.STR_PLANPREP_RESULT) ) {
					if ( fromServer.contains(ExecutionProtocol.STR_PLANPREP_PASSED) ) {
						isPreped = true;
					} else {
						isPreped = false;
					}
					ExecutionProtocol.debug("Result from server: " + fromServer);
					break;
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * True if the plan was successfully prepared by the remote server.
	 * 
	 * @return
	 */
	public boolean isPreped()
	{
		return isPreped;
	}
	
	/**
	 * Make a request for remote test case execution.
	 */
	public void execute(
		TestCase tc)
	{
		try {
			
			// Check connection
			if ( ! conn.isGood() ) {
				throw new Exception("The connection is no longer available.");
			}
			
			// Reset
			setExecutionResult(TestCaseExecutor.RESULT_UNKNOWN);
			setExecutionState(TestCaseExecutor.STATE_READY);
			setExecutionNotes("");
			
			// Build and send the request
			sendTestCaseRequest(tc);
			
			// Wait for the response
			while ( (fromServer = conn.receiveMessage()) != null ) {
				ep.processInput("client tc exec " + conn.getPort() +", " + clientName, fromServer);
				if ( fromServer.startsWith(
					clientName + ExecutionProtocol.STR_CLIENT_SEPARATOR)
						&& fromServer.contains(ExecutionProtocol.STR_TC_RESULT) ) {
					ExecutionProtocol.debug("Result from server: " + fromServer);
					break;
				} else {
					ExecutionProtocol.debug("Dispose from server: " + fromServer);
				}
			}
			
			// Process Result
			fromServer = fromServer.substring(
				fromServer.indexOf(ExecutionProtocol.STR_CLIENT_SEPARATOR)
					+ ExecutionProtocol.STR_CLIENT_SEPARATOR.length());
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
		String request = ExecutionProtocol.STR_TC_REQUEST 
			+ ExecutionProtocol.STR_REQUEST_PROJECT_NAME + tc.getProjectName()
			+ ExecutionProtocol.STR_REQUEST_PLAN_NAME + testPlan.getTestPlanName()
			+ ExecutionProtocol.STR_REQUEST_TC_EXEC + tc.getTestCaseInternalID().toString();
		sendMessage(request);
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
	

	
	/**
	 * Return the default client identifier plus project name.
	 */
	public final String toString()
	{
		String superToString = super.toString();
		return this.testPlan.getTestPlanName() + "@" + superToString;
	}
}
