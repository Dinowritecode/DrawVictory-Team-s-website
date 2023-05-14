package com.evencyan.thread;

import com.evencyan.util.MailUtil;

public class ActivationMailThread extends Thread {
    private String username, receiveMailAccount, activationLink;



    public ActivationMailThread(String username, String receiveMailAccount, String link) {
        super.setPriority(Thread.MIN_PRIORITY);
        super.setName("MailSender-" + username);
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
