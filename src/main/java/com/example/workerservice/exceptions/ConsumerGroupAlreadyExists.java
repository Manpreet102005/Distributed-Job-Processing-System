package com.example.workerservice.exceptions;

public class ConsumerGroupAlreadyExists extends RuntimeException{
    public ConsumerGroupAlreadyExists(){
        super("Consumer Group Already Exists");
    }
}
