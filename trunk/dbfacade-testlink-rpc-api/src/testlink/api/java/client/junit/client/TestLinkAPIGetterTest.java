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
package testlink.api.java.client.junit.client;


import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.TestLinkAPIConst;
import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIHelper;
import testlink.api.java.client.TestLinkAPIResults;
import testlink.api.java.client.junit.constants.TestConst;


/**
 * 
 * Test used to verify the TestLink API Java implementation.
 * 
 * If you are going to use these tests it is recommended that 
 * you do it against a test installation of the TestLink system
 * especially the database since these test will create and
 * delete entries in the database.
 * 
 * @author Daniel Padilla
 *
 */
public class TestLinkAPIGetterTest implements TestLinkAPIConst,
	TestConst
{	
	// The api instance
	public static HashMap<String,
		TestLinkAPIClient> apiList = new HashMap();
	public static TestLinkAPIClient api = null;
	public static String version;
	
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
		if ( apiList.size() < 1 ) {
			TestLinkAPIClient api = new TestLinkAPIClient(userKey, api182URL, true);
			apiList.put("TestLink1.8.2", api);
			api = new TestLinkAPIClient(userKey, api185URL, true);
			apiList.put("TestLink1.8.5", api);
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{}

	/**
	 * Test method about
	 */
	@Test
	public void testAbout()
	{
		try {
			Iterator versions = apiList.keySet().iterator();
			while ( versions.hasNext() ) {
				version = (String) versions.next();
				api = apiList.get(version);
				TestLinkAPIResults results = api.about();
				if ( results == null ) {
					throw new Exception("Unable to run about method. Version=" + version);
				}
				printResults("testAbout (" + version + ") ", results);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to run TestLink API about method.");
		}
	}
	
	/**
	 * Test method pint
	 */
	@Test
	public void testPing()
	{
		try {
			Iterator versions = apiList.keySet().iterator();
			while ( versions.hasNext() ) {
				version = (String) versions.next();
				api = apiList.get(version);
				TestLinkAPIResults results = api.ping();
				if ( results == null ) {
					throw new Exception("Unable to run ping method. Version=" + version);
				}
				printResults("testPing (Version=" + version + ") ", results);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to run TestLink API ping method. Version=" + version);
		}
	}
	
	/**
	 * Test method getProjects
	 */
	@Test
	public void testGetProjects()
	{
		try {
			Iterator versions = apiList.keySet().iterator();
			while ( versions.hasNext() ) {
				version = (String) versions.next();
				api = apiList.get(version);
		
				TestLinkAPIResults results = api.getProjects();
				if ( results == null ) {
					throw new Exception("Unable to run getProject() method.");
				}
				printResults("testGetProjects (Version=" + version + "): ", results);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to run TestLink API getProjects() method. Version=" + version);
		}
	}
	
	/**
	 * Test method getFirstLevelTestSuitesForTestProject
	 */
	@Test
	public void testGetFirstLevelTestSuitesForTestProject()
	{
		try {
			Iterator versions = apiList.keySet().iterator();
			while ( versions.hasNext() ) {
				version = (String) versions.next();
				api = apiList.get(version);
				TestLinkAPIResults results = api.getFirstLevelTestSuitesForTestProject(
					JUNIT_STATIC_PROJECT);
				if ( results == null ) {
					throw new Exception(
						"Unable to run getFirstLevelTestSuitesForTestProject() method. Version="
							+ version);
				}
				printResults(
					"testGetFirstLevelTestSuitesForTestProject (Version=" + version + "): ",
					results);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail(
				"Failed to run TestLink API getFirstLevelTestSuitesForTestProject() method. Version="
					+ version);
		}
	}
	
	/**
	 * Test method getProjectTestPlans
	 */
	@Test
	public void testGetProjectTestPlans()
	{
		try {
			Iterator versions = apiList.keySet().iterator();
			while ( versions.hasNext() ) {
				version = (String) versions.next();
				api = apiList.get(version);
		
				TestLinkAPIResults results = api.getProjectTestPlans(JUNIT_STATIC_PROJECT);
				if ( results == null ) {
					throw new Exception("Unable to run getProjectTestPlans() method.");
				}
				printResults("testGetProjectTestPlans (Version=" + version + "): ",
					results);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail(
				"Failed to run TestLink API getProjectTestPlans() method. Version=" + version);
		}
	}
	
	/**
	 * Test method getBuildsForTestPlan
	 */
	@Test
	public void testGetBuildsForTestPlan()
	{
		try {
			Iterator versions = apiList.keySet().iterator();
			while ( versions.hasNext() ) {
				version = (String) versions.next();
				api = apiList.get(version);
		
				TestLinkAPIResults results = api.getBuildsForTestPlan(JUNIT_STATIC_PROJECT,
					JUNIT_STATIC_TEST_PLAN);
				if ( results == null ) {
					throw new Exception(
						"Unable to run getBuildsForTestPlan() method. Version=" + version);
				}
				printResults("testGetBuildsForTestPlan (Version=" + version + "): ",
					results);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail(
				"Failed to run TestLink API getBuildsForTestPlan() method. Version="
					+ version);
		}
	}
	
	/**
	 * Test method getLatestBuildForTestPlan
	 */
	@Test
	public void testGetLatestBuildForTestPlan()
	{
		try {
			
			Iterator versions = apiList.keySet().iterator();
			while ( versions.hasNext() ) {
				version = (String) versions.next();
				api = apiList.get(version);
				TestLinkAPIResults results = api.getLatestBuildForTestPlan(
					JUNIT_STATIC_PROJECT, JUNIT_STATIC_TEST_PLAN);
				if ( results == null ) {
					throw new Exception(
						"Unable to run getLatestBuildForTestPlan() method.");
				}
				printResults("testGetLatestBuildForTestPlans (Version=" + version + "): ",
					results);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail(
				"Failed to run TestLink API getLatestBuildForTestPlan() method. Version="
					+ version);
		}
	}
	
	/**
	 * Test method getTestSuitesForTestPlan
	 */
	@Test
	public void testGetTestSuitesForTestPlan()
	{
		try {
			
			Iterator versions = apiList.keySet().iterator();
			while ( versions.hasNext() ) {
				version = (String) versions.next();
				api = apiList.get(version);
				TestLinkAPIResults results = api.getTestSuitesForTestPlan(
					JUNIT_STATIC_PROJECT, JUNIT_STATIC_TEST_PLAN);
				if ( results == null ) {
					throw new Exception(
						"Unable to run getTestSuitesForTestPlan() method. Version=" + version);
				}
				printResults("testGetTestSuitesForTestPlan", results);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail(
				"Failed to run TestLink API getTestSuitesForTestPlan() method. Version="
					+ version);
		}
	}
	
	/**
	 * Test method getTestCaseIDByName
	 */
	@Test
	public void testGetTestCaseIDByName()
	{
		try {
			Iterator versions = apiList.keySet().iterator();
			while ( versions.hasNext() ) {
				version = (String) versions.next();
				api = apiList.get(version);
				TestLinkAPIResults results = api.getTestCaseIDByName(JUNIT_STATIC_TEST_PLAN);
				if ( results == null ) {
					throw new Exception(
						"Unable to run getTestCaseIDByName() method. Version=" + version);
				}
				printResults("testGetTestCaseIDByName (Version=" + version + "): ",
					results);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail(
				"Failed to run TestLink API getTestCaseIDByName() method. Version=" + version);
		}
	}
	
	/**
	 * Test method getLastExecutionResult
	 */
	@Test
	public void testGetLastExecutionResult()
	{
		try {
			Iterator versions = apiList.keySet().iterator();
			while ( versions.hasNext() ) {
				version = (String) versions.next();
				api = apiList.get(version);
				TestLinkAPIResults results = api.getLastExecutionResult(JUNIT_STATIC_PROJECT,
					JUNIT_STATIC_TEST_PLAN, JUNIT_STATIC_CASE);
				if ( results == null ) {
					throw new Exception(
						"Unable to run getLastExecutionResult() method. Version=" + version);
				}
				printResults("testGetLastExecutionResult (Version=" + version + "):",
					results);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to run TestLink API getLastExecutionResult() method.");
		}
	}
	
	/**
	 * Test method getCasesForTestPlan(projectName, planName)
	 */
	@Test
	public void testGetTestCasesByPlanName()
	{
		try {
			Iterator versions = apiList.keySet().iterator();
			while ( versions.hasNext() ) {
				version = (String) versions.next();
				api = apiList.get(version);
				TestLinkAPIResults results = api.getCasesForTestPlan(JUNIT_STATIC_PROJECT,
					JUNIT_STATIC_TEST_PLAN);
				if ( results == null ) {
					throw new Exception(
						"Unable to run getCasesForTestPlan() method. Version=" + version);
				}
				printResults("testGetTestCasesByPlanName (Version=" + version + "):",
					results);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail(
				"Failed to run TestLink API getCasesForTestPlan() method. Version=" + version);
		}
	}
	
	/**
	 * Test method getLastExecutionResult
	 */
	@Test
	public void testGetTestCasesManual()
	{
		try {
			Iterator versions = apiList.keySet().iterator();
			while ( versions.hasNext() ) {
				version = (String) versions.next();
				api = apiList.get(version);
				Integer projectID = TestLinkAPIHelper.getProjectID(api, JUNIT_STATIC_PROJECT);
				if ( projectID == null ) {
					throw new TestLinkAPIException(
						"Could not get project identifier for " + JUNIT_STATIC_PROJECT
						+ ". Version=" + version);
				}
				Integer planID = TestLinkAPIHelper.getPlanID(api, projectID,
					JUNIT_STATIC_TEST_PLAN);
				if ( planID == null ) {
					throw new TestLinkAPIException(
						"Could not get plan identifier for " + JUNIT_STATIC_TEST_PLAN + ". Version="
						+ version);
				}
				TestLinkAPIResults results = api.getCasesForTestPlan(planID, null, null,
					null, null, null, null, TESTCASE_EXECUTION_TYPE_MANUAL);
				if ( results == null ) {
					throw new Exception(
						"Unable to run getCasesForTestPlan() method. Version=" + version);
				}
				printResults("testGetTestCasesPassedManual (Version=" + version + "):",
					results);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail(
				"Failed to run TestLink API getCasesForTestPlan() method. Version=" + version);
		}
	}
	
	/**
	 * Test method getLastExecutionResult
	 */
	@Test
	public void testGetTestCasesAuto()
	{
		try {
			Iterator versions = apiList.keySet().iterator();
			while ( versions.hasNext() ) {
				version = (String) versions.next();
				api = apiList.get(version);
				Integer projectID = TestLinkAPIHelper.getProjectID(api, JUNIT_STATIC_PROJECT);
				if ( projectID == null ) {
					throw new TestLinkAPIException(
						"Could not get project identifier for " + JUNIT_STATIC_PROJECT
						+ ". Version=" + version);
				}
				Integer planID = TestLinkAPIHelper.getPlanID(api, projectID,
					JUNIT_STATIC_TEST_PLAN);
				if ( planID == null ) {
					throw new TestLinkAPIException(
						"Could not get plan identifier for " + JUNIT_STATIC_TEST_PLAN);
				}
				TestLinkAPIResults results = api.getCasesForTestPlan(planID, null, null,
					null, null, null, null, TESTCASE_EXECUTION_TYPE_AUTO);
				if ( results == null ) {
					throw new Exception(
						"Unable to run getCasesForTestPlan() method. Version=" + version);
				}
				printResults("testGetTestCasesPassedAuto (Version=" + version + "):",
					results);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail(
				"Failed to run TestLink API getCasesForTestPlan() method. Version=" + version);
		}
	}
	
	/**
	 * Test method getLastExecutionResult
	 */
	@Test
	public void testGetTestCasesFailed()
	{
		try {
			Iterator versions = apiList.keySet().iterator();
			while ( versions.hasNext() ) {
				version = (String) versions.next();
				api = apiList.get(version);
				Integer projectID = TestLinkAPIHelper.getProjectID(api, JUNIT_STATIC_PROJECT);
				if ( projectID == null ) {
					throw new TestLinkAPIException(
						"Could not get project identifier for " + JUNIT_STATIC_PROJECT
						+ " Version=" + version);
				}
				Integer planID = TestLinkAPIHelper.getPlanID(api, projectID,
					JUNIT_STATIC_TEST_PLAN);
				if ( planID == null ) {
					throw new TestLinkAPIException(
						"Could not get plan identifier for " + JUNIT_STATIC_TEST_PLAN);
				}
				TestLinkAPIResults results = api.getCasesForTestPlan(planID, null, null,
					null, null, null, TEST_FAILED, null);
				if ( results == null ) {
					throw new Exception(
						"Unable to run getCasesForTestPlan() method. Version=" + version);
				}
				printResults("testGetTestCasesFailed. (Version=" + version + "):", results);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail(
				"Failed to run TestLink API getCasesForTestPlan() method. Version=" + version);
		}
	}
	
	/**
	 * Test method getLastExecutionResult
	 */
	@Test
	public void testGetTestCasesPassed()
	{
		try {
			Iterator versions = apiList.keySet().iterator();
			while ( versions.hasNext() ) {
				version = (String) versions.next();
				api = apiList.get(version);
				Integer projectID = TestLinkAPIHelper.getProjectID(api, JUNIT_STATIC_PROJECT);
				if ( projectID == null ) {
					throw new TestLinkAPIException(
						"Could not get project identifier for " + JUNIT_STATIC_PROJECT);
				}
				Integer planID = TestLinkAPIHelper.getPlanID(api, projectID,
					JUNIT_STATIC_TEST_PLAN);
				if ( planID == null ) {
					throw new TestLinkAPIException(
						"Could not get plan identifier for " + JUNIT_STATIC_TEST_PLAN + ". Version="
						+ version);
				}
				TestLinkAPIResults results = api.getCasesForTestPlan(planID, null, null,
					null, null, null, TEST_PASSED, null);
				if ( results == null ) {
					throw new Exception(
						"Unable to run getCasesForTestPlan() method. Version=" + version);
				}
				printResults("testGetTestCasesPassed. (Version=" + version + "):", results);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail(
				"Failed to run TestLink API getCasesForTestPlan() method. Version=" + version);
		}
	}
	
	private void printResults(
		String method,
		TestLinkAPIResults results)
	{
		System.out.println(
			"\n----------------------------------------------------------------");
		System.out.println(method + " results: ");
		System.out.println(results.toString());
	}
	
}

