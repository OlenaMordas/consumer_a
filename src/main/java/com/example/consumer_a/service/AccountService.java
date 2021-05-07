package com.example.consumer_a.service;

import com.example.consumer_a.entity.Account;
import com.example.consumer_a.repository.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    // json object mapper
    Gson gson = new Gson();

    final AccountRepository accountRepository;

    public void processRecordValue(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        log.info("Account record value: {}", consumerRecord.value());
        Account account = gson.fromJson(consumerRecord.value(), Account.class);
        save(account);
    }

    private void save(Account account){
        accountRepository.save(account);
        log.info("Successfully saved account entity {}", account);
    }
}
