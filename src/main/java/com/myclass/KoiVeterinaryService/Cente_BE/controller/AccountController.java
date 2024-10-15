package com.myclass.KoiVeterinaryService.Cente_BE.controller;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.Account;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.AccountDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.CreateAccountRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.UpdateAccountRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.AvailableVeterinariansResponse;
import com.myclass.KoiVeterinaryService.Cente_BE.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/account")
@Slf4j
@CrossOrigin(origins = "http://localhost:5153")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        try {
            List<AccountDTO> accounts = accountService.findAll();
            return ResponseEntity.ok(accounts);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(null);
        }
    }
    @PostMapping("/create")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody CreateAccountRequest accountRequest) {
        AccountDTO account = accountService.createAccount(accountRequest);

        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @PutMapping("/update")
    public ResponseEntity<AccountDTO> updateAccount(@RequestBody UpdateAccountRequest account, @RequestParam String userName) {
        AccountDTO updatedAccount = accountService.updateAccount(account, userName);

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

    @GetMapping("/findbyusername")
    public ResponseEntity<AccountDTO> findByUserName(@RequestParam String userName) {
        AccountDTO account = accountService.findByUserName(userName);

        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    @GetMapping("/available")
    public List<AvailableVeterinariansResponse> getAvailableVeterinarians(@RequestParam String date, @RequestParam Integer shiftId) {
        LocalDate specificDate = LocalDate.parse(date);
        return accountService.findAvailableVeterinarians(specificDate, shiftId);
    }

    @GetMapping("/role")
    public List<AccountDTO> getAccountByRole(@RequestParam String roleName) {
        return accountService.findAccountByRole(roleName);
    }

    @GetMapping("/active")
    public List<AccountDTO> getAccountByActive(@RequestParam boolean active) {
        return accountService.findAccountByActive(active);
    }
}
