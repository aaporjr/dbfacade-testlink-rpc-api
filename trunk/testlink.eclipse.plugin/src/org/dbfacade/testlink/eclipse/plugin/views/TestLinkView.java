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


import org.dbfacade.testlink.eclipse.plugin.UserMsg;
import org.dbfacade.testlink.eclipse.plugin.preferences.TestLinkPreferences;
import org.dbfacade.testlink.eclipse.plugin.views.tree.TestLinkTree;
import org.dbfacade.testlink.eclipse.plugin.views.tree.ViewContentProvider;
import org.dbfacade.testlink.eclipse.plugin.views.tree.ViewLabelProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;


/**
 * Add documentation here
 */

public class TestLinkView extends ViewPart
{
	// Singletons
	public static TestLinkTree testLinkTree;
	public static TreeViewer viewer;

	// locals
	private DrillDownAdapter drillDownAdapter;
	private Action doubleClickAction;
	private TestLinkActions testPlanActions = new TestLinkActions();

	class NameSorter extends ViewerSorter
	{}

	/**
	 * The constructor.
	 */
	public TestLinkView()
	{}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(
		Composite parent)
	{
		UserMsg.shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		createTreeView(parent);
	}
	
	public void createTreeView(
		Composite parent)
	{
		try {
			// Initialize view
			viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		
			// Setup user message display
			UserMsg.shell = viewer.getControl().getShell();
			
			// Setup preferences
			TestLinkPreferences prefs = new TestLinkPreferences();
		
			// Setup adapter
			drillDownAdapter = new DrillDownAdapter(viewer);
			testLinkTree = new TestLinkTree(prefs.getDefaultProject());
		
			// Setup content provider
			ViewContentProvider contentProvider = null;
			if ( TestLinkMode.isWorkbench() ) {
				contentProvider = new ViewContentProvider(getViewSite(),
					testLinkTree.getInvisibleRoot());
			} else {
				contentProvider = new ViewContentProvider(testLinkTree.getInvisibleRoot());
			}
			
			viewer.setContentProvider(contentProvider);
		
			// Setup label provider
			ViewLabelProvider labelProvider = new ViewLabelProvider(
				this.getConfigurationElement());
			viewer.setLabelProvider(labelProvider);
		
			// Complete tree setup
			// viewer.setSorter(new NameSorter());
			if ( TestLinkMode.isWorkbench() ) {
				viewer.setInput(getViewSite());
			} else {
				viewer.setInput(testLinkTree.getInvisibleRoot());
			}
		
			// Create actions
			testPlanActions.makeActions(labelProvider);
			
			// Create double click action
			doubleClickAction = new TestLinkAction(labelProvider,
				TestLinkAction.DOUBLE_CLICK, "Get information about the node.");

			// Assign actions
			hookContextMenu(viewer);
			hookDoubleClickAction();
			contributeToActionBars();	
		} catch ( Exception e ) {
			UserMsg.error(e, "Failed to create the TestLink view.");
		}
	}
	
	public static void refresh()
	{
		Object[] expanded = viewer.getExpandedElements();
		viewer.refresh();
		viewer.setExpandedElements(expanded);
	}
	
	public static void update(
		Object element)
	{
		try {
			if ( element != null ) {
				viewer.update(element, null);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			System.out.println("The element could not be update. " + element);
		}
	}
	
	public static void refresh(
		Object element)
	{

		try {
			if ( element != null ) {
				viewer.refresh(element, true);
				viewer.setExpandedState(element, true);
        
				// Stumbled across this way of making the tree finally refresh during a test
				// TODO: Need to figure how to get the test to refresh in an expanded way
				// much like the junit plugin
				viewer.collapseToLevel(element, TreeViewer.ALL_LEVELS);
				viewer.expandToLevel(element, TreeViewer.ALL_LEVELS);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			System.out.println("The element could not be refreshed. " + element);
		}
	}
	
	/*
	 * Private methods
	 */
	private void hookContextMenu(
		TreeViewer viewer)
	{
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		final TreeViewer v = viewer;
		menuMgr.addMenuListener(
			new IMenuListener()
		{
			public void menuAboutToShow(
				IMenuManager manager)
			{

				/*
				 * Using the selection is a way to decide what we want
				 * filled in as actions on the menu.
				 */
				IStructuredSelection selection = (IStructuredSelection) v
					.getSelection();
				
				Object node = null;
				if ( selection != null ) {
					node = selection.getFirstElement();
				}

				TestLinkView.this.testPlanActions.fillContextMenu(node, drillDownAdapter,
					manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		
		if ( TestLinkMode.isWorkbench() ) {
			IWorkbenchPartSite site = getSite();
			site.registerContextMenu(menuMgr, viewer);
		}
	}

	private void contributeToActionBars()
	{
		if ( TestLinkMode.isWorkbench() ) {
			IViewSite site = getViewSite();
			IActionBars bars = site.getActionBars();
			testPlanActions.fillLocalPullDown(bars.getMenuManager());
			testPlanActions.fillLocalToolBar(drillDownAdapter, bars.getToolBarManager());
		}
	}

	private void hookDoubleClickAction()
	{
		viewer.addDoubleClickListener(new IDoubleClickListener()
		{
			public void doubleClick(
				DoubleClickEvent event)
			{
				doubleClickAction.run();
			}
		});
	}
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus()
	{
		viewer.getControl().setFocus();
	}
}
