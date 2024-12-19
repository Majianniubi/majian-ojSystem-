package com.majian.springbootinit.model.dto.questionSubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建提交请求
 *
 * @author ""程序员马建</a>
 * @from <a href="">自我开发项目</a>
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;
    /**
     * 提交的题目Id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}
