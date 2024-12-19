package com.majian.springbootinit.model.dto.questionSubmit;

import com.majian.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author ""程序员马建</a>
 * @from <a href="">自我开发项目</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {
    /**
     * 提交的提交的题目Id
     */
    private Long questionId;
    /**
     * 提交题目的语言
     */
    private String language;
    /**
     * 题目提交的userId
     */
    private Long userId;
    /**
     * 题目提交的状态
     */
    private Integer status;
    private static final long serialVersionUID = 1L;
}