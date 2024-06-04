package com.example.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

// import javax.swing.text.html.Option;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;
    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }
    public Optional<Message> createMessage(Message message){
        if(message.getMessageText().isBlank() || message.getMessageText().length()>255 || accountRepository.findByAccountId(message.getPostedBy()).isEmpty()){
            return Optional.empty();
        }
        Message updatedMessage = messageRepository.save(message);
        return Optional.of(updatedMessage);
    }

    public List<Message> getMessages(){
        return messageRepository.findAll();
    }

    public Message getMessageById(int id){
        Optional<Message> message = messageRepository.findByMessageId(id);
        if(message.isPresent()) return message.get();
        return null;
    }

    public boolean deleteByMessageId(int id){
        try{
            messageRepository.deleteById(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public ResponseEntity<Object> updateMessage(int messageId, String messageText){
        Optional<Message> message = messageRepository.findByMessageId(messageId);
        if (!messageText.isBlank() && messageText != null && message.isPresent() && messageText.length() < 256){
            message.get().setMessageText(messageText);
            messageRepository.save(message.get());
            return ResponseEntity.ok("1");
        }
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); 
    }

    public List<Message> getAllByUser(int id){
        return messageRepository.findAllByPostedBy(id).get();
    }
}
