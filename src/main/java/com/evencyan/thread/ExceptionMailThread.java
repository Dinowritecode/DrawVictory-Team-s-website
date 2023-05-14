package com.evencyan.thread;

import com.evencyan.util.MailUtil;

public class ExceptionMailThread extends Thread{
    private String receiveAcc, subject, body;

    public ExceptionMailThread(String receiveAcc, String subject, String body) {
        this.receiveAcc = receiveAcc;
        this.subject = subject;
        this.body = body;
        this.setPriority(Thread.MIN_PRIORITY);
        this.setName("ExceptionMail");
    }

    @Override
    public void run() {
        MailUtil.sendMail(receiveAcc, subject, body);
    }
}
