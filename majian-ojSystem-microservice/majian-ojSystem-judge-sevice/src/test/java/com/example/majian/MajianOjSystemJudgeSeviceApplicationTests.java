package com.example.majian;

import com.majian.springbootinit.judge.codeSendBox.CodeSendBox;
import com.majian.springbootinit.judge.codeSendBox.CodeSendBoxFactory;
import com.majian.springbootinit.judge.codeSendBox.CodeSendBoxProxy;
import com.majian.springbootinit.model.codeSendBox.ExecuteCodeRequest;
import com.majian.springbootinit.model.codeSendBox.ExecuteCodeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
@SpringBootApplication
class MajianOjSystemJudgeSeviceApplicationTests {

    @Test
    void contextLoads() {
        final CodeSendBox codeSendBox = CodeSendBoxFactory.newInstance("remote");
        CodeSendBoxProxy codeSendBoxProxy=new CodeSendBoxProxy(codeSendBox);

        String code ="public class MainApplication {\n" +
                "    public static void main(String[] args) {\n" +
                "        int a = Integer.parseInt(args[0]);\n" +
                "        int b =Integer.parseInt(args[1]);\n" +
                "        System.out.println(\"结果:\"+(a+b));\n" +
                "    }\n" +
                "}";
        ExecuteCodeRequest executeCodeRequest=ExecuteCodeRequest.builder().
                language("java").code(code).inputList(Arrays.asList("1 2","1 3")).build();
        System.out.println(executeCodeRequest);
        final ExecuteCodeResponse execute = codeSendBoxProxy.execute(executeCodeRequest);
        System.out.println(execute);
    }

}
