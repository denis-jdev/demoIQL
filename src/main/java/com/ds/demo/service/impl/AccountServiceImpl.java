package com.ds.demo.service.impl;

import com.ds.demo.dto.TransferDto;
import com.ds.demo.entity.Account;
import com.ds.demo.exception.NotFoundException;
import com.ds.demo.exception.TransferException;
import com.ds.demo.repository.AccountRepository;
import com.ds.demo.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Transactional
    @Override
    synchronized public void transfer(TransferDto dto) {
        Long senderId = getUserIdByContext();
        Optional<Account> senderOptional = accountRepository.findByUserId(senderId);
        Optional<Account> recipientOptional = accountRepository.findByUserId(dto.getRecipientId());

        Account sender = senderOptional.get();
        canTransfer(sender, recipientOptional, dto);
        Account recipient = recipientOptional.get();

        BigDecimal debit = sender.getBalance().subtract(dto.getValue());
        BigDecimal credit = recipient.getBalance().add(dto.getValue());

        sender.setBalance(debit);
        recipient.setBalance(credit);

        accountRepository.save(sender);
        accountRepository.save(recipient);
    }

    private void canTransfer(Account sender, Optional<Account> recipient, TransferDto dto) {

        if (dto.getValue().compareTo(BigDecimal.ZERO) <= 0) {
            log.error(String.format(
                    "Ошибка при переводе средств пользователя с id '%s': Сумма для перевода должна быть больше 0",
                    sender.getId()
            ));
            throw new TransferException("Сумма для перевода должна быть больше 0");
        }
        if (recipient.isEmpty()) {
            log.error(String.format(
                    "Ошибка при переводе средств пользователя с id '%s': Указан несуществующий id получателя '%s'",
                    sender.getId(), dto.getRecipientId()
            ));
            throw new NotFoundException("Указан несуществующий id получателя перевода");
        }
        if (sender.getBalance().compareTo(dto.getValue()) < 0) {
            log.error(String.format(
                    "Ошибка при переводе средств пользователя с id '%s': Сумма перевода превышает остаток средств на балансе.",
                    sender.getId()
            ));
            throw new TransferException("Сумма перевода превышает остаток средств на балансе.");
        }
    }

    private final Map<Long, BigDecimal> initialValueMap = new HashMap<>();
    private Long lastCachedId = 0L;
    private final BigDecimal increasePercent = BigDecimal.valueOf(1.1);
    private final BigDecimal redLinePercent = BigDecimal.valueOf(2.07);

    @Scheduled(initialDelay = 30000, fixedDelay = 30000)
    void increaseBalance() {
        List<Account> storedAccounts = accountRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        List<Account> increasedAccountList = storedAccounts.stream()
                .peek(account -> {
                    if (account.getId() > lastCachedId) {
                        initialValueMap.put(account.getId(), account.getBalance());
                        lastCachedId = account.getId();
                    }
                    BigDecimal initialBalance = initialValueMap.get(account.getId());
                    BigDecimal redLineBalance = initialBalance.multiply(redLinePercent);
                    BigDecimal increasedValue = account.getBalance().multiply(increasePercent);

                    if (increasedValue.compareTo(redLineBalance) <= 0) {
                        account.setBalance(increasedValue);
                    }
                })
                .collect(Collectors.toList());

        accountRepository.saveAll(increasedAccountList);

    }

    private Long getUserIdByContext() {
        return (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
