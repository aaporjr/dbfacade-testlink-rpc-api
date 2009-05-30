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
package org.dbfacade.testlink.api.client.junit;


import static org.junit.Assert.fail;

import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.api.client.TestLinkAPIConst;
import org.dbfacade.testlink.api.client.TestLinkAPIResults;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


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
public class TestLinkAPIGetterTest implements TestLinkAPIConst
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
	 * Test method create project
	 */
	@Test
	public void testAbount()
	{
		try {
			TestLinkAPIResults results = api.about();
			if ( results == null ) {
				throw new Exception("Unable to run about method.");
			}
		} catch ( Exception e ) {
			fail("Failed to run TestLink API about method.");
		}
	}
	
	
	
	
}

