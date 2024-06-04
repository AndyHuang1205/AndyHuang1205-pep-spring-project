package com.example.controller;

import java.util.List;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

// import net.bytebuddy.asm.Advice.Return;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@Controller
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;
    @Autowired
    public SocialMediaController(MessageService messageService, AccountService accountService){
        this.messageService = messageService;
        this.accountService = accountService;
    }
    @PostMapping(path = "/register")
    public ResponseEntity<Object> registerAccount(@RequestBody Account account) throws Exception{
        try{
            Account regAccount = accountService.register(account);
            return ResponseEntity.ok(regAccount);
        }catch(IllegalArgumentException e){
            if (e.getMessage().contains("Account with username exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping(path = "/login")
    public ResponseEntity<Object> login(@RequestBody Account account){
            Account verifiedAccount = accountService.verify(account);
            if(verifiedAccount != null) {
                return ResponseEntity.ok(verifiedAccount);
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
    }

    @PostMapping(path = "/messages")
    public ResponseEntity<Object> createMessage(@RequestBody Message message){
        Optional<Message> updatedMessage = messageService.createMessage(message);
        if(updatedMessage.isPresent()){
            return ResponseEntity.ok(updatedMessage.get());
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping(path = "/messages")
    public ResponseEntity<List<Message>> getMessages(){
        return ResponseEntity.ok(messageService.getMessages());
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Object> getMessageById(@PathVariable int messageId){
        return ResponseEntity.ok(messageService.getMessageById(messageId));
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Object> deleteByMessageId(@PathVariable int messageId){
        if(messageService.deleteByMessageId(messageId)) return ResponseEntity.ok("1");
        else return ResponseEntity.ok(null);
    }
    
    @PatchMapping("/messages/{messageId}")
    public @ResponseBody ResponseEntity<Object> updateMessage(@PathVariable int messageId, @RequestBody Message message){
        return messageService.updateMessage(messageId, message.getMessageText());

    }
    
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<Object> findAllMessagesByUser(@PathVariable int accountId){
        return ResponseEntity.ok(messageService.getAllByUser(accountId));
    }
}
