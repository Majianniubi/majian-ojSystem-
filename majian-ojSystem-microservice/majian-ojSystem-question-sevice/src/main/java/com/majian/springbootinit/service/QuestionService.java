package com.majian.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.majian.springbootinit.model.dto.question.QuestionQueryRequest;
import com.majian.springbootinit.model.entity.question.Question;
import com.majian.springbootinit.model.vo.QuestionVo;

import javax.servlet.http.HttpServletRequest;

/**
* @author majian
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2024-11-15 10:48:43
*/
public interface QuestionService extends IService<Question> {
    /**
     * 校验
     *
     * @param question
     * @param add
     */
    void validQuestion(Question question, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取题目封装
     *
     * @param question
     * @param request
     * @return
     */
    QuestionVo getQuestionVO(Question question, HttpServletRequest request);

    /**
     * 分页获取题目   封装
     *
     * @param questionPage
     * @param request
     * @return
     */
    Page<QuestionVo> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);

}
