package com.leo618.mail;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * 邮件发送管理器
 */
@SuppressWarnings("ALL")
public class MailManager {
    private static final String SENDER_NAME     = "SENDER_NAME";
    private static final String SENDER_PASS     = "SENDER_PASS";
    private static final String VALUE_MAIL_HOST = "smtp.126.com";
    private static final String KEY_MAIL_HOST   = "mail.smtp.host";
    private static final String KEY_MAIL_AUTH   = "mail.smtp.auth";
    private static final String VALUE_MAIL_AUTH = "true";

    private static final AtomicReference<MailManager> INSTANCE = new AtomicReference<>();

    /** 获取邮件发送实例 */
    public static MailManager getInstance() {
        for (; ; ) {
            MailManager netManager = INSTANCE.get();
            if (netManager != null) return netManager;
            netManager = new MailManager();
            if (INSTANCE.compareAndSet(null, netManager)) return netManager;
        }
    }

    private MailManager() {}

    private Map<String, String> paramsMap     = new LinkedHashMap<>();
    private ArrayList<String>   recevierNames = new ArrayList<>();

    /**
     * 配置发送者的账号和密码(授权码)
     *
     * @param senderName     发送者账号
     * @param senderPassword 密码(授权码)
     * @param recevicerNames 接收者账号数组
     */
    public MailManager config(String senderName, String senderPassword, String... recevicerNames) {
        paramsMap.clear();
        paramsMap.put(SENDER_NAME, senderName);
        paramsMap.put(SENDER_PASS, senderPassword);
        recevierNames.clear();
        recevierNames.addAll(Arrays.asList(recevicerNames));
        return getInstance();
    }

    public boolean sendMail(final String title, final String content) {
        try {
            MimeMessage mimeMessage = createMessage(title, content);
            MailTask    mailTask    = new MailTask(mimeMessage);
            mailTask.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sendMailWithFile(String title, String content, String filePath) {
        try {
            MimeMessage mimeMessage = createMessage(title, content);
            appendFile(mimeMessage, filePath);
            MailTask mailTask = new MailTask(mimeMessage);
            mailTask.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sendMailWithMultiFile(String title, String content, List<String> pathList) {
        try {
            MimeMessage mimeMessage = createMessage(title, content);
            appendMultiFile(mimeMessage, pathList);
            MailTask mailTask = new MailTask(mimeMessage);
            mailTask.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Authenticator getAuthenticator() throws Exception {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(paramsMap.get(SENDER_NAME), paramsMap.get(SENDER_PASS));
            }
        };
    }

    private MimeMessage createMessage(String title, String content) throws Exception {
        Properties properties = System.getProperties();
        properties.put(KEY_MAIL_HOST, VALUE_MAIL_HOST);
        properties.put(KEY_MAIL_AUTH, VALUE_MAIL_AUTH);
        Session     session     = Session.getInstance(properties, getAuthenticator());
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(paramsMap.get(SENDER_NAME)));
        List<InternetAddress> addresses = new ArrayList<>();
        for (String receiver : recevierNames) addresses.add(new InternetAddress(receiver));
        mimeMessage.setRecipients(Message.RecipientType.TO, addresses.toArray(new InternetAddress[addresses.size()]));
        mimeMessage.setSubject(title);
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(content, "text/html;charset=utf-8");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        mimeMessage.setContent(multipart);
        mimeMessage.setSentDate(new Date());
        return mimeMessage;
    }

    private void appendFile(MimeMessage message, String filePath) throws Exception {
        Multipart    multipart = (Multipart) message.getContent();
        MimeBodyPart filePart  = new MimeBodyPart();
        filePart.attachFile(filePath);
        multipart.addBodyPart(filePart);
    }

    private void appendMultiFile(MimeMessage message, List<String> pathList) throws Exception {
        Multipart multipart = (Multipart) message.getContent();
        for (String path : pathList) {
            MimeBodyPart filePart = new MimeBodyPart();
            filePart.attachFile(path);
            multipart.addBodyPart(filePart);
        }
    }

    private class MailTask extends AsyncTask<Void, Void, Boolean> {

        private MimeMessage mimeMessage;

        MailTask(MimeMessage mimeMessage) {
            this.mimeMessage = mimeMessage;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Transport.send(mimeMessage);
                return Boolean.TRUE;
            } catch (MessagingException e) {
                e.printStackTrace();
                return Boolean.FALSE;
            }
        }
    }
}
