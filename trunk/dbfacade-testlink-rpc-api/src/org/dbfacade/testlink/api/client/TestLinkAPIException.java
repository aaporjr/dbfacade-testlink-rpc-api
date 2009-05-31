package org.dbfacade.testlink.api.client;


/**
 * The exception is used to indicate the conditions under which a failure
 * occurred during a call to a TestLink API Java Client method.
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
