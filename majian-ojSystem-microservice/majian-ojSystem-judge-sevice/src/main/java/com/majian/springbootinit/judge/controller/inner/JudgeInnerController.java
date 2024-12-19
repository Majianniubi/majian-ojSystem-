package com.majian.springbootinit.judge.controller.inner;

import com.majian.springbootinit.judge.JudgeService;
import com.majian.springbootinit.model.entity.question.QuestionSubmit;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements com.majian.springbootinit.serice.JudgeFeignClient {
    @Resource
    private JudgeService judgeService;
    @Override
    @GetMapping("/doJudge/{id}")
    public QuestionSubmit doJudge(@PathVariable("id") Long questionSubmitId){
        final QuestionSubmit questionSubmit = judgeService.doJudge(questionSubmitId);
        return questionSubmit;
    }
}
