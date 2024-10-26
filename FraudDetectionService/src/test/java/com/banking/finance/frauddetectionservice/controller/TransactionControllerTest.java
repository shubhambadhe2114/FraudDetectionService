package com.banking.finance.frauddetectionservice.controller;

import com.banking.finance.frauddetectionservice.datasource.DataSources;
import com.banking.finance.frauddetectionservice.entity.Transaction;
import com.banking.finance.frauddetectionservice.service.FraudDetectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class TransactionControllerTest {

    @Mock
    private FraudDetectionService fraudService;

    @Mock
    private DataSources dataSources;

    @InjectMocks
    private TransactionController controller;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transaction = new Transaction();
        transaction.setId(1L);
        // Set other necessary fields
    }

    @Test
    void testProcessTransaction_Success() throws Exception {
        when(fraudService.analyzeTransaction(any(Transaction.class))).thenReturn(false);

        ResponseEntity<String> response = controller.processTransaction(transaction);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Transaction processed successfully", response.getBody());
        verify(fraudService).analyzeTransaction(transaction);
    }


    @Test
    void testProcessTransaction_Fraudulent() throws Exception {
        when(fraudService.analyzeTransaction(any(Transaction.class))).thenReturn(true);

        ResponseEntity<String> response = controller.processTransaction(transaction);

        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Transaction flagged as fraudulent", response.getBody());
        verify(fraudService).analyzeTransaction(transaction);
    }

    @Test
    void testProcessTransaction_Exception() throws Exception {
        doThrow(new IllegalArgumentException("Illegal argument")).when(fraudService).analyzeTransaction(any(Transaction.class));

        ResponseEntity<String> response = controller.processTransaction(transaction);

        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Illegal argument"));
        verify(fraudService).analyzeTransaction(transaction);
    }

    @Test
    public void testGetTransactions_Success() throws Exception {
        Map<String, List<Transaction>> transactions = new HashMap<>();
        transactions.put("1", Arrays.asList(new Transaction()));

        when(dataSources.retrieveTransactionData(1L)).thenReturn(transactions);

        ResponseEntity<String> response = controller.getTransactions(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(dataSources).retrieveTransactionData(1L);
    }

    @Test
    public void testGetTransactions_Exception() throws Exception {
        when(dataSources.retrieveTransactionData(1L)).thenThrow(new RuntimeException("Data retrieval failure"));

        ResponseEntity<String> response = controller.getTransactions(1L);

        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Data retrieval failure"));
        verify(dataSources).retrieveTransactionData(1L);
    }

}
