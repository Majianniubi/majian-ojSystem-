package com.majian.springbootinit.judge.strategy;

import com.majian.springbootinit.model.dto.question.JudgeCase;
import com.majian.springbootinit.model.dto.questionSubmit.JudgeInfo;
import com.majian.springbootinit.model.entity.question.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JudgeContext {
    private JudgeInfo judgeInfo;
    private List<String> inputList;
    private List<String> outputList;
    private List<JudgeCase> judgeCaseList;
    private Question question;

}
