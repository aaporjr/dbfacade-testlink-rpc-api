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
package org.dbfacade.testlink.tc.autoexec;


import java.util.ArrayList;

import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.api.client.TestLinkAPIException;


public class ExecuteTestCases extends Thread
{
	private TestLinkAPIClient apiClient=null;
	private TestPlan testPlan;
	private boolean hasTestRun = false;
	private boolean hasTestFailed = false;
	private boolean reportResultsToTestLink=true;
	private TestCase[] cases;
	private TestCaseExecutor manualExecutor;
	private ArrayList<ExecuteTestCaseListener> listeners = new ArrayList();
	private String buildName=null;
	
	/**
	 * Executes the tests for test cases in a test plan.
	 * 
	 * @param apiClient             Optional
	 * @param plan					Required
	 * @param manualTestExecutor	Required
	 */
	public ExecuteTestCases(
		TestLinkAPIClient apiClient,
		TestPlan plan,
		TestCaseExecutor manualTestExecutor)
	{
		this(apiClient, plan, null, manualTestExecutor);
	}
	
	/**
	 * Executes the tests for test cases in a test plan.
	 * 
	 * @param apiClient             Optional
	 * @param plan					Required
	 * @param buildName				Required
	 * @param manualTestExecutor	Required
	 */
	public ExecuteTestCases(
		TestLinkAPIClient apiClient,
		TestPlan plan,
		String buildName,
		TestCaseExecutor manualTestExecutor)
	{
		this(apiClient, plan, plan.getTestCases(), buildName, manualTestExecutor);
	}
	
	/**
	 * Executes the cases against the test plan.
	 * 
	 * @param apiClient            	Optional
	 * @param plan					Required
	 * @param cases					Required
	 * @param buildName				Required
	 * @param manualTestExecutor	Required
	 */
	public ExecuteTestCases(
			TestLinkAPIClient apiClient,
		TestPlan plan,
		TestCase[] cases,
		String buildName,
		TestCaseExecutor manualTestExecutor)
	{
		this.apiClient = apiClient;
		this.testPlan = plan;
		this.cases = cases;
		this.buildName = buildName;
		this.manualExecutor = manualTestExecutor;
	}
	
	/**
	 * True if the test has run to completion
	 */
	public boolean hasTestRun()
	{
		return hasTestRun;
	}
	
	/**
	 * True if the all test passed
	 */
	public boolean hasTestPassed()
	{
		return !(hasTestFailed());
	}
	
	/**
	 * True if the a single test did not pass
	 */
	public boolean hasTestFailed()
	{
		return hasTestFailed;
	}

	/**
	 * Added an ExecuteTestCaseListener
	 * 
	 * @param listener
	 */
	public void addListtener(ExecuteTestCaseListener listener) {
		
	}
	

	/**
	 * Execute the test results and report results or
	 * execute the results in the background.
	 * 
	 * @param reportResults
	 * @param inBackground
	 */
	public void executeTestCases(
		boolean reportResultsToTestLink,
		boolean runInBackground)
	{
		this.reportResultsToTestLink = reportResultsToTestLink;
		if ( runInBackground ) {
			this.start();
		} else {
			run();
		}
	}
	
	/**
	 * Execution of the test cases. This class is not
	 * recommended that it be started with this method
	 * until all the variables have been assigned.
	 * 
	 * @param reportResults
	 */
	public void run()
	{
		hasTestRun = false;
		try {
			if ( this.testPlan == null ||
			     this.cases == null) {
				throw new TestLinkAPIException("All the variables have not been set so tests cannot be executed.");
			}
			hasTestFailed = false;
			for ( int i = 0; i < cases.length; i++ ) {
			
				TestCase tc = cases[i];
				TestCaseExecutor te = tc.getExecutor();
				
				testCaseStart(tc);
				
				// If no executor is registered then create empty and run empty
				if ( te == null && tc.isAutoExec() ) {
					testCaseWithoutExecutor(tc);
					te = new EmptyExecutor();
				}
			
				// Execute the test case exception does not mean failure
				try {
					if ( tc.isManualExec() ) {
						te = manualExecutor;
						if ( te == null ) {
							te = new EmptyExecutor();
							testCaseWithoutExecutor(tc);
						}
						tc.setExecutor(te);
					} 
					te.execute(tc);
					if ( te.getExecutionResult() != TestCaseExecutor.RESULT_PASSED ) {
						hasTestFailed = true;
					}
				} catch ( Exception e ) {
					te.setExecutionResult(TestCaseExecutor.RESULT_FAILED);
					te.setExecutionState(TestCaseExecutor.STATE_BOMBED);
					hasTestFailed = true;
					testCaseBombed(tc, te, e);
				}
			
				if ( reportResultsToTestLink && apiClient != null) {
					try {
						TestCaseUtils.reportTestResult(apiClient, testPlan, tc, te, buildName) ;
					} catch ( Exception e ) {
						hasTestFailed = true;
						testCaseReportResultsFailed(tc, te, e);
					}
				}
			
				testCaseCompleted(tc, te);
			}
		} catch ( Exception e ) {
			hasTestFailed = true;
			executionFailed(e);
		}
		hasTestRun = true;
	}
	
