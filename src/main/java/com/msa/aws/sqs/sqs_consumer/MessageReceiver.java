package com.msa.aws.sqs.sqs_consumer;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class MessageReceiver {
    @Autowired
    private AmazonSQS amazonSQSClient;


    public void receiveMessage(){
    String url = "https://sqs.ap-northeast-1.amazonaws.com/626394096352/MA-fujishiroms-sqs-standard";

    ReceiveMessageRequest request = new ReceiveMessageRequest()
                .withQueueUrl(url)
                .withWaitTimeSeconds(5)
                .withMaxNumberOfMessages(5);

    ReceiveMessageResult result = amazonSQSClient.receiveMessage(request);


    System.out.println(result);
    for (Message msg : result.getMessages()) {


        // 受信したメッセージの情報を表示
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now_bf = LocalDateTime.now();
        System.out.println(" Recived Time: "+ formatter.format(now_bf));


        // 受信したメッセージの情報を表示
        System.out.println("["+msg.getMessageId()+"]");
        System.out.println("  Message ID     : " + msg.getMessageId());
        System.out.println("  Receipt Handle : " + msg.getReceiptHandle());
        System.out.println("  Message Body   : " + msg.getBody());


        // 受け取った数字分だけ待機をする
        int waitTime = Integer.parseInt(msg.getBody())*1000;
        waitInMilliseconds(waitTime);
        LocalDateTime now_af = LocalDateTime.now();
        System.out.println(" Recived Time: "+ formatter.format(now_af));


        System.out.println();

        // 受信したメッセージを削除
        amazonSQSClient.deleteMessage(url, msg.getReceiptHandle());
        }
    }
    private void waitInMilliseconds(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
