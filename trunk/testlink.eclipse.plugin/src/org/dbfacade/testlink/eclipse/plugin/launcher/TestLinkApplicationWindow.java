package org.dbfacade.testlink.eclipse.plugin.launcher;


import org.dbfacade.testlink.eclipse.plugin.views.TestLinkView;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;


/**
 * The main shell for the TestLink executable jar application.
 * 
 * @author Daniel Padilla
 * @deprecated
 *
 */
public class TestLinkApplicationWindow extends ApplicationWindow
{	
	public TestLinkApplicationWindow()
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
