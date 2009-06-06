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
package org.dbfacade.testlink.junit.autoexec;


import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.Map;

import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.api.client.TestLinkAPIConst;
import org.dbfacade.testlink.junit.constants.TestConst;
import org.dbfacade.testlink.tc.autoexec.TestCase;
import org.dbfacade.testlink.tc.autoexec.TestPlan;
import org.dbfacade.testlink.tc.autoexec.TestPlanLoader;
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
public class TestPlanTest implements TestLinkAPIConst, TestConst
{	
	// The api instance
	private TestLinkAPIClient api;
	private static TestPlanLoader planLoader;
	
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
	 * Test TestPlanLoader
	 */
	@Test
	public void testTestPlanLoader()
	{
		try {
			planLoader = new TestPlanLoader(JUNIT_PLAN_PROJECT, api);
			System.out.println(planLoader.toString());
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to load the test plans.");
		}
	}
	
	/**
	 * Test ToArray
	 */
	@Test
	public void testToArray()
	{
		try {
			int cnt=0;
			Iterator ids = planLoader.getPlanIDs();
			while (ids.hasNext()) {
				Object id = ids.next();
				TestPlan plan = planLoader.getPlan(id);
				TestCase[] cases = plan.getTestCases();
				for (int i=0; i < cases.length; i++) {
					TestCase tc = cases[i];
					String name = tc.getTestCaseName();
					System.out.println(name);
					cnt++;
				}
			}
			if ( cnt == 0 ) {
				fail("No test cases were printed and some were expected.");
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Make sure we can get an array of test cases.");
		}
	}
	
	
}

