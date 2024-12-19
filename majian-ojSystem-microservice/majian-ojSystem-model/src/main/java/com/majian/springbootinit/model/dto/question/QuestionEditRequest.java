package com.majian.springbootinit.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 编辑请求
 *
 * @author ""程序员马建</a>
 * @from <a href="">自我开发项目</a>
 */
@Data
public class QuestionEditRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 栈 队列 数组 链表
     */
    private List<String> tags;
    /**
     * 题目答案
     */
    private String answer;

    /**
     * 判题用例(Json)数组
     */
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置(json)数组
     */
    private JudgeConfig judgeConfig;

    private static final long serialVersionUID = 1L;

}