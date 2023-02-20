package com.ds.demo.service.impl;

import com.ds.demo.dto.TransferDto;
import com.ds.demo.entity.Account;
import com.ds.demo.entity.User;
import com.ds.demo.exception.NotFoundException;
import com.ds.demo.exception.TransferException;
import com.ds.demo.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class AccountServiceImplTest {
    final Long senderId = 1L;

    @Mock
    private AccountRepository repository;

    @Spy
    @InjectMocks
    private AccountServiceImpl accountService;

    @MockBean
    private Authentication auth;

    @BeforeEach
    public void initSecurityContext() {
        Mockito.when(auth.getPrincipal()).thenReturn(senderId);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void transferSuccess() {
        final Long recipientId = 2L;
        BigDecimal transferValue = BigDecimal.valueOf(100);

        BigDecimal senderBalance = BigDecimal.valueOf(100);
        User sender = new User(senderId, "first", LocalDate.now(), "somepass", null, null, null);
        Account senderAccount = new Account(senderId, sender, senderBalance);
        sender.setAccount(senderAccount);

        BigDecimal recipientBalance = BigDecimal.valueOf(100);
        User recipient = new User(senderId, "second", LocalDate.now(), "somepass", null, null, null);
        Account recipientAccount = new Account(recipientId, recipient, recipientBalance);
        recipient.setAccount(recipientAccount);

        Optional<Account> senderOptional = Optional.of(senderAccount);
        Optional<Account> recepientOptional = Optional.of(recipientAccount);

        Mockito.when(repository.findByUserId(senderId))
                .thenReturn(senderOptional);
        Mockito.when(repository.findByUserId(recipientId))
                .thenReturn(recepientOptional);

        TransferDto dto = new TransferDto(recipientId, transferValue);
        accountService.transfer(dto);
    }

    @Test
    void transfer_TransferException_GreatZero() {
        final Long recipientId = 2L;
        BigDecimal transferValue = BigDecimal.valueOf(-100);

        BigDecimal senderBalance = BigDecimal.valueOf(100);
        User sender = new User(senderId, "first", LocalDate.now(), "somepass", null, null, null);
        Account senderAccount = new Account(senderId, sender, senderBalance);
        sender.setAccount(senderAccount);

        BigDecimal recipientBalance = BigDecimal.valueOf(100);
        User recipient = new User(senderId, "second", LocalDate.now(), "somepass", null, null, null);
        Account recipientAccount = new Account(recipientId, recipient, recipientBalance);
        recipient.setAccount(recipientAccount);

        Optional<Account> senderOptional = Optional.of(senderAccount);
        Optional<Account> recepientOptional = Optional.of(recipientAccount);

        Mockito.when(repository.findByUserId(senderId))
                .thenReturn(senderOptional);
        Mockito.when(repository.findByUserId(recipientId))
                .thenReturn(recepientOptional);

        String expectedMessage = "Сумма для перевода должна быть больше 0";

        TransferDto dto = new TransferDto(recipientId, transferValue);
        TransferException exception = assertThrows(TransferException.class, () -> accountService.transfer(dto));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void transfer_TransferException_RecipientNotFound() {
        final Long recipientId = 2L;
        final Long notExistRecipientId = 999L;
        BigDecimal transferValue = BigDecimal.valueOf(100);

        BigDecimal senderBalance = BigDecimal.valueOf(100);
        User sender = new User(senderId, "first", LocalDate.now(), "somepass", null, null, null);
        Account senderAccount = new Account(senderId, sender, senderBalance);
        sender.setAccount(senderAccount);

        BigDecimal recipientBalance = BigDecimal.valueOf(100);
        User recipient = new User(senderId, "second", LocalDate.now(), "somepass", null, null, null);
        Account recipientAccount = new Account(recipientId, recipient, recipientBalance);
        recipient.setAccount(recipientAccount);

        Optional<Account> senderOptional = Optional.of(senderAccount);
        Optional<Account> recepientOptional = Optional.of(recipientAccount);

        Mockito.when(repository.findByUserId(senderId))
                .thenReturn(senderOptional);
        Mockito.when(repository.findByUserId(recipientId))
                .thenReturn(recepientOptional);

        String expectedMessage = "Указан несуществующий id получателя перевода";

        TransferDto dto = new TransferDto(notExistRecipientId, transferValue);
        NotFoundException exception = assertThrows(NotFoundException.class, () -> accountService.transfer(dto));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void transfer_TransferException_NegativeBalance() {
        final Long recipientId = 2L;
        BigDecimal transferValue = BigDecimal.valueOf(1000);

        BigDecimal senderBalance = BigDecimal.valueOf(100);
        User sender = new User(senderId, "first", LocalDate.now(), "somepass", null, null, null);
        Account senderAccount = new Account(senderId, sender, senderBalance);
        sender.setAccount(senderAccount);

        BigDecimal recipientBalance = BigDecimal.valueOf(100);
        User recipient = new User(senderId, "second", LocalDate.now(), "somepass", null, null, null);
        Account recipientAccount = new Account(recipientId, recipient, recipientBalance);
        recipient.setAccount(recipientAccount);

        Optional<Account> senderOptional = Optional.of(senderAccount);
        Optional<Account> recepientOptional = Optional.of(recipientAccount);

        Mockito.when(repository.findByUserId(senderId))
                .thenReturn(senderOptional);
        Mockito.when(repository.findByUserId(recipientId))
                .thenReturn(recepientOptional);

        String expectedMessage = "Сумма перевода превышает остаток средств на балансе.";

        TransferDto dto = new TransferDto(recipientId, transferValue);
        TransferException exception = assertThrows(TransferException.class, () -> accountService.transfer(dto));
        assertEquals(expectedMessage, exception.getMessage());
    }
}