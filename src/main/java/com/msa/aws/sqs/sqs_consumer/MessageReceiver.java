package com.msa.aws.sqs.sqs_consumer;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Component
public class MessageReceiver {
    @Autowired
    private AmazonSQS amazonSQSClient;

    // check processing or waiting
    private boolean processing = false;

    @Scheduled(fixedDelay = 1000)
    public void receiveMessage() {
        if (processing) {
            return;
        }

        String url = "https://sqs.ap-northeast-1.amazonaws.com/626394096352/MA-fujishiroms-sqs-standard";

        ReceiveMessageRequest request = new ReceiveMessageRequest()
                .withQueueUrl(url)
                .withWaitTimeSeconds(5)
                .withMaxNumberOfMessages(10);  // 受信するメッセージの最大数を増やす

        ReceiveMessageResult result = amazonSQSClient.receiveMessage(request);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        processing = true;

        List<Message> messages = result.getMessages();  // 受信したメッセージをリストとして取得
        for (Message msg : messages) {
            // 処理開始時間
            LocalDateTime now_bf = LocalDateTime.now();
            System.out.println("Received Time: " + formatter.format(now_bf));

            // 受信したメッセージの情報を表示
            System.out.println("[" + msg.getMessageId() + "]");
            System.out.println("  Message ID     : " + msg.getMessageId());
            System.out.println("  Receipt Handle : " + msg.getReceiptHandle());
            System.out.println("  Message Body   : " + msg.getBody());

            // 受け取った数字分だけ待機をする
            int waitTime = Integer.parseInt(msg.getBody()) * 10000;
            System.out.println("wait time: " + waitTime);
            waitInMilliseconds(waitTime);

            // 処理終了時間
            LocalDateTime now_af = LocalDateTime.now();
            System.out.println("Processed Time: " + formatter.format(now_af));

            // 受信したメッセージを削除
            amazonSQSClient.deleteMessage(url, msg.getReceiptHandle());
        }

        processing = false;
    }

    private void waitInMilliseconds(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
