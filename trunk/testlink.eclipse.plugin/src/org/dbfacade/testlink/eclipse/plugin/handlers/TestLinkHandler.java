package org.dbfacade.testlink.eclipse.plugin.handlers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.ShowViewHandler;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class TestLinkHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public TestLinkHandler() {
	}

	/**
	 * In this handler we define the test link view that we want to go to to process the execution of tests.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Map params = new HashMap();
		params.put("org.eclipse.ui.views.showView.viewId", "org.dbfacade.testlink.eclipse.plugin.views.TestLinkView");
		ShowViewHandler view = new ShowViewHandler();
		ExecutionEvent goToView = new ExecutionEvent(event.getCommand(), params, event.getTrigger(), event.getApplicationContext() );
		view.execute(goToView);
		return null;
	}
}
