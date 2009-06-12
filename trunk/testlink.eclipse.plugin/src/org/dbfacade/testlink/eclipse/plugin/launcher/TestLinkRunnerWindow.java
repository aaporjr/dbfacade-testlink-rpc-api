package org.dbfacade.testlink.eclipse.plugin.launcher;


import org.dbfacade.testlink.eclipse.plugin.views.TestLinkView;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;


/**
 * Has the same plugin.xml parameters for command hardcoded
 * into the runner.
 * 
 * @author Daniel Padilla
 *
 */
public class TestLinkRunnerWindow extends ApplicationWindow
{	
	public TestLinkRunnerWindow()
	{
		super(null);
	}
	
	protected Control createContents(
		Composite parent)
	{
		parent.setSize(400, 600);
		getShell().setText("TestLink Test Plan Execution Runner");
		// Create window contents
		TestLinkView testLinkView = new TestLinkView();
		testLinkView.createTreeView(parent);
		Tree tree = TestLinkView.viewer.getTree();
		return tree;
	}          
}
