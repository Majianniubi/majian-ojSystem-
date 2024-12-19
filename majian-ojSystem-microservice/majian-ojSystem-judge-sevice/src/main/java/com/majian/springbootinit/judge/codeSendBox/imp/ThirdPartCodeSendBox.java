package com.majian.springbootinit.judge.codeSendBox.imp;

import com.majian.springbootinit.judge.codeSendBox.CodeSendBox;
import com.majian.springbootinit.model.codeSendBox.ExecuteCodeRequest;
import com.majian.springbootinit.model.codeSendBox.ExecuteCodeResponse;

public class ThirdPartCodeSendBox implements CodeSendBox {
    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("thirdPart");
        return null;
    }
}
