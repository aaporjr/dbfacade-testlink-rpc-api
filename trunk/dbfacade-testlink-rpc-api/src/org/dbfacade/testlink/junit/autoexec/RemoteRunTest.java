package org.dbfacade.testlink.junit.autoexec;

import static org.junit.Assert.fail;

import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.junit.constants.TestConst;
import org.dbfacade.testlink.tc.autoexec.server.ExecutionProtocol;
import org.dbfacade.testlink.tc.autoexec.server.ExecutionServer;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RemoteRunTest {

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
		api = new TestLinkAPIClient(TestConst.userKey, TestConst.apiURL, true);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{}
	
	/**
	 * Test Execution Server
	 */
	@Test
	public void testExecutionServer()
	{
		try {
			ExecutionProtocol.inDebugMode=true;
			int port = ExecutionServer.demandPort();
			ExecutionServer server = new ExecutionServer(port, api, null, null, null);
			server.start();
			ExecutionServer.sendServerShutdownRequest(port);
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed create server.");
		}
	}
		

		
}
