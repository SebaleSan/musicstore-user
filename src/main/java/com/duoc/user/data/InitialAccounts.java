package com.duoc.user.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.duoc.user.dto.UserCreateRequest;
import com.duoc.user.model.Role;
import com.duoc.user.repository.UserRepository;
import com.duoc.user.service.UserService;

@Component
public class InitialAccounts implements CommandLineRunner {

    private static final Logger log =
            LoggerFactory.getLogger(InitialAccounts.class);

    private final UserRepository userRepository;
    private final UserService userService;

    private final String adminPassword;
    private final String userOnePassword;
    private final String userTwoPassword;

    public InitialAccounts(
            UserRepository userRepository,
            UserService userService,
            @Value("${musicstore.seed.admin-password:}") String adminPassword,
            @Value("${musicstore.seed.user-one-password:}") String userOnePassword,
            @Value("${musicstore.seed.user-two-password:}") String userTwoPassword) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.adminPassword = adminPassword;
        this.userOnePassword = userOnePassword;
        this.userTwoPassword = userTwoPassword;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Accounts already exist. Initial seeding skipped.");
            return;
        }

        if (adminPassword.length() < 8
                || userOnePassword.length() < 8
                || userTwoPassword.length() < 8) {

            log.warn(
                "Initial accounts were not created. "
                + "Required seed passwords are missing or shorter than 8 characters."
            );
            return;
        }

        userService.createUser(new UserCreateRequest(
                "admin",
                "admin@musicstore.test",
                adminPassword,
                Role.ADMIN
        ));

        userService.createUser(new UserCreateRequest(
                "user1",
                "user1@musicstore.test",
                userOnePassword,
                Role.USER
        ));

        userService.createUser(new UserCreateRequest(
                "user2",
                "user2@musicstore.test",
                userTwoPassword,
                Role.USER
        ));

        log.info("Three initial accounts were created with encrypted passwords.");
    }
}