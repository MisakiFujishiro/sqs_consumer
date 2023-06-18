package com.msa.aws.sqs.sqs_consumer;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Component
public class MessageReceiver implements Runnable {

    private static final String QUEUE_URL = "https://sqs.ap-northeast-1.amazonaws.com/626394096352/MA-fujishiroms-sqs-standard";
    private static final int MAX_NUMBER_OF_MESSAGES = 10; // 一度に受信する最大メッセージ数
    private static final int WAIT_TIME_SECONDS = 20; // メッセージがない場合のロングポーリング待機時間
    private final AmazonSQSAsync sqsAsyncClient;

    @Autowired
    public MessageReceiver(AmazonSQSAsync sqsAsyncClient) {
        this.sqsAsyncClient = sqsAsyncClient;
    }

    @Override
    public void run() {
        while (true) {
            // 受信用のリクエスト作成
            final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(QUEUE_URL)
                    .withMaxNumberOfMessages(MAX_NUMBER_OF_MESSAGES)
                    .withWaitTimeSeconds(WAIT_TIME_SECONDS);

            // 受信リクエストの設定を入力としてSQSからメッセージを受信
            final List<Message> messages = sqsAsyncClient.receiveMessage(receiveMessageRequest).getMessages();



            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (final Message message : messages) {
                System.out.println("PROCESSING START =======================================================" );

                //処理開始時間の表示
                LocalDateTime now_bf = LocalDateTime.now();
                System.out.println("START TIME：" + formatter.format(now_bf)+" & MESSAGE ID： "+message.getMessageId());

                //メッセージ内容の表示
                System.out.println("Message");
                System.out.println("Body:          " + message.getBody());
                System.out.println("SQS MESSAGE ID:            " + message.getMessageId());
                //メッセージの処理（今回は待機するだけ）
                int waitTime = Integer.parseInt(message.getBody()) * 1000;
                System.out.println("wait time: " + waitTime);
                waitInMilliseconds(waitTime);
                //メッセージの削除
                sqsAsyncClient.deleteMessage(QUEUE_URL, message.getReceiptHandle());

                //処理完了時間の表示
                LocalDateTime now_af = LocalDateTime.now();
                System.out.println("END TIME： " + formatter.format(now_af) +" & MESSAGE ID： "+message.getMessageId());
                System.out.println("PROCESSING END =======================================================" );

            }
        }
    }


    //数字を受け取って、その時間待機するためのメソッド
    private void waitInMilliseconds(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}