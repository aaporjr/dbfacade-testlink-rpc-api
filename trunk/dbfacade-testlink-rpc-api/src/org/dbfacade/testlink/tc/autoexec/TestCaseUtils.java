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


import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.api.client.TestLinkAPIConst;
import org.dbfacade.testlink.api.client.TestLinkAPIException;

/**
 * Test case utilities
 * 
 * @author Daniel Padilla
 *
 */
public class TestCaseUtils
{
	/**
	 * Report the test results for a test case.
	 * 
	 * @param apiClient
	 * @param testPlan
	 * @param tc
	 * @param buildName
	 * @throws TestLinkAPIException
	 */
	public static void reportTestResult(
		TestLinkAPIClient apiClient,
		TestPlan testPlan,
		TestCase tc,
		String buildName) throws TestLinkAPIException
	{
		TestCaseExecutor te = tc.getExecutor();
		if ( te != null ) {
			reportTestResult(apiClient, testPlan, tc, te, buildName);
		} else {
			throw new TestLinkAPIException(
				"No executor registered with the test case " + tc.getTestCaseName() + ".");
		}
	}
	
	/**
	 * Report the test results for a test case with external TestExecutor.
	 * 
	 * @param apiClient
	 * @param testPlan
	 * @param tc
	 * @param te
	 * @param buildName
	 * @throws TestLinkAPIException
	 */
	public static void reportTestResult(
		TestLinkAPIClient apiClient,
		TestPlan testPlan,
		TestCase tc,
		TestCaseExecutor te,
		String buildName) throws TestLinkAPIException
	{
		String result = TestLinkAPIConst.TEST_FAILED;
		if ( te.getExecutionResult() == TestCaseExecutor.RESULT_PASSED ) {
			result = TestLinkAPIConst.TEST_PASSED;
		} else if ( te.getExecutionResult() == TestCaseExecutor.RESULT_BLOCKED ) {
			result = TestLinkAPIConst.TEST_BLOCKED;
		}
		apiClient.reportTestCaseResult(testPlan.getProject().getProjectName(),
			testPlan.getTestPlanName(), tc.getTestCaseName(), buildName, null, result);
	}
}
