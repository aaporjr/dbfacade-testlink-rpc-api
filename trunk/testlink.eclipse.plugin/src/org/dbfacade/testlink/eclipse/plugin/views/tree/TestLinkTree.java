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


import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.eclipse.plugin.UserMsg;
import org.dbfacade.testlink.eclipse.plugin.preferences.TestLinkPreferences;
import org.dbfacade.testlink.tc.autoexec.TestProject;


public class TestLinkTree
{
	// Singleton for a single viewer
	private TreeParentNode invisibleRoot;
	
	// First visible root
	private ProjectTree visibleRoot;
	
	public TestLinkTree(
		TestLinkPreferences prefs)
	{
		if ( invisibleRoot == null ) {
			invisibleRoot = new TreeParentNode("");
		} else {
			TreeNode[] children = invisibleRoot.getChildren(true);
			for ( int i = 0; i < children.length; i++ ) {
				TreeNode child = children[i];
				invisibleRoot.removeChild(child);
			}
		}
		addPreferedProject(visibleRoot, prefs, prefs.getDefaultProject());
	}
	
	public void addPreferedProject(
		ProjectTree visibleRoot,
		TestLinkPreferences pref,
		String failMessage)
	{
		try {
			addProject(visibleRoot, pref, pref.getDefaultProject());
		} catch ( Exception e ) {
			visibleRoot = new ProjectTree(ProjectTree.UNABLE_TO_OPEN_PREFIX + failMessage);
			invisibleRoot.addChild(visibleRoot);
			if ( pref != null ) {
				UserMsg.error(e,
					"Failed to build the project root node."
					+ "\n\nTried to access TestLinkAPI at: " + pref.getTestLinkAPIURL()
					+ "\n\nCheck and verify the project, devKey and URL preferences are setup and are valid. Also make sure and check to see if the test TestLink system is available by using a browser.");
			} else {
				UserMsg.error(e,
					"Failed to build the project root node.\n"
					+ "The preferences for the project are null.");
			}
		}	
	}
	
	public void addProject(
		ProjectTree visibleRoot,
		TestLinkPreferences pref,
		String projectName)
	{
		try {
			TestLinkAPIClient apiClient = pref.getTestLinkAPIClient();
			TestProject project = new TestProject(apiClient, projectName);
			visibleRoot = new ProjectTree(project);
			visibleRoot.preferences = pref;
			invisibleRoot.addChild(visibleRoot);
		} catch ( Exception e ) {
			visibleRoot = new ProjectTree(ProjectTree.UNABLE_TO_OPEN_PREFIX + projectName);
			visibleRoot.preferences=pref;
			invisibleRoot.addChild(visibleRoot);
			if ( pref != null ) {
				UserMsg.error(e,
					"Failed to build the project root node."
					+ "\n\nTried to access TestLinkAPI at: " + pref.getTestLinkAPIURL()
					+ "\n\nCheck and verify that the requested project, devKey and URL are valid. Also make sure and check to see if the test TestLink system is available by using a browser.");
			} else {
				UserMsg.error(e,
					"Failed to build the project root node.\n"
					+ "The preferences for the project are null.");
			}
		}			
	}
	
	public TreeParentNode getInvisibleRoot()
	{
		return invisibleRoot;
	}
}
