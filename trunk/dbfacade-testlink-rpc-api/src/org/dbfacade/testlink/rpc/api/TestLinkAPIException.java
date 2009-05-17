package org.dbfacade.testlink.rpc.api;


/**
 * The exception does not do anything special other
 * then report back with its own exception type.
 * 
 * @author Daniel Padilla
 *
 */
public class TestLinkAPIException extends Exception
{

	/**
	 * Create exception with a message.
	 * 
	 * @param msg
	 */
	public TestLinkAPIException(
		String msg)
	{
		super(msg);
	}

	/** 
	 * Create a nested exception with a new message.
	 * 
	 * @param msg
	 * @param e
	 */
	public TestLinkAPIException(
		String msg,
		Throwable e)
	{
		super(msg, e);
	}
}