	/*
	 * Private methods
	 */
	
	/*
	 * Called before the test case runs
	 * 
	 * @param event
	 */
	private void testCaseStart(TestCase tc) {
		ExecuteTestCaseEvent event = new ExecuteTestCaseEvent();
		event.eventType = ExecuteTestCaseEvent.TEST_CASE_START;
		event.testPlan = testPlan;
		event.testCase = tc;
		for (int i=0; i < listeners.size(); i++) {
			ExecuteTestCaseListener listener = listeners.get(i);
			listener.testCaseStart(event);
		}
	}
	
	/*
	 * Called if a test case is found without a registered executor
	 * 
	 * @param event
	 */
	private void testCaseWithoutExecutor(TestCase tc) {
		ExecuteTestCaseEvent event  = new ExecuteTestCaseEvent();
		event.eventType = ExecuteTestCaseEvent.TEST_CASE_EXECUTOR_MISSING;
		event.testPlan = testPlan;
		event.testCase = tc;
		for (int i=0; i < listeners.size(); i++) {
			ExecuteTestCaseListener listener = listeners.get(i);
			listener.testCaseWithoutExecutor(event);
		}
	}
		
	/*
	 * Called if report results to listener is enabled.
	 * 
	 * @param event
	 */
	private void testCaseReportResultsFailed(TestCase tc, TestCaseExecutor te, Exception e) {
		ExecuteTestCaseEvent event = new ExecuteTestCaseEvent();
		event.eventType = ExecuteTestCaseEvent.TEST_CASE_REPORTING_FAILED;
		event.testPlan = testPlan;
		event.testCase = tc;
		event.testExecutor=te;
		event.e = e;
		for (int i=0; i < listeners.size(); i++) {
			ExecuteTestCaseListener listener = listeners.get(i);
			listener.testCaseReportResultsFailed(event);
		}
	}
	
	/*
	 * Called at any time the test cases is being processed
	 * and has not reached completion and there is an execption.
	 * 
	 * @param event
	 */
	private void testCaseBombed(TestCase tc, TestCaseExecutor te, Exception e) {
		ExecuteTestCaseEvent event = new ExecuteTestCaseEvent();
		event.eventType = ExecuteTestCaseEvent.TEST_CASE_BOMBED;
		event.testPlan = testPlan;
		event.testCase = tc;
		event.testExecutor=te;
		event.e = e;
		for (int i=0; i < listeners.size(); i++) {
			ExecuteTestCaseListener listener = listeners.get(i);
			listener.testCaseBombed(event);
		}
	}
	
	/*
	 * Called when the test case has completed and the results
	 * have been registered.
	 * 
	 * @param event
	 */
	private void testCaseCompleted(TestCase tc, TestCaseExecutor te) {
		ExecuteTestCaseEvent event = new ExecuteTestCaseEvent();
		event.eventType = ExecuteTestCaseEvent.TEST_CASE_COMPLETED;
		event.testPlan = testPlan;
		event.testCase = tc;
		event.testExecutor=te;
		for (int i=0; i < listeners.size(); i++) {
			ExecuteTestCaseListener listener = listeners.get(i);
			listener.testCaseCompleted(event);
		}
	}
	
	/*
	 * Called when the test case has completed and the results
	 * have been registered.
	 * 
	 * @param event
	 */
	private void executionFailed(Exception e) {
		ExecuteTestCaseEvent event = new ExecuteTestCaseEvent();
		event.eventType = ExecuteTestCaseEvent.TEST_CASE_COMPLETED;
		event.testPlan = testPlan;
		event.e = e;
		for (int i=0; i < listeners.size(); i++) {
			ExecuteTestCaseListener listener = listeners.get(i);
			listener.executionFailed(event);
		}
	}
	
}