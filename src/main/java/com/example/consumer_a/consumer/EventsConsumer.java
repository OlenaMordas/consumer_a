package com.example.consumer_a.consumer;

import com.example.consumer_a.service.ClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class EventsConsumer {


    private final ClientService clientService;

    //reading the topic from the beginning
    @KafkaListener(id = "receiver-api",
            topicPartitions =
                    { @TopicPartition(topic = "clients",
                            partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
    public void onMessage(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {

        log.info("ConsumerRecord: {}", consumerRecord);
        clientService.processRecordValue(consumerRecord);

    }
}
