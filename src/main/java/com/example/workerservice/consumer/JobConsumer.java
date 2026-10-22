package com.example.workerservice.consumer;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class JobConsumer {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Getter
    private final String worker="worker-1";

    @Scheduled(fixedDelay = 1000)
    public void consumeJob(){
        List<MapRecord<String,Object,Object>> consumedJobs;
        consumedJobs=redisTemplate.opsForStream().read(
                        Consumer.from("consumer-workers",worker),
                        StreamReadOptions.empty().count(1).block(Duration.ofMillis(500)),
                        StreamOffset.create("stream",ReadOffset.lastConsumed())
                );
        if(consumedJobs!=null){
            for(MapRecord<String,Object,Object> consumedJob:consumedJobs){

                //only for testing , will process later
                System.out.println("Consumed Job Id: "+consumedJob.getId());
                System.out.println("Data:"+ consumedJob.getValue());
                redisTemplate.opsForStream().acknowledge(
                        "stream",
                        "consumer-workers",
                        consumedJob.getId()
                );
            }
        }
    }
}
