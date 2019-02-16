/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coor;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import javax.mail.*;
import java.util.Properties;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author TACTS
 */
class Hotmail extends Authenticator {

    public boolean envioDatos() throws MessagingException {

        String mailhost = "smtp.live.com";

        String user = "****@gmail.com";

        String password = "******";
        Properties props = new Properties();
        try {
            props.put("mail.store.protocol", "imaps");
            Session session = Session.getDefaultInstance(props);

            Store store = session.getStore("imaps");
            store.connect("smtp.gmail.com", user, password);

            Folder inbox = store.getFolder("pintar");
            inbox.open(Folder.READ_ONLY);
            int messageCount = inbox.getMessageCount();

            System.out.println("Total Messages:- " + messageCount);

            Message[] messages = inbox.getMessages();
            System.out.println("------------------------------");
            String salida = "";
            for (int i = 0; i < messages.length; i++) {
                System.out.println("Mail Subject:- " + messages[i].getSubject());
                salida += getTextFromMessage(messages[i]);
            }
            System.out.println(salida);
            inbox.close(true);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
            Multipart multiPart = (Multipart) message.getContent();
            for (int i = 0; i < multiPart.getCount(); i++) {
                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    String destFilePath = "/Users/TACTS/Documents/correos/" + part.getFileName();

                    FileOutputStream output = new FileOutputStream(destFilePath);

                    InputStream input = part.getInputStream();

                    byte[] buffer = new byte[4096];

                    int byteRead;

                    while ((byteRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, byteRead);
                    }
                    output.close();
                }
            }
        }
        return result;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {

                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }

}
