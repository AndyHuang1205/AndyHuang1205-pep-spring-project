package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository; 
    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    public Account register(Account account) throws Exception{
        Optional<Account> acc = accountRepository.findByUsername(account.getUsername());
        if(acc.isPresent()) throw new IllegalArgumentException("Account with username exists");
        if(account.getPassword().length() < 4 || account.getUsername().isBlank()) 
            throw new IllegalArgumentException("Password must be at least 4 characters long and Username must not be empty");
        return accountRepository.save(account);
    }

    public Account verify(Account account){
        Optional<Account> acc = accountRepository.findByUsername(account.getUsername());
        if(acc.isPresent() && acc.get().getPassword().equals(account.getPassword())){
            return acc.get();
        }
        return null;
    }
}
