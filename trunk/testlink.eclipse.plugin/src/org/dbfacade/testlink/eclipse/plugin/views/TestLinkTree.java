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

import org.dbfacade.testlink.eclipse.plugin.views.tree.TreeObject;
import org.dbfacade.testlink.eclipse.plugin.views.tree.TreeParent;

public class TestLinkTree {
	private TreeParent invisibleRoot;
	
	public TestLinkTree() {
		initialize();
	}
	
	public TreeParent getInvisibleRoot() {
		return invisibleRoot;
	}
	/*
	 * We will set up a dummy model to initialize tree hierarchy.
	 * In a real code, you will connect to a real model and
	 * expose its hierarchy.
	 */
	private void initialize()
	{
		TreeObject to1 = new TreeObject("Leaf 1");
		TreeObject to2 = new TreeObject("Leaf 2");
		TreeObject to3 = new TreeObject("Leaf 3");
		TreeParent p1 = new TreeParent("Parent 1");
		p1.addChild(to1);
		p1.addChild(to2);
		p1.addChild(to3);
			
		TreeObject to4 = new TreeObject("Leaf 4");
		TreeParent p2 = new TreeParent("Parent 2");
		p2.addChild(to4);
			
		TreeParent root = new TreeParent("Root");
		root.addChild(p1);
		root.addChild(p2);
			
		invisibleRoot = new TreeParent("");
		invisibleRoot.addChild(root);
	}

}
