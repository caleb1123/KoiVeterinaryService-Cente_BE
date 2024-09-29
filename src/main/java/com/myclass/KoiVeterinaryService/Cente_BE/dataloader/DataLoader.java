package com.myclass.KoiVeterinaryService.Cente_BE.dataloader;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.ERole;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.Role;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.Shift;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.ShiftName;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.RoleRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        loadShifts();
        loadRoles();
    }

    private void loadShifts() {
        // Check if shifts already exist
        if (shiftRepository.count() == 0) {
            // Create predefined shifts
            List<Shift> shifts = List.of(
                    Shift.builder().shiftName(ShiftName.MORNING).build(),
                    Shift.builder().shiftName(ShiftName.AFTERNOON).build(),
                    Shift.builder().shiftName(ShiftName.EVENING).build(),
                    Shift.builder().shiftName(ShiftName.NIGHT).build()
            );

            // Save shifts to the database
            shiftRepository.saveAll(shifts);
            System.out.println("Predefined shifts have been added to the database.");
        } else {
            System.out.println("Shifts already exist in the database. No data loaded.");
        }
    }

    private void loadRoles() {
        // Check if roles already exist
        if (roleRepository.count() == 0) {
            // Create predefined roles
            List<Role> roles = List.of(
                    Role.builder().roleName(ERole.CUSTOMER).build(),
                    Role.builder().roleName(ERole.STAFF).build(),
                    Role.builder().roleName(ERole.VETERINARIAN).build(),
                    Role.builder().roleName(ERole.MANAGER).build()
            );

            // Save roles to the database
            roleRepository.saveAll(roles);
            System.out.println("Predefined roles have been added to the database.");
        } else {
            System.out.println("Roles already exist in the database. No data loaded.");
        }
    }
}
