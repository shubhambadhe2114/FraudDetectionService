package com.banking.finance.frauddetectionservice.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ThreadPoolConfig.class)
public class ThreadPoolConfigTest {

    @Qualifier("taskExecutor")
    @Autowired
    private Executor executor;

    @Test
    public void taskExecutorTest() {
        assertTrue(executor instanceof ThreadPoolTaskExecutor, "Executor should be an instance of ThreadPoolTaskExecutor");

        ThreadPoolTaskExecutor threadPoolTaskExecutor = (ThreadPoolTaskExecutor) executor;
        assertEquals(10, threadPoolTaskExecutor.getCorePoolSize(), "Core pool size should be 10");
        assertEquals(20, threadPoolTaskExecutor.getMaxPoolSize(), "Max pool size should be 20");
        assertEquals(500, threadPoolTaskExecutor.getQueueCapacity(), "Queue capacity should be 500");
        assertEquals("FraudCheck-", threadPoolTaskExecutor.getThreadNamePrefix(), "Thread name prefix should be 'FraudCheck-'");
    }
}
