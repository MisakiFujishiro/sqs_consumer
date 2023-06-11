package com.msa.aws.sqs.sqs_consumer;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MessageReceiver implements Runnable {

    private static final String QUEUE_URL = "https://sqs.ap-northeast-1.amazonaws.com/626394096352/MA-fujishiroms-sqs-standard";
    private static final int MAX_NUMBER_OF_MESSAGES = 10; // 一度に受信する最大メッセージ数
    private static final int WAIT_TIME_SECONDS = 20; // メッセージがない場合のロングポーリング待機時間
    private final AmazonSQSAsync sqsAsyncClient;

    public MessageReceiver() {
        this.sqsAsyncClient = AmazonSQSAsyncClientBuilder.defaultClient();
    }

    @Override
    public void run() {
        while (true) {
            final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(QUEUE_URL)
                    .withMaxNumberOfMessages(MAX_NUMBER_OF_MESSAGES)
                    .withWaitTimeSeconds(WAIT_TIME_SECONDS);

            final List<Message> messages = sqsAsyncClient.receiveMessage(receiveMessageRequest).getMessages();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


            for (final Message message : messages) {
                System.out.println("======================================================= start =======================================================" );
                LocalDateTime now_bf = LocalDateTime.now();
                System.out.println("Received Time: " + formatter.format(now_bf));

                System.out.println("Message");
                System.out.println("Body:          " + message.getBody());


                int waitTime = Integer.parseInt(message.getBody()) * 1000;
                System.out.println("wait time: " + waitTime);
                waitInMilliseconds(waitTime);
                LocalDateTime now_af = LocalDateTime.now();
                System.out.println("Processed Time: " + formatter.format(now_af));



                sqsAsyncClient.deleteMessage(QUEUE_URL, message.getReceiptHandle());
                System.out.println("======================================================= end =======================================================" );

            }
        }
    }
    private void waitInMilliseconds(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}