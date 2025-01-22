package com.acme_insurance.quote.infra.ports;

import com.sun.net.httpserver.Authenticator;

public interface RequestAuthenticator {

    Authenticator getAuthenticator();

}
