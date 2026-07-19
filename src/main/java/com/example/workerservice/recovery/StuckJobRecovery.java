package com.example.workerservice.recovery;

import com.example.workerservice.consumer.JobConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.PendingMessage;
import org.springframework.data.redis.connection.stream.PendingMessages;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class StuckJobRecovery {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JobConsumer jobConsumer;

    @Scheduled(fixedDelay = 30000)
    public void recoverStuckJobs(){
        PendingMessages pendingMessages;
        pendingMessages=redisTemplate.opsForStream()
                .pending("stream",
                        "consumer-workers",
                        Range.unbounded(),
                        10
                        );
        for(PendingMessage pendingMessage:pendingMessages){
            System.out.println("***********************************************");
            System.out.println("Id: "+pendingMessage.getIdAsString());
            System.out.println("Consumer Name: "+ pendingMessage.getConsumerName());
            System.out.println("Total Delievery Count: "+pendingMessage.getTotalDeliveryCount());
            System.out.println("Elaped Time: "+pendingMessage.getElapsedTimeSinceLastDelivery());
            System.out.println("***********************************************");
            redisTemplate.opsForStream().claim(
                    "stream",
                    "consumer-workers",
                    jobConsumer.getWorker(),
                    Duration.ofMillis(0),
                    pendingMessage.getId()
            );
            System.out.println("Claimed Successfully");
        }
    }
}
