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
package org.dbfacade.testlink.junit.autoexec;


import static org.junit.Assert.fail;

import java.util.Map;

import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.api.client.TestLinkAPIConst;
import org.dbfacade.testlink.api.client.TestLinkAPIHelper;
import org.dbfacade.testlink.tc.autoexec.ExecutableTestCase;
import org.dbfacade.testlink.tc.autoexec.TestCase;
import org.dbfacade.testlink.tc.autoexec.TestProject;
import org.dbfacade.testlink.tc.autoexec.TestSuite;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * 
 * Test used to verify ExecutableTestCase implementation
 * 
 * @author Daniel Padilla
 *
 */
public class ExecutableTestCaseTest implements TestLinkAPIConst
{
	
	// The URL path to testing TestLink application instance. You want to avoid production.
	private final String apiURL = "http://localhost/testlink/lib/api/xmlrpc.php";
	
	// The user script_key value
	private final String userKey = "fc7eaf2092095e912af73ce44c26080b";
	
	// The api instance
	private TestLinkAPIClient api;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		api = new TestLinkAPIClient(userKey, apiURL, true);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{}
	
	/**
	 * Test ExecutableTestCase.initExistingCase() method
	 */
	@Test
	public void testTestPlanConstructor()
	{
		try {
			Map projectInfo= TestLinkAPIHelper.getProjectInfo(api, JUNIT_PLAN_PROJECT);
			TestProject project = new TestProject(projectInfo);
			project.getProjectID();
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to initialize the existing test case.");
		}
	}

	/**
	 * Test ExecutableTestCase.initExistingCase() method
	 */
	@Test
	public void testInitExistingCase()
	{
		try {
			// Get IDs
			Integer projectID = TestLinkAPIHelper.getProjectID(api, JUNIT_PLAN_PROJECT);
			Integer caseID = TestLinkAPIHelper.getCaseIDByName(api, projectID, JUNIT_PLAN_CASE);
			
			// Get information for the ids
			Map testCaseInfo = TestLinkAPIHelper.getTestCaseInfo(api, projectID, caseID);
			Map projectInfo= TestLinkAPIHelper.getProjectInfo(api, JUNIT_PLAN_PROJECT);
			Map suiteInfo = TestLinkAPIHelper.getSuiteInfo(api, projectID, JUNIT_PLAN_SUITE);
			
			// Create the classes
			TestProject testProject = new TestProject(projectInfo);
			TestSuite testSuite = new TestSuite(suiteInfo);
			TestCase testCase = new ExecutableTestCase();
			
			// Test the initializer
			testCase.initExistingCase(testProject, testSuite, testCaseInfo);
			
		} catch ( Exception e ) {
			fail("Failed to initialize the existing test case.");
		}
	}
	
	
	
	
}

