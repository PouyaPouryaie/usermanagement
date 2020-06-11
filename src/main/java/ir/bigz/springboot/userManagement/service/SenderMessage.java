package ir.bigz.springboot.userManagement.service;

import org.springframework.stereotype.Service;

@Service
public interface SenderMessage {

    void sendMessageTo(String to, String subject, String message);
}
