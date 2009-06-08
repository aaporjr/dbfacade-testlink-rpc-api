package org.dbfacade.testlink.eclipse.plugin.views.tree;

import org.dbfacade.testlink.tc.autoexec.TestCase;
import org.dbfacade.testlink.tc.autoexec.TestCaseExecutor;

public class TestCaseLeaf extends TreeObject {
	public static final String AUTOMATED_AND_INCOMPLETE="(I) ";
	public static final String AUTOMATED_WITH_EXECUTOR="(A) ";
	public static final String MANUAL="(M) ";
	private TestCase testCase;
	private String leafType;
	
	public TestCaseLeaf(String testCaseName) {
		super(testCaseName);
	}
	
	public TestCaseLeaf(TestCase tc) {
		super(tc.getTestCaseName());
		if ( tc.isAutoExec()) {
			TestCaseExecutor te = tc.getExecutor();
			if ( te == null ) {
				leafType=AUTOMATED_AND_INCOMPLETE;
			} else {
				leafType=AUTOMATED_WITH_EXECUTOR;
			}
		} else {
			leafType = MANUAL;
		}
		this.setName(leafType + this.getName());
		this.testCase = tc;
	}
	
	public String getTestType() {
		return leafType;
	}
	
	public TestCase getTestCase() {
		return testCase;
	}
}
