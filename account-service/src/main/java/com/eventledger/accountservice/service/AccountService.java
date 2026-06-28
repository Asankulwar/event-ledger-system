package com.eventledger.accountservice.service;

import com.eventledger.accountservice.model.Account;
import com.eventledger.accountservice.model.Transaction;
import com.eventledger.accountservice.repository.AccountRepository;
import com.eventledger.accountservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Transaction applyTransaction(String accountId, Transaction tx) {
        Account account = accountRepository.findById(accountId)
                .orElseGet(() -> {
                    Account newAcc = new Account();
                    newAcc.setAccountId(accountId);
                    return accountRepository.save(newAcc);
                });

        if ("CREDIT".equalsIgnoreCase(tx.getType())) {
            account.setBalance(account.getBalance().add(tx.getAmount()));
        } else if ("DEBIT".equalsIgnoreCase(tx.getType())) {
            account.setBalance(account.getBalance().subtract(tx.getAmount()));
        }

        accountRepository.save(account);
        tx.setAccountId(accountId);
        return transactionRepository.save(tx);
    }
}
