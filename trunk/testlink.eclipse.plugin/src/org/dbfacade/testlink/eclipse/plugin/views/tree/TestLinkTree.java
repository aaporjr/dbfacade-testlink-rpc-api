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
	private static TreeParent invisibleRoot;
	
	// First visible root
	private ProjectTree visibleRoot;
	
	public TestLinkTree(
		String failMessage)
	{
		if ( invisibleRoot == null ) {
			invisibleRoot = new TreeParent("");
		} else {
			TreeObject[] children = invisibleRoot.getChildren(true);
			for (int i=0; i < children.length; i++) {
				TreeObject child = children[i];
				invisibleRoot.removeChild(child);
			}
		}
		addPreferedProject(visibleRoot, failMessage);
	}
	
	public static void addPreferedProject(
		ProjectTree visibleRoot,
		String failMessage)
	{
		try {
			TestLinkPreferences pref = new TestLinkPreferences();
			addProject(visibleRoot, pref.getDefaultProject());
		} catch ( Exception e ) {
			visibleRoot = new ProjectTree("Unable to build : " + failMessage);
			invisibleRoot.addChild(visibleRoot);
			UserMsg.error(e, "Failed to build the project root node.");
		}	
	}
	
	public static void addProject(
		ProjectTree visibleRoot,
		String projectName)
	{
		try {
			TestLinkPreferences pref = new TestLinkPreferences();
			TestLinkAPIClient apiClient = pref.getTestLinkAPIClient();
			TestProject project = new TestProject(apiClient, projectName);
			visibleRoot = new ProjectTree(project);
			invisibleRoot.addChild(visibleRoot);
		} catch ( Exception e ) {
			visibleRoot = new ProjectTree("Unable to build : " + projectName);
			invisibleRoot.addChild(visibleRoot);
			UserMsg.error(e, "Failed to build the project root node.");
		}			
	}
	
	public static TreeParent getInvisibleRoot()
	{
		return invisibleRoot;
	}
}
