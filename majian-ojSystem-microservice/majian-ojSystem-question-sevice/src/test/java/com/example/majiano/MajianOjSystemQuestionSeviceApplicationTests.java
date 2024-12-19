package com.example.majiano;

import com.majian.springbootinit.model.entity.rabbitMqConstant;
import com.majian.springbootinit.rabbitmq.MyMessageSender;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class MajianOjSystemQuestionSeviceApplicationTests {
    @Test
    void contextLoads() {
        MyMessageSender messageSender =new MyMessageSender();

    }

}
