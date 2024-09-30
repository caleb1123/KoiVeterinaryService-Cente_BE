package com.myclass.KoiVeterinaryService.Cente_BE.service;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.Account;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.ERole;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.AccountDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.CreateAccountRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.AvailableVeterinariansResponse;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface AccountService {
    List<Account> findAll();
    AccountDTO createAccount(CreateAccountRequest account);
    Account updateAccount(AccountDTO account, String userName);
    boolean deleteAccount(String userName);

    Account findByUserName(String userName);
    List<AvailableVeterinariansResponse> findAvailableVeterinarians(LocalDate specificDate, Integer shiftId);
    List<AccountDTO> findAccountByRole(String roleName);
    List<AccountDTO> findAccountByActive(boolean active);
}
