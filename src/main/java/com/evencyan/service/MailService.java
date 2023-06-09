package com.evencyan.service;

public interface MailService {
    void sendActivationMail(String subject, String to, String username, String activationUrl);

    void sendExceptionMail(String type, String to, String info);
}
