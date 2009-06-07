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
package org.dbfacade.testlink.eclipse.plugin.handlers;


import java.util.HashMap;
import java.util.Map;

import org.dbfacade.testlink.eclipse.plugin.UserMsg;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.ShowViewHandler;


/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class TestLinkHandler extends AbstractHandler
{

	/**
	 * The constructor.
	 */
	public TestLinkHandler()
	{}

	/**
	 * In this handler we define the test link view that we want to go to to process the execution of tests.
	 */
	public Object execute(
		ExecutionEvent event) throws ExecutionException
	{
		try {
			Map params = new HashMap();
			params.put("org.eclipse.ui.views.showView.viewId",
				"org.dbfacade.testlink.eclipse.plugin.views.TestLinkView");
			ShowViewHandler view = new ShowViewHandler();
			ExecutionEvent goToView = new ExecutionEvent(event.getCommand(), params,
				event.getTrigger(), event.getApplicationContext());
			view.execute(goToView);
		} catch ( Exception e ) {
			UserMsg.error(e, "Unable to open the TestLink View.");
		}
		return null;
	}
}
