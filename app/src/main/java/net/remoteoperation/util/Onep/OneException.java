/*=============================================================================
* OneException.java
* Base class of exception. 
*==============================================================================
*
* Tested with JDK 1.6
*
* Copyright (c) 2011, Exosite LLC
* All rights reserved.
*/

package net.remoteoperation.util.Onep;

@SuppressWarnings("serial")
public class OneException extends Exception {

	public OneException(final String message) {
		super(message);
	}
}
