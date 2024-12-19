package com.majian.springbootinit.model.dto.question;

import com.majian.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询请求
 *
 * @author ""程序员马建</a>
 * @from <a href="">自我开发项目</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionQueryRequest extends PageRequest implements Serializable {

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
     * 提交用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}