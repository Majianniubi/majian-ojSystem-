package com.majian.springbootinit.model.dto.comment;

import com.majian.springbootinit.model.dto.question.JudgeCase;
import com.majian.springbootinit.model.dto.question.JudgeConfig;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class CommentAddRequest implements Serializable {
    /**
     * 内容
     */
    private String content;
    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 创建用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}
