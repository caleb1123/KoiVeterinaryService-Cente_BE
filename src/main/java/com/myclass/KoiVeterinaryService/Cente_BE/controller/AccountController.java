package com.myclass.KoiVeterinaryService.Cente_BE.controller;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.Account;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.AccountDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.CreateAccountRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@Slf4j
@CrossOrigin(origins = "http://localhost:3001")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        try {
            List<Account> accounts = accountService.findAll();
            return ResponseEntity.ok(accounts);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(null);
        }
    }
    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountRequest accountRequest) {
        Account account = accountService.createAccount(accountRequest);

        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @PutMapping("/update")
    public ResponseEntity<Account> updateAccount(@RequestBody AccountDTO account, @RequestParam String userName) {
        Account updatedAccount = accountService.updateAccount(account, userName);

        if (updatedAccount == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAccount(@RequestParam String userName) {
        boolean result = accountService.deleteAccount(userName);

        if (!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Delete fail");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Delete success");
    }

    @GetMapping("/find")
    public ResponseEntity<Account> findByUserName(@RequestParam String userName) {
        Account account = accountService.findByUserName(userName);

        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(account);
    }
}
