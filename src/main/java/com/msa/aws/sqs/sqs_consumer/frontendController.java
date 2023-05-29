package com.msa.aws.sqs.sqs_consumer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class frontendController {

    @RequestMapping("/sqs-consumer")
    public String frontend(){
        return "hello sqs consumer";
    }
}
