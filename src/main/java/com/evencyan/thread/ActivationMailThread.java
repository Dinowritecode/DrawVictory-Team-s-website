package com.evencyan.thread;

import com.evencyan.util.MailUtil;

public class ActivationMailThread extends Thread {
    private String username, receiveMailAccount, activationLink;



    public ActivationMailThread(String username, String receiveMailAccount, String link) {
        super.setName("MailSender-" + username);
        this.setPriority(Thread.MIN_PRIORITY);
        this.username = username;
        this.receiveMailAccount = receiveMailAccount;
        this.activationLink = link;
    }

    @Override
    public void run() {
        MailUtil.sendMail(receiveMailAccount, "账号激活",
                MailUtil.mailBody.replaceAll("%username%", username).replaceAll("%link%", activationLink));
    }
}
