package com.majian.springbootinit.judge.codeSendBox;

import com.majian.springbootinit.model.codeSendBox.ExecuteCodeRequest;
import com.majian.springbootinit.model.codeSendBox.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CodeSendBoxProxy implements CodeSendBox{
    private CodeSendBox codeSendBox;

    public CodeSendBoxProxy(CodeSendBox codeSendBox){
        this.codeSendBox =codeSendBox;
    }
    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息"+executeCodeRequest.toString());
        final ExecuteCodeResponse executeCodeResponse = codeSendBox.execute(executeCodeRequest);
        log.info("代码沙箱反应信息"+executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
