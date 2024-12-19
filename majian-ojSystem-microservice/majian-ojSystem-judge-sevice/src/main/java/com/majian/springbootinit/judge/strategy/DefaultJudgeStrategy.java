package com.majian.springbootinit.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.majian.springbootinit.model.dto.question.JudgeCase;
import com.majian.springbootinit.model.dto.question.JudgeConfig;
import com.majian.springbootinit.model.dto.questionSubmit.JudgeInfo;
import com.majian.springbootinit.model.entity.question.Question;
import com.majian.springbootinit.model.enums.JudgeInfoMessageEnum;

import java.util.List;

public class DefaultJudgeStrategy implements JudgeStrategy{
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        final JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        final List<String> outputList = judgeContext.getOutputList();
        final List<String> inputList = judgeContext.getInputList();
        final List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        final Question question = judgeContext.getQuestion();
        JudgeInfoMessageEnum judgeInfoMessageEnum=JudgeInfoMessageEnum.WAITING;
        final Long time = judgeInfo.getTime();
        final Long memory = judgeInfo.getMemory();
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setTime(time);
        judgeInfoResponse.setMemory(memory);
        //设置判题信息
        if(outputList.size()!=inputList.size()){
            judgeInfoMessageEnum=JudgeInfoMessageEnum.WRONG_ANSWER;
            return judgeInfoResponse;
        }
        for (int i = 0; i < judgeCaseList.size(); i++) {
            final JudgeCase judgeCase = judgeCaseList.get(i);
            if(!judgeCase.getOutput().equals(outputList.get(i))){
                judgeInfoMessageEnum=JudgeInfoMessageEnum.WRONG_ANSWER;
                return judgeInfoResponse;
            }
        }
        final String judgeConfigStr = question.getJudgeConfig();
        final JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        if(time>judgeConfig.getMemoryLimit()){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            return judgeInfoResponse;
        }
        if((memory/1000000)>judgeConfig.getTimeLimit()){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(JudgeInfoMessageEnum.ACCEPTED.getValue());
        return judgeInfoResponse;
    }
}
