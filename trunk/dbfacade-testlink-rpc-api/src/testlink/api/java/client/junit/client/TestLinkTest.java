package testlink.api.java.client.junit.client;

import testlink.api.java.client.TestLinkAPIClient;

/**
 * Expects the test to throw an exception if it fails.
 * 
 * @author DPadilla
 *
 */
public interface TestLinkTest {
	public void runTest(String version, TestLinkAPIClient api) throws Exception;
}
