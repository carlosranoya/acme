package com.acme_insurance.quote.ports.out;

public interface QuoteReceivedMessageSender {

    boolean sendMessage(Long id);

}
