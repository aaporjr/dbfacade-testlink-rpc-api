package testlink.api.java.client.tc.autoexec.annotation.handlers;

import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.tc.autoexec.TestPlan;
import testlink.api.java.client.tc.autoexec.TestPlanPrepare;

/**
 * This class prepares the TestPlan for the execution server and takes
 * as a parameter an annotated class
 * @author DPadilla
 *
 */
public class LoadAnnotatedTestClass implements TestPlanPrepare {
	private String testClass;
	
	public LoadAnnotatedTestClass(String testClass) {
		this.testClass = testClass;
	}
	
public void setExternalPath(String path) {
	
}

	public void setTCUser(String user) {
		
	}

	public TestPlan adjust(
		TestLinkAPIClient apiClient,
		TestPlan plan) throws TestLinkAPIException {
		parseTestClassAndCreateTestCaseExecutors(apiClient, plan);
		return plan;
	}
	
	/*
	 * This is what will do the brunt of the work of taking apart the
	 * TestClass with annotations and making executors for is test case 
	 * using the annotated class methods.
	 */
	private void parseTestClassAndCreateTestCaseExecutors(TestLinkAPIClient apiClient, TestPlan plan) {
		
	}
}
