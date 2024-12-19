package com.majian.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.majian.springbootinit.model.dto.comment.CommentAddRequest;
import com.majian.springbootinit.model.dto.comment.CommentQueryRequest;
import com.majian.springbootinit.model.dto.comment.CommentUpdateRequest;
import com.majian.springbootinit.model.dto.question.QuestionQueryRequest;
import com.majian.springbootinit.model.entity.Comment;
import com.majian.springbootinit.model.entity.question.Question;
import com.majian.springbootinit.model.vo.CommentVo;

import java.util.List;

/**
 * @author majian
 * @description 针对表【comment(评论)】的数据库操作Service
 * @createDate 2024-12-05 13:13:19
 */
public interface CommentService extends IService<Comment> {
    List<CommentVo> getCommentsByQuestionId(Long id);

    boolean AddComment(CommentAddRequest commentAddRequest);

    boolean updateComment(CommentUpdateRequest commentUpdateRequest);

    boolean DeleteCommentById(Long commentId);
    QueryWrapper<Comment> getQueryWrapper(CommentQueryRequest commentQueryRequest);
}
