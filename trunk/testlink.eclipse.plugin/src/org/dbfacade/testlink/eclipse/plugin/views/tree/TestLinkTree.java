/*
 * Daniel R Padilla
 *
 * Copyright (c) 2009, Daniel R Padilla
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.dbfacade.testlink.eclipse.plugin.views.tree;

import java.util.Iterator;
import java.util.Map;

import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.eclipse.plugin.preferences.TestLinkPreferences;
import org.dbfacade.testlink.tc.autoexec.TestPlan;
import org.dbfacade.testlink.tc.autoexec.TestPlanLoader;
import org.dbfacade.testlink.tc.autoexec.TestPlanPrepare;
import org.dbfacade.testlink.tc.autoexec.TestProject;

public class TestLinkTree {
	private TreeParent invisibleRoot;
	private String toolTip="";
	
	public TestLinkTree(String projectName) {
		initialize(projectName);
	}
	
	public TreeParent getInvisibleRoot() {
		return invisibleRoot;
	}
	/*
	 * We will set up a dummy model to initialize tree hierarchy.
	 * In a real code, you will connect to a real model and
	 * expose its hierarchy.
	 */
	private void initialize(String projectName)
	{	
		ProjectTree root = null;
		
		try {
			TestLinkPreferences pref = new TestLinkPreferences();
			TestPlanPrepare prep = pref.getTestPlanPrepare();
			TestLinkAPIClient apiClient = pref.getTestLinkAPIClient();
			TestProject project = new TestProject(apiClient, projectName);
			root = new ProjectTree(project);

			
			TestPlanLoader loader = new TestPlanLoader(projectName, apiClient);
			Map<Integer, TestPlan> plans = loader.getPlans();
			
			Iterator planIDs = plans.keySet().iterator();
			while (planIDs.hasNext()) {
				
				// Get the plan information
				Object planID = planIDs.next();
				TestPlan plan = plans.get(planID);
				prep.setTCUser(pref.getTestCaseCreator());
				prep.setExternalDirectory(pref.getExternalDirectory());
				prep.adjust(apiClient, plan);
				PlanTree planRoot = new PlanTree(plan);
				root.addChild(planRoot);
			}
			
			invisibleRoot = new TreeParent("");
			invisibleRoot.addChild(root);
		} catch (Exception e) {
			TreeParent bad = new TreeParent("Unable to build project tree: " + projectName);
			invisibleRoot = new TreeParent("");
			invisibleRoot.addChild(bad);
			if ( e.getMessage() != null ) {
				StackTraceElement[] trace = e.getStackTrace();
				toolTip = e.getMessage();
				for (int i=0; i < trace.length; i++) {
					StackTraceElement et = trace[i];
					toolTip += "\n" + et.toString();
					if ( i > 10 ) {
						break;
					}
				}
			}
		}
	}
	
	public String getToolTip() {
		return toolTip;
	}

}
