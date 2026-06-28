package com.eventledger.accountservice.controller;

import com.eventledger.accountservice.model.Account;
import com.eventledger.accountservice.model.Transaction;
import com.eventledger.accountservice.repository.AccountRepository;
import com.eventledger.accountservice.repository.TransactionRepository;
import com.eventledger.accountservice.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountController(AccountService accountService,
                             AccountRepository accountRepository,
                             TransactionRepository transactionRepository) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @PostMapping("/{accountId}/transactions")
    public ResponseEntity<Transaction> applyTransaction(@PathVariable String accountId,
                                                        @RequestBody Transaction tx) {
        Transaction saved = accountService.applyTransaction(accountId, tx);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<Account> getBalance(@PathVariable String accountId) {
        return accountRepository.findById(accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountId) {
        return accountRepository.findById(accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Account Service is UP");
    }
}
