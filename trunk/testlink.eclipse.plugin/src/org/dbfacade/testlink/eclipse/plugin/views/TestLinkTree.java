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
package org.dbfacade.testlink.eclipse.plugin.views;

import org.dbfacade.testlink.eclipse.plugin.preferences.TestLinkPreferences;
import org.dbfacade.testlink.eclipse.plugin.views.tree.TreeParent;
import org.dbfacade.testlink.tc.autoexec.TestProject;

public class TestLinkTree {
	private TreeParent invisibleRoot;
	
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
		TreeParent root = null;
		
		try {
			TestLinkPreferences pref = new TestLinkPreferences();
			TestProject project = new TestProject(pref.getTestLinkAPIClient(), projectName);
			root = new TreeParent(projectName);
			root.setContent(project);
		} catch (Exception e) {
			root = new TreeParent("Unable to build project tree: " + projectName);
		}
		
			
		invisibleRoot = new TreeParent("");
		invisibleRoot.addChild(root);
	}

}
