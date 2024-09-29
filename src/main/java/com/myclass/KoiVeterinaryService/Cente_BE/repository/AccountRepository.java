package com.myclass.KoiVeterinaryService.Cente_BE.repository;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUserName(String userName);
    Optional<Account> findByPhone(String phone);

    Optional<Account> findByEmail(String email);

    @Query(value = "select * from employee",nativeQuery = true)
    Optional<Account> findAll(int employeeId);

    @Query(value = "select * from account where user_name = :userName",nativeQuery = true)
    Account existsAccountByUserName(String userName);

    @Query(value = "SELECT * FROM account WHERE role_id = :roleId AND account_id = :accountId", nativeQuery = true)
    Optional<Account> findAccountByAccountIdAndRole(int roleId, int accountId);

}
