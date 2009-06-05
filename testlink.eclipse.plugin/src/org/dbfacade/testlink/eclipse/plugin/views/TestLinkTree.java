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
