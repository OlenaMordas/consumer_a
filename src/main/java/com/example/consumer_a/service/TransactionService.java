package com.example.consumer_a.service;

import com.example.consumer_a.entity.Transaction;
import com.example.consumer_a.repository.TransactionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    // json object mapper
    Gson gson = new Gson();

    final TransactionRepository transactionRepository;

    public void processRecordValue(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        log.info("Transaction record value: {}", consumerRecord.value());
        Transaction transaction = gson.fromJson(consumerRecord.value(), Transaction.class);
        save(transaction);
    }

    private void save(Transaction transaction){
        transactionRepository.save(transaction);
        log.info("Successfully transaction client entity {}", transaction);
    }
}
