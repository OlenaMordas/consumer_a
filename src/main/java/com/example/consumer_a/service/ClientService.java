package com.example.consumer_a.service;

import com.example.consumer_a.entity.Client;
import com.example.consumer_a.repository.ClientRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ClientService {

    // json object mapper
    Gson gson = new Gson();

    final ClientRepository clientRepository;

    public void processRecordValue(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        log.info("Record value: {}", consumerRecord.value());
        Client client = gson.fromJson(consumerRecord.value(), Client.class);
        save(client);
    }

    private void save(Client client){
        clientRepository.save(client);
        log.info("Successfully saved client entity {}", client);
    }
}
