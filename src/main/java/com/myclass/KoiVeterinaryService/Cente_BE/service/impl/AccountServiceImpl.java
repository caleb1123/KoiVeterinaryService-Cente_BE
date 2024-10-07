package com.myclass.KoiVeterinaryService.Cente_BE.service.impl;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.Account;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.ERole;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.AppException;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.ErrorCode;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.AccountDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.CreateAccountRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.UpdateAccountRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.AvailableVeterinariansResponse;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.AccountRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.RoleRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.service.AccountService;
import jakarta.persistence.Access;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public List<AccountDTO> findAll() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public AccountDTO createAccount(CreateAccountRequest accountRequest) {
        Account existingAccount = accountRepository.existsAccountByUserName(accountRequest.getUserName());
        if (existingAccount != null) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        Account newAccount = modelMapper.map(accountRequest, Account.class);

        newAccount.setActive(true);

        // Mã hóa mật khẩu
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String encodedPassword = passwordEncoder.encode("12345678");
        newAccount.setPassword(encodedPassword);

        // Thiết lập vai trò cho tài khoản
        newAccount.setRole(roleRepository.findRoleByRoleId(accountRequest.getRodeId()));
        if (newAccount.getRole() == null) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }

        // Lưu tài khoản mới vào cơ sở dữ liệu
        accountRepository.save(newAccount);

        // Trả về DTO của tài khoản mới
        return modelMapper.map(newAccount, AccountDTO.class);
    }


    @Override
    public AccountDTO updateAccount(UpdateAccountRequest account, String userName) {
        Account existAccount = accountRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        existAccount.setDob(account.getDob());
        existAccount.setEmail(account.getEmail());
        existAccount.setFullName(account.getFullName());
        existAccount.setPhone(account.getPhone());
        existAccount.setAddress(account.getAddress());
        existAccount.setActive(account.isActive());
        accountRepository.save(existAccount);
        return modelMapper.map(existAccount, AccountDTO.class);
    }

    @Override
    public boolean deleteAccount(String userName) {
        Account account = accountRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if(account != null){
            accountRepository.delete(account);
            return true;
        }

        return false;
    }

    @Override
    public AccountDTO findByUserName(String userName) {
        Account account = accountRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if(account != null){
            return modelMapper.map(account, AccountDTO.class);
        }
        return null;
    }

    @Override
    public List<AvailableVeterinariansResponse> findAvailableVeterinarians(LocalDate specificDate, Integer shiftId) {
        List<Object[]> veterinarians = accountRepository.findAvailableVeterinarians(specificDate, shiftId);

        return veterinarians.stream()
                .map(veterinarian -> new AvailableVeterinariansResponse(
                        (int) veterinarian[0], // accountId
                        (String) veterinarian[1], // fullName
                        (String) veterinarian[2], // phone
                        (String) veterinarian[3]  // email
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountDTO> findAccountByRole(String roleName) {
        ERole roleEnum = ERole.valueOf(roleName);
        List<Account> accounts = accountRepository.findAccountByRole_RoleName(roleEnum);
        return accounts.stream()
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountDTO> findAccountByActive(boolean active) {
        List<Account> accounts = accountRepository.findAccountByActive(active);
        return accounts.stream()
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .collect(Collectors.toList());
    }


}
