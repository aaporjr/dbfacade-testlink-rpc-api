package org.dbfacade.testlink.tc.autoexec.server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.api.client.TestLinkAPIException;
import org.dbfacade.testlink.tc.autoexec.ExecuteTestCase;
import org.dbfacade.testlink.tc.autoexec.TestCase;
import org.dbfacade.testlink.tc.autoexec.TestCaseExecutor;
import org.dbfacade.testlink.tc.autoexec.TestPlan;
import org.dbfacade.testlink.tc.autoexec.TestPlanPrepare;


public class ExecutionServer extends Thread
{
	private int port;
	private TestLinkAPIClient apiClient;
	private TestPlan testPlan = null;
	TestPlanPrepare prep;
	String defaultTestCaseUser;
	String externalDir;

	/**
	 * The execution server expects a test plan that has
	 * already been prepared.
	 * 
	 * @param port
	 * @param apiClient
	 * @param testPlan
	 */
	public ExecutionServer(
		int port,
		TestLinkAPIClient apiClient,
		TestPlanPrepare prep,
		String defaultTestCaseUser,
		String externalDir)
	{
		this.port = port;
		this.apiClient = apiClient;
		this.prep = prep;
		this.defaultTestCaseUser = defaultTestCaseUser;
		this.externalDir = externalDir;
	}
	
	public void run()
	{
		try {
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket(port);
			} catch ( IOException e ) {
				System.err.println("Could not listen on port: " + port);
				System.exit(1);
			}

			Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept();
			} catch ( IOException e ) {
				System.err.println("Accept failed.");
				System.exit(1);
			}

			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(
				new InputStreamReader(clientSocket.getInputStream()));
			String inputLine,
				outputLine;
			ExecutionProtocol ep = new ExecutionProtocol();

			// Process the first output to check things out
			outputLine = ep.processInput(null);
			out.println(outputLine);
			ExecutionProtocol.debug(
				"The server is alive on locolhost port: " + port + ", output: " + outputLine);

			while ( (inputLine = in.readLine()) != null ) {
				
				// Process input and send answer
				outputLine = ep.processInput(inputLine);

				// After answer is sent then process the requests
				if ( ep.shutdown() ) {
					out.println(ExecutionProtocol.STR_SHUTDOWN);
					ExecutionProtocol.debug(
						"The TestLink test execution server on port " + port
						+ " is shutting down.");
					break;
				} else if ( ep.isRequest() 
					&& inputLine.contains(ExecutionProtocol.STR_REQUEST_PROJECT_NAME)
					&& inputLine.contains(ExecutionProtocol.STR_REQUEST_PLAN_NAME)
					&& inputLine.contains(ExecutionProtocol.STR_REQUEST_TC_EXEC) ) {
					outputLine = processTestCaseExecRequest(inputLine);
					ExecutionProtocol.debug(outputLine);
					out.println(outputLine);
				} else {
					out.println(ExecutionProtocol.STR_PING);
					continue;
				}
			}
			out.close();
			in.close();
			clientSocket.close();
			serverSocket.close();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Parses the client request and performs the request.
	 * 
	 * TODO: Process the request
	 * 
	 * @param request
	 */
	public String processTestCaseExecRequest(
		String request)
	{
		// Default result
		String result = ExecutionProtocol.STR_RESULT + ExecutionProtocol.STR_EXEC_BOMBED
			+ ExecutionProtocol.STR_EXEC_FAILED + ExecutionProtocol.STR_EXEC_NOTES
			+ "Did not find requested test case and was unable to execute it.";
		
		try {
			createTestPlan(request);
		} catch ( Exception e ) {
			// Do not use Request: it will confuse the protocol
			return result + "{Req: "
				+ request.replaceAll(ExecutionProtocol.STR_REQUEST, "") + ", Exception: "
				+ e.toString() + "}";
		}
		
		// Extract test case id
		int idx = request.indexOf(ExecutionProtocol.STR_REQUEST_TC_EXEC)
			+ ExecutionProtocol.STR_REQUEST_TC_EXEC.length();
		String strID = request.substring(idx);
		Integer internalID = new Integer(strID);

		TestCase tc = null;
		TestCase[] cases = testPlan.getTestCases();
		for ( int i = 0; i < cases.length; i++ ) {
			TestCase tmp = cases[i];
			if ( tmp.getTestCaseInternalID().intValue() == internalID.intValue() ) {
				tc = tmp;
				break;
			}
		}
	
		if ( tc != null ) {
			TestCaseExecutor te = tc.getExecutor();
			try {
				ExecuteTestCase.execute(testPlan, tc, te);
				result = ExecutionProtocol.STR_RESULT;
				if ( te.getExecutionState() == TestCaseExecutor.STATE_BOMBED ) {
					result += ExecutionProtocol.STR_EXEC_BOMBED
						+ ExecutionProtocol.STR_EXEC_FAILED
						+ ExecutionProtocol.STR_EXEC_NOTES + te.getExecutionNotes();					
				} else {
					result += ExecutionProtocol.STR_EXEC_COMPLETED;
					if ( te.getExecutionResult() == TestCaseExecutor.RESULT_PASSED ) {
						result += ExecutionProtocol.STR_EXEC_PASSED;
					} else if ( te.getExecutionResult() == TestCaseExecutor.RESULT_BLOCKED ) {
						result += ExecutionProtocol.STR_EXEC_BLOCKED;
					} else {
						result += ExecutionProtocol.STR_EXEC_FAILED;
					}
					result += ExecutionProtocol.STR_EXEC_NOTES + te.getExecutionNotes();
				}
			} catch ( Exception e ) {
				result = ExecutionProtocol.STR_RESULT + ExecutionProtocol.STR_EXEC_BOMBED
					+ ExecutionProtocol.STR_EXEC_FAILED + ExecutionProtocol.STR_EXEC_NOTES
					+ "The test cases execution failed with an exeception. [Exception: "
					+ e.toString() + "], [TC:" + internalID + "]";
			}
		} else {
			result = result + "[Test Case ID: " + internalID + "].";
		}
		return result;
	}
	
