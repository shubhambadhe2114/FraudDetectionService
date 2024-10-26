package com.banking.finance.frauddetectionservice.datasource;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.banking.finance.frauddetectionservice.entity.Transaction;
import com.banking.finance.frauddetectionservice.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DataSourcesTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private DataSources dataSources;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRetrieveTransactionData_ValidId() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        List<Transaction> transactions = Arrays.asList(transaction);

        when(transactionRepository.findByIdIn(Collections.singletonList(1L))).thenReturn(transactions);

        Map<String, List<Transaction>> result = dataSources.retrieveTransactionData(1L);

        assertNotNull(result);
        assertTrue(result.containsKey("1"));
        assertEquals(1, result.get("1").size());
        assertEquals(transaction, result.get("1").get(0));
        verify(transactionRepository).findByIdIn(Collections.singletonList(1L));
    }

    @Test
    public void testRetrieveTransactionData_NoRecords() {
        when(transactionRepository.findByIdIn(Collections.singletonList(1L))).thenReturn(Collections.emptyList());

        Map<String, List<Transaction>> result = dataSources.retrieveTransactionData(1L);

        assertNotNull(result);
        assertTrue(result.containsKey("1"));
        assertTrue(result.get("1").isEmpty());
        verify(transactionRepository).findByIdIn(Collections.singletonList(1L));
    }

}
