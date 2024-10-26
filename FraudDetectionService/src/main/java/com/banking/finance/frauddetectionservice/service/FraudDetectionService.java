package com.banking.finance.frauddetectionservice.service;

import com.banking.finance.frauddetectionservice.entity.Transaction;
import com.banking.finance.frauddetectionservice.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.concurrent.Executor;

@Service
public class FraudDetectionService {

    private static final Logger log = LoggerFactory.getLogger(FraudDetectionService.class);
    @Autowired
    private TransactionRepository transactionRepository;

    @Qualifier("errorChannel")
    @Autowired
    private MessageChannel output;

    @Qualifier("taskExecutor")
    @Autowired
    private Executor taskExecutor;

    @Cacheable(value = "transactionRulesCache", key = "#transaction.id")
    @Async
    public boolean analyzeTransaction(Transaction transaction) throws IllegalArgumentException {
        log.info("Analysing transaction");
        if(!StringUtils.hasText(transaction.getSourceAccount()) || !StringUtils.hasText(transaction.getDestinationAccount())) {
            log.error("Source/Destination account is empty");
            throw new IllegalArgumentException("Source/Destination account is empty");
        }
        // Simple rule: Any transaction over $10,000 is considered suspicious
        if(transaction.getAmount().compareTo(new BigDecimal("10000")) <= 0) {
            log.info("Transaction is genuine");
            transaction.setSuspicious(false);
            transactionRepository.save(transaction);
            return false;
        }
        log.info("Transaction is suspicious");
        emitEvent(transaction);
        return true;
    }

    private void emitEvent(Transaction transaction) {
        output.send(MessageBuilder.withPayload(transaction).build());
    }
}