	public void createTestPlan(
		String request) throws Exception
	{
		int projectIdx = request.indexOf(ExecutionProtocol.STR_REQUEST_PROJECT_NAME);
		int planIdx = request.indexOf(ExecutionProtocol.STR_REQUEST_PLAN_NAME);
		int tcIdx = request.indexOf(ExecutionProtocol.STR_REQUEST_TC_EXEC);
		
		int start = projectIdx + ExecutionProtocol.STR_REQUEST_PROJECT_NAME.length();
		String projectName = request.substring(start, planIdx);
		
		start = planIdx + ExecutionProtocol.STR_REQUEST_PLAN_NAME.length();
		String planName = request.substring(start, tcIdx);
		
		start = tcIdx + ExecutionProtocol.STR_REQUEST_TC_EXEC.length();
		String caseInternalID = request.substring(start);
		
		ExecutionProtocol.debug(projectName + ", " + planName + ", " + caseInternalID);
		
		if ( testPlan == null || !(testPlan.getTestPlanName().equals(planName)) ) {
			testPlan = new TestPlan(apiClient, projectName, planName);	
			if ( prep != null ) {
				prep.setExternalPath(externalDir);
				prep.setTCUser(defaultTestCaseUser);
				prep.adjust(apiClient, testPlan);
			}
		}
	}
	
	/**
	 * Returns an open port on local host or throws an exception
	 * 
	 * @return
	 * @throws TestLinkAPIException
	 */
	public static int demandPort() throws TestLinkAPIException
	{
		int port = findFreePort();
		if ( port == -1 ) {
			throw new TestLinkAPIException(
				"Could not find a free port for the connection.");
		}
		return port;
	}
    
	/**
	 * Returns a free port number on localhost, or -1 if unable to find a free port.
	 *
	 * @return a free port number on localhost, or -1 if unable to find a free port
	 * @since 3.0
	 */
	public static int findFreePort()
	{
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(0);
			return socket.getLocalPort();
		} catch ( IOException e ) {} finally {
			if ( socket != null ) {
				try {
					socket.close();
				} catch ( IOException e ) {}
			}
		}
		return -1;
	}
	
	/**
	 * Open a client connection to the server port and send a shutdown request.
	 * 
	 * @param port
	 */
	public static void sendServerShutdownRequest(
		int port) throws Exception
	{
		PrintWriter cOut = null;
		Socket cSocket = null;
		try {
			cSocket = new Socket("localhost", port);
			cOut = new PrintWriter(cSocket.getOutputStream(), true);
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
		cOut.println(ExecutionProtocol.STR_SHUTDOWN);	
		cOut.close();
		cSocket.close();
	}

}

