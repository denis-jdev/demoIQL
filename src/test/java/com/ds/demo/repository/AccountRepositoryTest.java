package com.ds.demo.repository;

import com.ds.demo.entity.Account;
import com.ds.demo.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class AccountRepositoryTest {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    void findByUserId() {
        User user = new User();
        user.setPassword("somepass");
        user.setName("somename");
        user.setDateOfBirth(LocalDate.now());
        User storedUser = userRepository.save(user);

        Account account = new Account();
        account.setBalance(BigDecimal.ONE);
        account.setUser(storedUser);
        Account storedAccount = accountRepository.save(account);

        Optional<Account> byUserId = accountRepository.findByUserId(user.getId());
        assertFalse(byUserId.isEmpty());

        assertEquals(byUserId.get().getId(), storedAccount.getId());
        assertEquals(byUserId.get().getUser(), storedAccount.getUser());
        assertEquals(byUserId.get().getBalance(), storedAccount.getBalance());
    }
}