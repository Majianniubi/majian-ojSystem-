package com.majian.springbootinit.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.majian.springbootinit.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.majian.springbootinit.model.dto.questionSubmit.QuestionSubmitQueryRequest;
import com.majian.springbootinit.model.entity.User;
import com.majian.springbootinit.model.entity.question.QuestionSubmit;
import com.majian.springbootinit.model.vo.QuestionSubmitVo;

/**
* @author majian
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2024-11-15 10:55:50
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);
    Page<QuestionSubmitVo> getQuestionVOPage(Page<QuestionSubmit> questionSubmit, User loginUser);

    QuestionSubmitVo getQuestionVO(QuestionSubmit questionSubmit, User loginUser);
}
