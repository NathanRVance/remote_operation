/*=============================================================================
* HttpRPCResponseException.java
* Exception class for http response failure.
*==============================================================================
*
* Tested with JDK 1.6
*
* Copyright (c) 2011, Exosite LLC
* All rights reserved.
*/

package net.remoteoperation.util.Onep;

@SuppressWarnings("serial")
public class HttpRPCResponseException extends OneException {

	public HttpRPCResponseException(final String message) {
		super(message);
	}
}
