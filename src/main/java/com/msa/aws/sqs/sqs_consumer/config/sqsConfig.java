package com.msa.aws.sqs.sqs_consumer.config;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class sqsConfig {
    @Bean
    public AmazonSQSAsync amazonSQSAsyncClient(){
        return AmazonSQSAsyncClientBuilder.defaultClient();
    }
}