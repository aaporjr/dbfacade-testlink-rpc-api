package org.dbfacade.testlink.junit.autoexec;

import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.junit.constants.TestConst;
import org.dbfacade.testlink.tc.autoexec.example.RandomTestResultPrep;
import org.dbfacade.testlink.tc.autoexec.server.ExecutionServer;

public class RemoteServerTest {
	private static TestLinkAPIClient api;

	public static void main(String[] args) throws Exception
	{
		try {
			api = new TestLinkAPIClient(TestConst.userKey, TestConst.apiURL, true);
			api.ping();
			int port = 59168;
			System.out.println(port);
			RandomTestResultPrep prep = new RandomTestResultPrep();
			ExecutionServer server = new ExecutionServer(port, api, prep, null, null);
			server.start();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}		
}
