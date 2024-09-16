package com.myclass.KoiVeterinaryService.Cente_BE.service;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.Account;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.CreateAccountRequest;

import java.util.List;

public interface AccountService {
    List<Account> findAll();
    Account createAccount(CreateAccountRequest account);

}
