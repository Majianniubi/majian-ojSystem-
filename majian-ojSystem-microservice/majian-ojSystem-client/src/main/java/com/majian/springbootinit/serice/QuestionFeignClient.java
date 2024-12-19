package com.majian.springbootinit.serice;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.majian.springbootinit.model.dto.question.QuestionQueryRequest;
import com.majian.springbootinit.model.entity.question.Question;
import com.majian.springbootinit.model.entity.question.QuestionSubmit;
import com.majian.springbootinit.model.vo.QuestionVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
* @author majian
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2024-11-15 10:48:43
*/
@FeignClient(value = "majian-ojSystem-question-service", path = "/api/question/inner")
public interface QuestionFeignClient {
    @GetMapping("/get/{questionId}")
    Question getQuestionById(@PathVariable("questionId") Long id);
    @PostMapping("/question_submit/update")
    boolean updateById(@RequestBody QuestionSubmit questionSubmit);
    @GetMapping("/question_submit/get/{questionSubmitId}")
    QuestionSubmit getSubmitQuestionById(@PathVariable("questionSubmitId") Long id);
}
