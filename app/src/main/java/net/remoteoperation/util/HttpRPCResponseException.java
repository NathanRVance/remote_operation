package net.remoteoperation.util;

/**
 * Created by nathav63 on 6/24/15.
 */
@SuppressWarnings("serial")
public class HttpRPCResponseException extends Exception {

    public HttpRPCResponseException(final String message) {
        super(message);
    }
}
