package com.majian.springbootinit.judge;

import cn.hutool.json.JSONUtil;
import com.majian.springbootinit.common.ErrorCode;
import com.majian.springbootinit.exception.BusinessException;
import com.majian.springbootinit.judge.codeSendBox.CodeSendBox;
import com.majian.springbootinit.judge.codeSendBox.CodeSendBoxFactory;
import com.majian.springbootinit.judge.codeSendBox.CodeSendBoxProxy;
import com.majian.springbootinit.judge.strategy.JudgeContext;
import com.majian.springbootinit.model.codeSendBox.ExecuteCodeRequest;
import com.majian.springbootinit.model.codeSendBox.ExecuteCodeResponse;
import com.majian.springbootinit.model.dto.question.JudgeCase;
import com.majian.springbootinit.model.dto.questionSubmit.JudgeInfo;
import com.majian.springbootinit.model.entity.question.Question;
import com.majian.springbootinit.model.entity.question.QuestionSubmit;
import com.majian.springbootinit.model.enums.QuestionSubmitStatusEnum;
import com.majian.springbootinit.serice.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImp implements JudgeService{
    @Value("${codeSendBox.type}")
    private String type;
    @Resource
    private QuestionFeignClient questionFeignClient;
    @Resource
    private JudgeManager judgeManager;

    @Override
    public QuestionSubmit doJudge(Long questionSubmitId) {
        final QuestionSubmit questionSubmit = questionFeignClient.getSubmitQuestionById(questionSubmitId);
        if(questionSubmit==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"提交的题目不存在");
        }
        final Question question = questionFeignClient.getQuestionById(questionSubmit.getQuestionId());
        if(question==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
        if(questionSubmit.getStatus()!= QuestionSubmitStatusEnum.WAITING.getValue()){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"正在等待判题中");
        }
        QuestionSubmit questionSubmitUpdate =new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        final boolean result = questionFeignClient.updateById(questionSubmitUpdate);
        if(!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更改状态失败");
        }
        final CodeSendBox codeSendBox = CodeSendBoxFactory.newInstance(type);
        CodeSendBoxProxy codeSendBoxProxy=new CodeSendBoxProxy(codeSendBox);
        final String judgeCaseStr = question.getJudgeCase();
        final List<JudgeCase> judgeCases = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        final List<String> inputList = judgeCases.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        final ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(questionSubmit.getCode())
                .inputList(inputList)
                .language(questionSubmit.getLanguage())
                .build();
        final ExecuteCodeResponse executeCodeResponse = codeSendBoxProxy.execute(executeCodeRequest);
        //设置判题信息
        JudgeContext judgeContext =new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setQuestion(question);
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(executeCodeResponse.getOutputList());
        judgeContext.setJudgeCaseList(judgeCases);
        System.out.println(judgeContext);
        final JudgeInfo judgeInfo = judgeManager.doJudge(questionSubmit.getLanguage(), judgeContext);
        QuestionSubmit questionSubmitUpdate2 =new QuestionSubmit();
        questionSubmitUpdate2.setId(questionSubmitId);
        questionSubmitUpdate2.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        questionSubmitUpdate2.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        final boolean result2= questionFeignClient.updateById(questionSubmitUpdate2);
        if(!result2){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"状态改变失败");
        }
        final QuestionSubmit questionSubmitResult = questionFeignClient.getSubmitQuestionById(questionSubmitId);
        return questionSubmitResult;
    }
}
