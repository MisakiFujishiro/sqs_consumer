package com.msa.aws.sqs.sqs_consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SqsConsumerApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SqsConsumerApplication.class, args);
		MessageReceiver receiver = context.getBean(MessageReceiver.class);
		receiver.receiveMessage();

	}
}
