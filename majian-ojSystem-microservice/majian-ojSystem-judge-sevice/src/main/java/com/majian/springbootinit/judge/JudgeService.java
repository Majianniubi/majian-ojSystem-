package com.majian.springbootinit.judge;

import com.majian.springbootinit.model.entity.question.QuestionSubmit;

public interface JudgeService {
    QuestionSubmit doJudge(Long questionSubmitId);
}
