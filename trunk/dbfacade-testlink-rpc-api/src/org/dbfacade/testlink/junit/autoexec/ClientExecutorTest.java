package org.dbfacade.testlink.junit.autoexec;

import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.api.client.TestLinkAPIConst;
import org.dbfacade.testlink.junit.constants.TestConst;
import org.dbfacade.testlink.tc.autoexec.ExecuteTestCases;
import org.dbfacade.testlink.tc.autoexec.TestPlan;
import org.dbfacade.testlink.tc.autoexec.server.ExecutionProtocol;
import org.dbfacade.testlink.tc.autoexec.server.ExecutionServer;

public class ClientExecutorTest {
	private static TestLinkAPIClient api;

	public static void main(String[] args) throws Exception
	{
		try {
			api = new TestLinkAPIClient(TestConst.userKey, TestConst.apiURL, true);
			api.ping();
			ExecutionProtocol.inDebugMode=true;
			int port = 59168;
			System.out.println(port);
			TestPlan tp = new TestPlan(api, TestLinkAPIConst.JUNIT_PLAN_PROJECT, TestLinkAPIConst.JUNIT_PLAN_NAME);
			ExecuteTestCases tcexec = new ExecuteTestCases(api, tp, null);
			tcexec.setRemoteExecutionMode(port);
			tcexec.executeTestCases(false, false);
			// ExecutionServer.sendServerShutdownRequest(port);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
}
