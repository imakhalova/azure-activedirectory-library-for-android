package com.microsoft.aad.adal;

/**
 * ADAL exception for device challenge processing
 */
class AuthenticationServerProtocolException extends Exception {
    public AuthenticationServerProtocolException(String detailMessage) {
        super(detailMessage);
    }
}

