package com.majian.springbootinit.model.dto.comment;

import lombok.Data;

import java.io.Serializable;
@Data
public class CommentUpdateRequest implements Serializable {
    /**
     * 评论id
     */
    private Long id;
    /**
     * 评论点赞数
     */
    private Long commentThumb;
    private static final long serialVersionUID = 1L;
}
