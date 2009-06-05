package org.dbfacade.testlink.eclipse.plugin.handlers;


import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.IParameterValues;


public class TestLinkHandlerParams implements IParameterValues
{

	public TestLinkHandlerParams()
	{
		super();
	}

	@Override
	public Map getParameterValues()
	{
		Map map = new HashMap();
		map.put("test.commandParameter", "blah");
		return map;
	}	
}

