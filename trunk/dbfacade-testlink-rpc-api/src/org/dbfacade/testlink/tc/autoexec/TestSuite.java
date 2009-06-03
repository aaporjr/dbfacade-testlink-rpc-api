package org.dbfacade.testlink.tc.autoexec;

import java.util.Map;

import org.dbfacade.testlink.api.client.TestLinkAPIConst;
import org.dbfacade.testlink.api.client.TestLinkAPIException;

/**
 * Intended to hold the information of an existing test suite in
 * the TestLink database. That is why the only way to instantiate
 * the class is using a Map to pass the suite information returned 
 * by the TestLink API.
 * 
 * @author Daniel Padilla
 *
 */
public class TestSuite {
	private String suiteName;
	private Integer suiteID;

	/**
	 * Constructs a TestSuite instance when provided with information
	 * about the the suite using a Map result from the TestLink API
	 * for a suite. 
	 * 
	 * @param suiteInfo
	 * @throws TestLinkAPIException
	 */
	public TestSuite(
		Map suiteInfo) throws TestLinkAPIException
	{
		if ( suiteInfo == null ) {
			throw new TestLinkAPIException("The TestSuite class object instance could not be created.");
		}
		
		// Suite Name
		Object value = suiteInfo.get(TestLinkAPIConst.API_RESULT_NAME);
		if ( value == null ) {
			throw new TestLinkAPIException(
				"The setter does not allow null values for suite name.");
		} else {
			this.suiteName = value.toString();
		}
		
		// Identifier
		value = suiteInfo.get(TestLinkAPIConst.API_RESULT_IDENTIFIER);
		if ( value == null ) {
			throw new TestLinkAPIException(
			"The setter does not allow null values for suite identifier.");
		} else {
			this.suiteID = new Integer(value.toString());
		}
		
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the name of the suite with which the test case is associated.
	 * 
	 * @return
	 */
	public String getSuiteName()
	{
		return suiteName;
	}
	

	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the internal identifier of the suite with which the test case is associated.
	 * 
	 * @return
	 */
	public Integer getSuiteID()
	{
		return suiteID;
	}

}
