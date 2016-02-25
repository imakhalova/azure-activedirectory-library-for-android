package com.microsoft.aad.adal;

/**
 * ADAL exception for the case when server response doesn't have expected data
 * e.g. missing header, wrong status code, invalid format
 */
class AuthenticationServerProtocolException extends Exception {
    static final long serialVersionUID = 1;

    public AuthenticationServerProtocolException(String detailMessage) {
        super(detailMessage);
    }
}
