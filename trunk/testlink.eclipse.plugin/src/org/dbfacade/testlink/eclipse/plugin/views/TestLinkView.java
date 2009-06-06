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
import org.dbfacade.testlink.eclipse.plugin.views.tree.TestLinkTree;
import org.dbfacade.testlink.eclipse.plugin.views.tree.ViewContentProvider;
import org.dbfacade.testlink.eclipse.plugin.views.tree.ViewLabelProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;


/**
 * Add documentation here
 */

public class TestLinkView extends ViewPart {
	private DrillDownAdapter drillDownAdapter;
	private TreeViewer viewer;
	private Action doubleClickAction;
	private TestPlanActions testPlanActions = new TestPlanActions();

	class NameSorter extends ViewerSorter {
	}


	/**
	 * The constructor.
	 */
	public TestLinkView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		TestLinkPreferences prefs = new TestLinkPreferences();
		TestLinkTree projectTree = new TestLinkTree(prefs.getDefaultProject());
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new ViewContentProvider(getViewSite(), projectTree.getInvisibleRoot()));
		viewer.setLabelProvider(new ViewLabelProvider(this.getConfigurationElement()));
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		testPlanActions.makeActions(viewer, doubleClickAction);
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		this.setTitleToolTip(projectTree.getToolTip());
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				TestLinkView.this.testPlanActions.fillContextMenu(drillDownAdapter, manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		testPlanActions.fillLocalPullDown(bars.getMenuManager());
		testPlanActions.fillLocalToolBar(drillDownAdapter, bars.getToolBarManager());
	}



	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	


	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}