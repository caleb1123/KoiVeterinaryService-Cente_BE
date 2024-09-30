package com.myclass.KoiVeterinaryService.Cente_BE.repository;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.Account;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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

    @Query(value = """
            WITH BookedVeterinarians AS (
                SELECT DISTINCT veterinarian_id
                FROM service_request
                WHERE 
                    CAST(appointment_time AS DATE) = :specificDate AND
                    shift_id = :shiftId
            )
            SELECT a.account_id, a.full_name, a.phone, a.email
            FROM account a
            WHERE 
                a.role_id = 3
                AND a.account_id NOT IN (SELECT veterinarian_id FROM BookedVeterinarians)
            """, nativeQuery = true)
    List<Object[]> findAvailableVeterinarians(LocalDate specificDate, Integer shiftId);

    List<Account> findAccountByRole_RoleName(ERole roleName);
    List<Account> findAccountByActive(boolean active);
}
