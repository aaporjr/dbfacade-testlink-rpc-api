package org.dbfacade.testlink.eclipse.plugin.views.tree;

import org.dbfacade.testlink.tc.autoexec.TestCase;
import org.dbfacade.testlink.tc.autoexec.TestPlan;

public class PlanTree extends TreeParent {
	TestPlan plan;
	public PlanTree(TestPlan plan) {
		super(plan.getTestPlanName());
		this.plan = plan;
		
		// Get Test Cases
		TestCase[] cases = this.plan.getTestCases();
		for (int i=0; i < cases.length; i++) {
			TestCase tc = cases[i];
			TestCaseLeaf tcLeaf = new TestCaseLeaf(tc);
			this.addChild(tcLeaf);
		}
	}
	
	public TestPlan getTestPlan() {
		return plan;
	}
}
