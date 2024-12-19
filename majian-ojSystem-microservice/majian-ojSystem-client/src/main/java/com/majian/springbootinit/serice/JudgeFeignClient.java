package com.majian.springbootinit.serice;

import com.majian.springbootinit.model.entity.question.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "majian-ojSystem-judge-service", path = "/api/judge/inner")
public interface JudgeFeignClient {
    @GetMapping("/doJudge/{id}")
    QuestionSubmit doJudge(@PathVariable("id") Long questionSubmitId);
}
