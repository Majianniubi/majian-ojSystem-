package com.majian.springbootinit.judge.codeSendBox;
import com.majian.springbootinit.model.codeSendBox.ExecuteCodeRequest;
import com.majian.springbootinit.model.codeSendBox.ExecuteCodeResponse;

public interface CodeSendBox{
    ExecuteCodeResponse execute(ExecuteCodeRequest executeCodeRequest);
}
