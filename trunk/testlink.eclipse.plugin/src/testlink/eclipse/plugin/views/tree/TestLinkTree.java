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
package testlink.eclipse.plugin.views.tree;



import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.tc.autoexec.TestProject;
import testlink.eclipse.plugin.UserMsg;
import testlink.eclipse.plugin.preferences.TestLinkPreferences;


public class TestLinkTree
{
	// Singleton for a single viewer
	private TreeParentNode invisibleRoot;
	
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
		addPreferedProject(prefs, prefs.getDefaultProject());
	}
	
	
	public ProjectTree addPreferedProject(
		TestLinkPreferences pref,
		String failMessage)
	{
		ProjectTree visibleRoot = null;
		try {
			visibleRoot = addProject( pref, pref.getDefaultProject());
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
		return visibleRoot;
	}
	
	public ProjectTree addProject(
		TestLinkPreferences pref,
		String projectName)
	{
		return addProject(pref, projectName, -1);		
	}
	
	public ProjectTree addProject(
		TestLinkPreferences pref,
		String projectName,
		int port)
	{
		ProjectTree visibleRoot = null;
		try {
			TestLinkAPIClient apiClient = pref.getTestLinkAPIClient();
			TestProject project = new TestProject(apiClient, projectName);
			visibleRoot = new ProjectTree(project, pref, port);
			invisibleRoot.addChild(visibleRoot);
		} catch ( Exception e ) {
			visibleRoot = new ProjectTree(ProjectTree.UNABLE_TO_OPEN_PREFIX + projectName);
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
		return visibleRoot;
	}
	
	public TreeParentNode getInvisibleRoot()
	{
		return invisibleRoot;
	}
}
