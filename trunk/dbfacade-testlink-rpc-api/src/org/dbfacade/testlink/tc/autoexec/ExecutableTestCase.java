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

import java.util.HashMap;
import java.util.Map;

import org.dbfacade.testlink.api.client.TestLinkAPIException;
import org.dbfacade.testlink.api.client.TestLinkAPIResults;


/**
 * Currently not supported (class stub).
 * <p>
 * Default implementation of the TestCase interface for test cases that
 * can be executed.
 * 
 * @author Daniel Padilla
 *
 */
public class ExecutableTestCase implements TestCase
{
	private String projectName;
	private Integer projectID;
	private String suiteName;
	private Integer suiteID;
	private String testCaseName;
	private String testCaseVisibleID;
	private Integer testCaseID;
	private String testCaseSummary;
	private String testCaseSteps;
	private String testCaseExpectedResults;
	private Map custom = new HashMap();
	private TestCaseExecutor autoTestExecutor=null;
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Using the data returned from the TestLink API the test
	 * case is initialized.
	 * 
	 * @param testCase
	 */
	public void initExistingCase(
		TestLinkAPIResults testCase) throws TestLinkAPIException {
		
		projectName = "";
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the name of the project with which the test case is associated.
	 * 
	 * @return
	 */
	public String getProjectName(){
		return projectName;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Set the name of the project with which the test case is associated.
	 * 
	 * @param projectName
	 */
	public void setProjectName(
		String projectName){
		this.projectName = projectName;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the internal identifier of the project with which the test case is associated.
	 * 
	 * @return
	 */
	public Integer getProjectID(){
		return projectID;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Set the internal identifier for the project with which the test case is associated.
	 * 
	 * @param id
	 */
	public void setProjectID(
		Integer id){
		this.projectID = id;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the name of the test suite with which the test case is associated.
	 * 
	 * @return
	 */
	public String getSuiteName(){
		return suiteName;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Set the name of the test suite with which the test case is associated.
	 * 
	 * @param suiteName
	 */
	public void setSuiteName(
		String suiteName){
		this.suiteName = suiteName;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the internal identifier of the test suite with which the test case is associated.
	 * 
	 * @return
	 */
	public Integer getSuiteID(){
		return suiteID;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Set the internal identifier of the test suite with which the test case is associated.
	 * 
	 * @param id
	 */
	public void setSuiteID(
		Integer id){
		this.suiteID = id;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the name of the test case.
	 * 
	 * @return
	 */
	public String getTestCaseName(){
		return testCaseName;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Set the name of the test case.
	 * 
	 * @param caseName
	 */
	public void setTestCaseName(
		String caseName) {
		this.testCaseName = caseName;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the test case's visible identifier in the web application.
	 * 
	 * @return
	 */
	public String getTestCaseVisibleID(){
		return testCaseVisibleID;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Set the test case's visible identifier as seen in the web application.
	 * 
	 * @param visibleID
	 */
	public void setTestCaseVisibleID(
		String visibleID){
		this.testCaseVisibleID = visibleID;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the test case's internal identifier.
	 * 
	 * @return
	 */
	public Integer getTestCaseInternalID(){
		return testCaseID;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Set the test case's internal identifier if it is available.
	 * 
	 * @param id
	 */
	public void setTestCaseInternalIDID(
		Integer id){
		this.testCaseID = id;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the test case summary information.
	 * 
	 * @return
	 */
	public String getTestCaseSummary(){
		return testCaseSummary;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Set the test case summary information.
	 * 
	 * @param summary
	 */
	public void setTestCaseSummary(
		String summary){
		this.testCaseSummary = summary;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the execution steps for the test case.
	 * 
	 * @return
	 */
	public String getTestCaseSteps(){
		return testCaseSteps;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Set the execution steps for the test case.
	 * 
	 * @param steps
	 */
	public void setTestCaseSteps(
		String steps){
		this.testCaseSteps = steps;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the test case execution expected results.
	 * 
	 * @return
	 */
	public String getTestCaseExpectedResults(){
		return testCaseExpectedResults;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Set the test case's expected results.
	 * 
	 * @param expectedResults
	 */
	public void setTestCaseExpectedResults(
		String expectedResults){
		this.testCaseExpectedResults = expectedResults;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get custom information.
	 * 
	 * @param fieldName
	 * @return
	 */
	public String getTestCaseCustomFieldContents(
		String fieldName){
		return (String) this.custom.get(fieldName);
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Set custom information.
	 * 
	 * @param fieldName
	 * @param contents
	 */
	public void setTestCaseCustomFieldContents(
		String fieldName,
		String contents){
		this.custom.put(fieldName, contents);
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the test case's automated execution instance.
	 * @return
	 */
	public TestCaseExecutor getExecutor(){
		return autoTestExecutor;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Reqister the object that will be responsible for executing the test case automatically.
	 * 
	 * @param executor
	 */
	public void setExecutor(
		TestCaseExecutor executor){
		this.autoTestExecutor = executor;
	}
}
