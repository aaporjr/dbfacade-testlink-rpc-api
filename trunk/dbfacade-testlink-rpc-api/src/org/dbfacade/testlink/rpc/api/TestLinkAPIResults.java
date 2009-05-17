/*
 * Database Facade
 *
 * Copyright (c) 2009, Database Facade
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
package org.dbfacade.testlink.rpc.api;


import java.util.ArrayList;
import java.util.Map;


/**
 * Used to return unparsed results from an XML-RPC call
 * to the TestLink API.
 * 
 * @author Daniel Padilla
 *
 */
public class TestLinkAPIResults implements TestLinkAPIConst
{
	ArrayList<Map> results = new ArrayList<Map>();

	/**
	 * Add a result to the list.
	 * 
	 * @param item
	 */
	public void add(
		Map item)
	{
		results.add(item);
	}
	
	/**
	 * Remove a result from the list
	 * 
	 * @param index
	 */
	public void remove(
		int index)
	{
		results.remove(index);
	}
	
	/**
	 * Get the result data in the list
	 * 
	 * @param index
	 * @return
	 */
	public Map getData(
		int index)
	{
		return (Map) results.get(index);
	}
	
	/**
	 * Get the values within the result data by name
	 * 
	 * @param index
	 * @param name
	 * @return
	 */
	public Object getValueByName(
		int index,
		String name)
	{
		Map result = getData(index);
		return result.get(name);
	}
	
	/**
	 * Get the size of the results list.
	 * 
	 * @return
	 */
	public int size()
	{
		return results.size();
	}
}
