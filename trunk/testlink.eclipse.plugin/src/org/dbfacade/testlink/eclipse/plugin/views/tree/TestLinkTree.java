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
import org.eclipse.jface.viewers.TreeViewer;


public class TestLinkTree
{
	private TreeParent invisibleRoot;
	private ProjectTree visibleRoot;
	private String projectName;
	
	public TestLinkTree(
		String projectName)
	{
		this.projectName = projectName;
		visibleRoot = new ProjectTree("Loading " + projectName + "....instantiate");
		invisibleRoot = new TreeParent("");
		invisibleRoot.addChild(visibleRoot);
	}
	
	public TreeParent getInvisibleRoot()
	{
		return invisibleRoot;
	}
	
	public void completeInitialization(
		TreeViewer viewer)
	{
			
		try {
			updateMessage(viewer, "init TestLink API Client");
			TestLinkPreferences pref = new TestLinkPreferences();
			TestPlanPrepare prep = pref.getTestPlanPrepare();
			TestLinkAPIClient apiClient = pref.getTestLinkAPIClient();
			TestProject project = new TestProject(apiClient, projectName);

			updateMessage(viewer, "getting test plans");
			TestPlanLoader loader = new TestPlanLoader(projectName, apiClient);
			Map<Integer,
				TestPlan> plans = loader.getPlans();
			
			Iterator planIDs = plans.keySet().iterator();
			while ( planIDs.hasNext() ) {
				Object planID = planIDs.next();
				TestPlan plan = plans.get(planID);

				updateMessage(viewer, "prepare test plan " + plan.getTestPlanName());
				prep.setTCUser(pref.getTestCaseCreator());
				prep.setExternalDirectory(pref.getExternalDirectory());
				prep.adjust(apiClient, plan);
				PlanTree planRoot = new PlanTree(plan);
				visibleRoot.addChild(planRoot);
			}
			visibleRoot.setProject(project);
		} catch ( Exception e ) {
			visibleRoot.setName("Unable to load " + projectName);
			if ( e.getMessage() != null ) {
				StackTraceElement[] trace = e.getStackTrace();
				for ( int i = 0; i < trace.length; i++ ) {
					StackTraceElement et = trace[i];
					if ( i > 10 ) {
						break;
					}
				}
			}
		}
		
		// Refresh the tree
		viewer.refresh();
	}
	
	private void updateMessage(TreeViewer viewer, String message) {
		visibleRoot.setName("Loading " + projectName + "..." + message);
		viewer.refresh();
	}

}
