package com.msa.aws.sqs.sqs_consumer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class frontendController {

    @RequestMapping(value="/sqs-consumer", produces = "text/plain")
    public String frontend(){
        return "Hello sqs-consumer!";
    }
}
