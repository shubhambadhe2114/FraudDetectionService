package com.banking.finance.frauddetectionservice.controller;

import com.banking.finance.frauddetectionservice.datasource.DataSources;
import com.banking.finance.frauddetectionservice.entity.Transaction;
import com.banking.finance.frauddetectionservice.service.FraudDetectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class TransactionController {
    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);
    @Autowired
    private FraudDetectionService fraudService;
    @Autowired
    private DataSources dataSources;

    @PostMapping("/transactions")
    public ResponseEntity<String> processTransaction(@RequestBody Transaction transaction) {
        log.info("Processing transaction {}", transaction);
        try {
            boolean isFraudulent = fraudService.analyzeTransaction(transaction);
            log.info("Transaction id {} is Fraudulent {}", transaction.getId(), isFraudulent);
            if (isFraudulent) {
                log.error("Fraudulent detected for transaction {}", transaction.getId());
                return ResponseEntity.status(403).body("Transaction flagged as fraudulent");
            }
            return ResponseEntity.ok("Transaction processed successfully");
        } catch (Exception e) {
            log.error("Error processing transaction {}", transaction, e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/transactions")
    public ResponseEntity<String> getTransactions(@RequestParam Long id) {
        log.info("Getting transactions with id {}", id);
        try {
            Map<String, List<Transaction>> transaction = dataSources.retrieveTransactionData(id);
            return ResponseEntity.ok(transaction.get(id.toString()).toString());
        } catch (Exception e) {
            log.error("Error getting transaction. ", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
