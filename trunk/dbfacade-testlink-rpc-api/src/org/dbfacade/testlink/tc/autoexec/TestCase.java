/*
 * Database Facade
 *
 * Copyright (c) 2009, Database Facade
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


import org.dbfacade.testlink.api.client.TestLinkAPIException;
import org.dbfacade.testlink.api.client.TestLinkAPIResults;


public interface TestCase
{
	
	/**
	 * Using the data returned from the TestLink API the test
	 * case is initialized.
	 * 
	 * @param testCase
	 */
	public void initExistingCase(
		TestLinkAPIResults testCase) throws TestLinkAPIException;
	
	/**
	 * Get the name of the project with which the test case is associated.
	 * 
	 * @return
	 */
	public String getProjectName();
	
	/**
	 * Set the name of the project with which the test case is associated.
	 * 
	 * @param projectName
	 */
	public void setProjectName(
		String projectName);
	
	/**
	 * Get the internal identifier of the project with which the test case is associated.
	 * 
	 * @return
	 */
	public Integer getProjectID();
	
	/**
	 * Set the internal identifier for the project with which the test case is associated.
	 * 
	 * @param id
	 */
	public void setProjectID(
		Integer id);
	
	/**
	 * Get the name of the test suite with which the test case is associated.
	 * 
	 * @return
	 */
	public String getSuiteName();
	
	/**
	 * Set the name of the test suite with which the test case is associated.
	 * 
	 * @param suiteName
	 */
	public void setSuiteName(
		String suiteName);
	
	/**
	 * Get the internal identifier of the test suite with which the test case is associated.
	 * 
	 * @return
	 */
	public Integer getSuiteID();
	
	/**
	 * Set the internal identifier of the test suite with which the test case is associated.
	 * 
	 * @param id
	 */
	public void setSuiteID(
		Integer id);
	
	/**
	 * 
	 * Get the name of the test case.
	 * 
	 * @return
	 */
	public String getTestCaseName();
	
	/**
	 * Set the name of the test case.
	 * 
	 * @param caseName
	 */
	public void setTestCaseName(
		String caseName);
	
	/**
	 * Get the test case's visible identifier in the web application.
	 * 
	 * @return
	 */
	public String getTestCaseVisibleID();
	
	/**
	 * Set the test case's visible identifier as seen in the web application.
	 * 
	 * @param visibleID
	 */
	public void setTestCaseVisibleID(
		String visibleID);
	
	/**
	 * Get the test case's internal identifier.
	 * 
	 * @return
	 */
	public Integer getTestCaseInternalID();
	
	/**
	 * Set the test case's internal identifier if it is available.
	 * 
	 * @param id
	 */
	public void setTestCaseInternalIDID(
		Integer id);
	
	/**
	 * Get the test case summary information.
	 * 
	 * @return
	 */
	public String getTestCaseSummary();
	
	/**
	 * Set the test case summary information.
	 * 
	 * @param summary
	 */
	public void setTestCaseSummary(
		String summary);
	
	/**
	 * Get the execution steps for the test case.
	 * 
	 * @return
	 */
	public String getTestCaseSteps();
	
	/**
	 * Set the execution steps for the test case.
	 * 
	 * @param steps
	 */
	public void setTestCaseSteps(
		String steps);
	
	/**
	 * Get the test case execution expected results.
	 * 
	 * @return
	 */
	public String getTestCaseExpectedResults();
	
	/**
	 * Set the test case's expected results.
	 * 
	 * @param expectedResults
	 */
	public void setTestCaseExpectedResults(
		String expectedResults);
	
	/**
	 * get custom information.
	 * 
	 * @param fieldName
	 * @return
	 */
	public String getTestCaseCustomFieldContents(
		String fieldName);
	
	/**
	 * Set custom information.
	 * 
	 * @param fieldName
	 * @param contents
	 */
	public void setTestCaseCustomFieldContents(
		String fieldName,
		String contents);
	
	/**
	 * Get the test case's automated execution instance.
	 * @return
	 */
	public TestCaseExecutor getExecutor();
	
	/**
	 * Reqister the object that will be responsible for executing the test case automatically.
	 * 
	 * @param executor
	 */
	public void setExecutor(
		TestCaseExecutor executor);
	
}
