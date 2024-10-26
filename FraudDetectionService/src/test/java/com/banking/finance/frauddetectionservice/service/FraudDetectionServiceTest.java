package com.banking.finance.frauddetectionservice.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.banking.finance.frauddetectionservice.entity.Transaction;
import com.banking.finance.frauddetectionservice.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.math.BigDecimal;

public class FraudDetectionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private MessageChannel output;

    @InjectMocks
    private FraudDetectionService fraudDetectionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAnalyzeTransaction_NonSuspicious() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setSourceAccount("123456");
        transaction.setDestinationAccount("654321");
        transaction.setAmount(new BigDecimal("5000"));

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        assertFalse(fraudDetectionService.analyzeTransaction(transaction));
        verify(transactionRepository).save(transaction);
        verifyNoInteractions(output);
    }

    @Test
    public void testAnalyzeTransaction_Suspicious() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setSourceAccount("123456");
        transaction.setDestinationAccount("654321");
        transaction.setAmount(new BigDecimal("15000"));

        assertTrue(fraudDetectionService.analyzeTransaction(transaction));
        verify(output).send(any());
    }

    @Test
    public void testAnalyzeTransaction_MissingSourceAccountDetails() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setSourceAccount("");
        transaction.setDestinationAccount("654321");
        transaction.setAmount(new BigDecimal("5000"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fraudDetectionService.analyzeTransaction(transaction);
        });

        assertEquals("Source/Destination account is empty", exception.getMessage());
        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(output);
    }

    @Test
    public void testAnalyzeTransaction_MissingDestinationAccountDetails() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setSourceAccount("123456");
        transaction.setDestinationAccount("");
        transaction.setAmount(new BigDecimal("5000"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            fraudDetectionService.analyzeTransaction(transaction);
        });

        assertEquals("Source/Destination account is empty", exception.getMessage());
        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(output);
    }
}
