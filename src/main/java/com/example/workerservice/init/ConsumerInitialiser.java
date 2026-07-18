package com.example.workerservice.init;

import com.example.workerservice.exceptions.ConsumerGroupAlreadyExists;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ConsumerInitialiser {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostConstruct
    public void createConsumerGroup(){
        try {
            redisTemplate.opsForStream()
                    .createGroup(
                            "stream",
                            ReadOffset.latest(),
                            "consumer-workers");
        }catch(Exception e){
            throw new ConsumerGroupAlreadyExists();
        }
    }
}
