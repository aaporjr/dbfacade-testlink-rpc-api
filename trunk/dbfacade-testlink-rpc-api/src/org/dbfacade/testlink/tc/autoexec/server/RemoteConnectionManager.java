package org.dbfacade.testlink.tc.autoexec.server;


import java.util.HashMap;
import java.util.Map;


public class RemoteConnectionManager
{
	private static Map connections = new HashMap();
	
	public static RemoteClientConnection getOrCreateConnection(
		int port)
	{
		RemoteClientConnection conn = (RemoteClientConnection) connections.get(port);
		if ( conn == null ) {
			conn = new RemoteClientConnection(port);
			try {
				conn.openConnection();
				connections.put(port, conn);
			} catch ( Exception e ) {
				e.printStackTrace();
			}	
		}
		return conn;
	}
	
	public static void closeClientConnection(
		int port)
	{
		RemoteClientConnection conn = (RemoteClientConnection) connections.get(port);
		if ( conn != null ) {
			conn.closeConnection();
		}
	}
	
}
