package com.example.consumer_a.consumer;

import com.example.consumer_a.entity.Account;
import com.example.consumer_a.entity.Client;
import com.example.consumer_a.entity.Transaction;
import com.example.consumer_a.repository.AccountRepository;
import com.example.consumer_a.repository.ClientRepository;
import com.example.consumer_a.repository.TransactionRepository;
import com.example.consumer_a.service.AccountService;
import com.example.consumer_a.service.ClientService;
import com.example.consumer_a.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(topics = {"clients", "accounts", "transactions"}, partitions=1)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers = ${spring.embedded.kafka.brokers}",
        "spring.kafka.consumer.bootstrap-servers = ${spring.embedded.kafka.brokers}"})
public class EventsConsumerIntgTest {



    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    KafkaListenerEndpointRegistry endpointRegistry;

    @SpyBean
    EventsConsumer eventsConsumerSpy;
    @SpyBean
    ClientService clientServiceSpy;
    @SpyBean
    AccountService accountServiceSpy;
    @SpyBean
    TransactionService transactionServiceSpy;

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @BeforeEach
    void setUp(){
        //make sure that container got all the partitions assigned
        for(MessageListenerContainer messageListenerContainer: endpointRegistry.getAllListenerContainers()){
            ContainerTestUtils.waitForAssignment(messageListenerContainer, 3);
        }
    }

    @Test
    void newEvent() throws ExecutionException, InterruptedException, JsonProcessingException {

        //given
        String jsonClient ="{client_id: 00000000000, first_name: Test, last_name: Test, ebanking_id: EB0000000}";
        kafkaTemplate.send("clients", jsonClient).get();

        String jsonAccount = "{'client_id': '00000000000', 'account_nr': '00000000', 'account_type': 'Cash', 'ccy': 'USD', 'iban': 'GB95NWBK70011931900000', 'balance': '10000'}";
        kafkaTemplate.send("accounts", jsonAccount).get();

        String jsonTransaction = "{transaction_id: 81d3a179-b1bc-465b-a9ee-000000000000, amount: \"CHF -3020.27\", iban: GB95NWBK70011931900000, value_date: \"2020-18-08 11:33:05\", description: \"Centennial Bank WITHDRAW\", account_id: 00000000}";
        kafkaTemplate.send("transactions", jsonTransaction).get();

        //when
        CountDownLatch latch = new CountDownLatch(1);
        latch.await(3, TimeUnit.SECONDS);

        //then
        verify(eventsConsumerSpy, times(3)).onMessage(isA(ConsumerRecord.class));
        verify(clientServiceSpy, times(1)).processRecordValue(isA(ConsumerRecord.class));
        verify(accountServiceSpy, times(1)).processRecordValue(isA(ConsumerRecord.class));
        verify(transactionServiceSpy, times(1)).processRecordValue(isA(ConsumerRecord.class));

        Optional<Client> clientOptional = clientRepository.findById("00000000000");
        assert clientOptional.isPresent();

        Optional <Account> accountOptional = accountRepository.findById("00000000");
        assert accountOptional.isPresent();

        Optional <Transaction> transactionOptional = transactionRepository.findById("81d3a179-b1bc-465b-a9ee-000000000000");
        assert transactionOptional.isPresent();
    }
}
