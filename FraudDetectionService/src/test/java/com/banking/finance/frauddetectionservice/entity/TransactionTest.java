package com.banking.finance.frauddetectionservice.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionTest {

    @Test
    public void testTransactionId() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        assertEquals(1L, transaction.getId(), "The transaction ID should match the setter value");
    }

    @Test
    public void testSourceAccount() {
        Transaction transaction = new Transaction();
        transaction.setSourceAccount("123456789");
        assertEquals("123456789", transaction.getSourceAccount(), "The source account should match the setter value");
    }

    @Test
    public void testDestinationAccount() {
        Transaction transaction = new Transaction();
        transaction.setDestinationAccount("987654321");
        assertEquals("987654321", transaction.getDestinationAccount(), "The destination account should match the setter value");
    }

    @Test
    public void testAmount() {
        Transaction transaction = new Transaction();
        BigDecimal amount = new BigDecimal("1500.00");
        transaction.setAmount(amount);
        assertEquals(0, amount.compareTo(transaction.getAmount()), "The amount should match the setter value");
    }

    @Test
    public void testTimestamp() {
        Transaction transaction = new Transaction();
        LocalDateTime now = LocalDateTime.now();
        transaction.setTimestamp(now);
        assertEquals(now, transaction.getTimestamp(), "The timestamp should match the setter value");
    }

    @Test
    public void testSuspiciousFlag() {
        Transaction transaction = new Transaction();
        transaction.setSuspicious(true);
        assertEquals(true, transaction.isSuspicious(), "The suspicious flag should be true as set");
    }
}
