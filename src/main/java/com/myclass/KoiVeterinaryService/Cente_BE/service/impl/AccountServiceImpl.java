package com.myclass.KoiVeterinaryService.Cente_BE.service.impl;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.Account;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.AppException;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.ErrorCode;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.CreateAccountRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.AccountRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.RoleRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.service.AccountService;
import jakarta.persistence.Access;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public List<Account> findAll() {
        List<Account> accounts = accountRepository.findAll();
        return accounts;
    }

    @Override
    public Account createAccount(CreateAccountRequest account) {

        Account account1 = accountRepository.existsAccountByUserName(account.getUserName());
        if(account1 != null){
            return null;
        }
        Account account2 = new Account();
        modelMapper.map(account, account2);
        account2.setActive(true);
        account2.setRole(roleRepository.findByRoleId(account.getRodeId()));
        accountRepository.save(account2);
        return account2;
    }


}
