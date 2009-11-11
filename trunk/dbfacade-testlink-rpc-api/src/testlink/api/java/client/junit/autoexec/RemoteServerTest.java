package testlink.api.java.client.junit.autoexec;


import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.junit.constants.TestConst;
import testlink.api.java.client.tc.autoexec.example.RandomTestResultPrep;
import testlink.api.java.client.tc.autoexec.server.ExecutionServer;

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
