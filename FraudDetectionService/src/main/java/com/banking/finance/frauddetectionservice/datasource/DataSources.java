package com.banking.finance.frauddetectionservice.datasource;

import com.banking.finance.frauddetectionservice.entity.Transaction;
import com.banking.finance.frauddetectionservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataSources {

    @Autowired
    private TransactionRepository transactionRepository;

    public Map<String, List<Transaction>> retrieveTransactionData(Long id) {
        // Connect to the database and retrieve historical transaction data
        List<Transaction> transactions = transactionRepository.findByIdIn(Collections.singletonList(id));

        Map<String, List<Transaction>> transactionData = new HashMap<>();
        transactionData.put(id.toString(), transactions);
        return transactionData;
    }
}
