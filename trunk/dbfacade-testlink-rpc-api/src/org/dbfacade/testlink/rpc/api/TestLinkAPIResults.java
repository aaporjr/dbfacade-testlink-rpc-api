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
public class TestLinkAPIResults implements TestLinkAPIConst {
	ArrayList<Map> results = new ArrayList<Map>();

	public void add(Map item) {
		results.add(item);
	}
	
	public void remove(int index) {
		results.remove(index);
	}
	
	public Map getData(int index) {
		return (Map) results.get(index);
	}
	
	public Object getValue(int index, String name) {
		Map result = (Map) results.get(index);
		return result.get(name);
	}
	
	public int size() {
		return results.size();
	}
}
