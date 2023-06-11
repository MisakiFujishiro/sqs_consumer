package com.msa.aws.sqs.sqs_consumer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SqsConsumerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SqsConsumerApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Thread messageReceiverThread = new Thread(new MessageReceiver());
		messageReceiverThread.start();
	}
}