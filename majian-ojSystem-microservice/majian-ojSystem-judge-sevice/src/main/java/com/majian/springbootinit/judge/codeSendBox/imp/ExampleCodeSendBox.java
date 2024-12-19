package com.majian.springbootinit.judge.codeSendBox.imp;

import com.majian.springbootinit.judge.codeSendBox.CodeSendBox;
import com.majian.springbootinit.model.codeSendBox.ExecuteCodeRequest;
import com.majian.springbootinit.model.codeSendBox.ExecuteCodeResponse;
import com.majian.springbootinit.model.dto.questionSubmit.JudgeInfo;
import com.majian.springbootinit.model.enums.JudgeInfoMessageEnum;
import com.majian.springbootinit.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

public class ExampleCodeSendBox implements CodeSendBox {
    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest executeCodeRequest) {
        ExecuteCodeResponse executeCodeResponse =new ExecuteCodeResponse();
        final List<String> inputList = executeCodeRequest.getInputList();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        executeCodeResponse.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        JudgeInfo judgeInfo=new JudgeInfo();
        judgeInfo.setMessage("执行代码成功");
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
