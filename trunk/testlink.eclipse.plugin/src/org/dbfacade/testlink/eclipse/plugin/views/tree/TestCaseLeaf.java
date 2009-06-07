package org.dbfacade.testlink.eclipse.plugin.views.tree;

import org.dbfacade.testlink.tc.autoexec.TestCase;

public class TestCaseLeaf extends TreeObject {
	private TestCase testCase;
	
	public TestCaseLeaf(String testCaseName) {
		super(testCaseName);
	}
	
	public TestCaseLeaf(TestCase tc) {
		super(tc.getTestCaseName());
		this.testCase = tc;
	}
	
	public TestCase getTestCase() {
		return testCase;
	}
}
