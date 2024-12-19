package com.majian.springbootinit.model.dto.comment;

import com.majian.springbootinit.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

@Data
public class CommentQueryRequest extends PageRequest implements Serializable {
    /**
     * 题目id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}
