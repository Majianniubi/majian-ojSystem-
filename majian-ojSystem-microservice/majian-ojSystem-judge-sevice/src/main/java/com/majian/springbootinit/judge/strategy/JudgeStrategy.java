package com.majian.springbootinit.judge.strategy;

import com.majian.springbootinit.model.dto.questionSubmit.JudgeInfo;

public interface JudgeStrategy {
    JudgeInfo doJudge(JudgeContext judgeContext);
}
