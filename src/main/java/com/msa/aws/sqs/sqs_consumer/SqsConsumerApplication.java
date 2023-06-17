package com.msa.aws.sqs.sqs_consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SqsConsumerApplication implements CommandLineRunner {

	private final MessageReceiver messageReceiver;

	@Autowired
	public SqsConsumerApplication(MessageReceiver messageReceiver) {
		this.messageReceiver = messageReceiver;
	}

	public static void main(String[] args) {
		SpringApplication.run(SqsConsumerApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Thread messageReceiverThread = new Thread(messageReceiver);
		messageReceiverThread.start();
	}
}