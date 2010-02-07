package testlink.api.java.client.junit.client;

import java.util.HashMap;
import java.util.Iterator;

import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.TestLinkAPIConst;
import testlink.api.java.client.junit.constants.TestConst;

public class VersionListTestRunner implements TestLinkAPIConst, TestConst
{
	private HashMap<String,
	TestLinkAPIClient> apiList = new HashMap();

	/**
     * The constructor sets up all known
     * testable cases via hard code.
     */
	public VersionListTestRunner() {
		TestLinkAPIClient api = new TestLinkAPIClient(userKey, api182URL, true);
		apiList.put("TestLink1.8.2", api);
		api = new TestLinkAPIClient(userKey, api185URL, true);
		apiList.put("TestLink1.8.5", api);
	}
	
	
	public RunExceptionResults runTest(String method, TestLinkTest test) {
		RunExceptionResults results = new RunExceptionResults();
	    
	    // Run the test for all testable versions
		try {
		    TestLinkAPIClient api;
		    String version;
			Iterator versions = apiList.keySet().iterator();
			while ( versions.hasNext() ) {
				version = (String) versions.next();
				api = apiList.get(version);
				try {
					test.runTest(version, api);
					results.addResult(version, null);
				} catch (Exception e) {
					results.addResult(version, e);
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			return null;
		}
		
		// Print out result summary
		System.out.println("\n\n=======================================================");
		Iterator versions = results.getVersions();
		while ( versions.hasNext() ) {
			String version = (String) versions.next();
			Exception e = results.getResultException(version);
			if ( e != null ) {
				System.err.println(method + ": " + version + " (FAILED) => " + e.getMessage());
			} else {
				System.out.println(method + ": " + version + " (SUCCEDED)");
			}
		}
		System.out.println("=======================================================\n");
		return results;
	}
}
