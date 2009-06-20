package org.dbfacade.testlink.tc.autoexec.server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * The opens a connection to a port. 
 * 
 * @author Daniel Padilla
 *
 */
public class RemoteClientConnection
{
	private Socket fSocket = null;
	private PrintWriter messageSend = null;
	private BufferedReader messageReceive = null;
	private int port = -1;
	
	public RemoteClientConnection(int port) {
		this.port = port;
	}
	
	
	public void openConnection() throws Exception
	{
		try {
			fSocket = new Socket("localhost", port);
			messageSend = new PrintWriter(fSocket.getOutputStream(), true);
			messageReceive = new BufferedReader(
				new InputStreamReader(fSocket.getInputStream()));
		} catch ( UnknownHostException e ) {
			System.err.println("Don't know about host: localhost.");
			throw e;
		} catch ( IOException e ) {
			System.err.println("Couldn't get I/O for the connection to: localhost.");
			throw e;
		} catch ( Exception e ) {
			System.err.println("Couldn't get connection established.");
			throw e;
		}
	}
		
	/**
	 * Close all the connections. No exception is thrown. The exceptions
	 * are ignored in case the other side is already closed.
	 */
	public void closeConnection()
	{
		try {
			messageSend.close();
		} catch ( Exception e ) {}
			
		try {
			messageReceive.close();
		} catch ( Exception e ) {}
		
		try {
			fSocket.close();
		} catch ( Exception e ) {}
	}
	
	/**
	 * Send a message to the server.
	 * 
	 * @param clientName
	 * @param message
	 */
	public void sendMessage(String clientName, String message) {
		messageSend.println(clientName + ExecutionProtocol.STR_CLIENT_SEPARATOR + message);	
	}
	
	/**
	 * Read a message from the server.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String receiveMessage() throws Exception {
		String receive = messageReceive.readLine();
		return receive;
	}
	
	/**
	 * True if the connection is good
	 * @return
	 */
	public boolean isGood() {
		return (fSocket.isConnected() && !fSocket.isClosed());
	}
	
	/**
	 * Return the port for this connection
	 * 
	 * @return
	 */
	public int getPort() {
		return port;
	}
}
