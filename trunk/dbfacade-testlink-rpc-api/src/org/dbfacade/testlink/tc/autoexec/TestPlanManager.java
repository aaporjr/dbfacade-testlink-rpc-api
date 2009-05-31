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


import java.util.Iterator;

import org.dbfacade.testlink.api.client.TestLinkAPIException;


/**
 * Currently not supported (class stub).
 * <p>
 * The class is used to manage the execution of automated test cases
 * associated with a test plan.
 * 
 * @author Daniel Padilla
 *
 */
public class TestPlanManager
{

	/**
	 * Currently not supported (constructor stub).
	 * <p
	 * When the TestPlanManager is instantiated then it retrieves all the
	 * test cases that are defined as automated test cases in the test plan.
	 * 
	 * @param projectName
	 * @param planName
	 */
	public TestPlanManager(
		String projectName,
		String planName)
	{
		init();
	}
	
	/**
	 * Currently not supported (constructor stub).
	 * <p>
	 * When the TestPlanManager is instantiated then it retrieves all the
	 * test cases that are defined as automated test cases in the test plan.
	 * <p>
	 * The cases are instantiated using the class that is passed to the constructor.
	 *
	 * @param projectName
	 * @param planName
	 * @param testCaseImplementionClass
	 */
	public TestPlanManager(
		String projectName,
		String planName,
		String testCaseImplementionClass)
	{
		init();
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Method is called by the constructor and it finds
	 * all the automated test cases in a test plan and
	 * then instantiates the classes using the initExistingCase()
	 * method. Once the case is instantiated it is stored in a list.
	 */
	private void init() {
		
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Adds a test case to the a project test suite if the test case does
	 * not exist and then it it adds it to the test plan.
	 * 
	 * @param testCase
	 * @throws TestLinkAPIException
	 */
	public void addTestCase(
		TestCase testCase) throws TestLinkAPIException
	{
		throw new TestLinkAPIException("The method is not currently supported.");
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the test case by name or visible id. The visible identifier can be
	 * viewed when looking at a test case suite tree in the TestLink web application.
	 * 
	 * @param caseNameOrVisibleID
	 * @return TestCase if found otherwise null
	 */
	public TestCase getTestCase(
		String caseNameOrVisibleID) throws TestLinkAPIException
	{
		throw new TestLinkAPIException("The method is not currently supported.");
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the test case by internal identifier.
	 * 
	 * @param caseID
	 * @return TestCase if found otherwise null
	 */
	public TestCase getTestCase(
		Integer caseID) throws TestLinkAPIException
	{
		throw new TestLinkAPIException("The method is not currently supported.");
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get an iterator for all the existing case names in this plan.
	 * 
	 * @return Iterator of test case internal identifiers
	 */
	public Iterator getTestCaseNames() throws TestLinkAPIException
	{
		throw new TestLinkAPIException("The method is not currently supported.");
	}
	

	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get an iterator for all the existing case visible identifiers in this plan.
	 * 
	 * @return Iterator of test case internal identifiers
	 */
	public Iterator getTestCaseVisibleIDs() throws TestLinkAPIException
	{
		throw new TestLinkAPIException("The method is not currently supported.");
	}
	
	

	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get an iterator for all the existing case internal identifiers in this plan.
	 * 
	 * @return Iterator of test case internal identifiers
	 */
	public Iterator getTestCaseInternalIDs() throws TestLinkAPIException
	{
		throw new TestLinkAPIException("The method is not currently supported.");
	}
	
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Execute the test cases for the instantiated plan and record the test case
	 * results. The execution records a failure for any test case that returns an 
	 * undefined result or throws an exception during execution.
	 * 
	 * @throws TestLinkAPIException
	 */
	public void executeTestCases() throws TestLinkAPIException
	{
		throw new TestLinkAPIException("The method is not currently supported.");
	}
}
