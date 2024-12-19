package com.majian.springbootinit.controller.inner;

import com.majian.springbootinit.model.entity.question.Question;
import com.majian.springbootinit.model.entity.question.QuestionSubmit;
import com.majian.springbootinit.service.QuestionService;
import com.majian.springbootinit.service.QuestionSubmitService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements com.majian.springbootinit.serice.QuestionFeignClient {
    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionSubmitService questionSubmitService;
    @Override
    @GetMapping("/get/{questionId}")
    public Question getQuestionById(@PathVariable("questionId") Long id){
        final Question questionId = questionService.getById(id);
        return questionId;
    }
    @Override
    @PostMapping("/question_submit/update")
    public boolean updateById(@RequestBody QuestionSubmit questionSubmit){
        final boolean result = questionSubmitService.updateById(questionSubmit);
        return result;
    }
    @Override
    @GetMapping("/question_submit/get/{questionSubmitId}")
    public QuestionSubmit getSubmitQuestionById(@PathVariable("questionSubmitId") Long id){
        final QuestionSubmit questionSubmitId = questionSubmitService.getById(id);
        return questionSubmitId;
    }
}
