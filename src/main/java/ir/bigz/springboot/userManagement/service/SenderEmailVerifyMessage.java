package ir.bigz.springboot.userManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component("SenderEmailMessage")
public class SenderEmailVerifyMessage implements SenderMessage{

    @Autowired
    private JavaMailSender emailSender;

    @Value( "${app.forgotPassword.url}" )
    private String forgotPasswordUrl;

    @Override
    public void sendMessageTo(String to, String subject, String message) {
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(to);
        emailMessage.setSubject(subject);
        String messageBuild = buildMessageForVerify(to, message);
        emailMessage.setText(messageBuild);
        emailSender.send(emailMessage);
    }

    private String buildMessageForVerify(String email, String message){
        return forgotPasswordUrl + "/" + email + "/" + message;
    }
}
