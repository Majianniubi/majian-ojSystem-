package com.majian.springbootinit.service.imp;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.majian.springbootinit.common.ErrorCode;
import com.majian.springbootinit.constant.CommonConstant;
import com.majian.springbootinit.exception.BusinessException;
import com.majian.springbootinit.mapper.CommentMapper;
import com.majian.springbootinit.model.dto.comment.CommentAddRequest;
import com.majian.springbootinit.model.dto.comment.CommentQueryRequest;
import com.majian.springbootinit.model.dto.comment.CommentUpdateRequest;
import com.majian.springbootinit.model.dto.question.QuestionQueryRequest;
import com.majian.springbootinit.model.entity.Comment;
import com.majian.springbootinit.model.entity.User;
import com.majian.springbootinit.model.entity.question.Question;
import com.majian.springbootinit.model.vo.CommentVo;
import com.majian.springbootinit.model.vo.UserVO;
import com.majian.springbootinit.serice.QuestionFeignClient;
import com.majian.springbootinit.serice.UserFeignClient;
import com.majian.springbootinit.service.CommentService;
import com.majian.springbootinit.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* @author majian
* @description 针对表【comment(评论)】的数据库操作Service实现
* @createDate 2024-12-05 13:13:19
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService {
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private UserFeignClient userFeignClient;
    @Resource
    private QuestionFeignClient questionFeignClient;

    @Override
    public QueryWrapper<Comment> getQueryWrapper(CommentQueryRequest commentQueryRequest) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        if (commentQueryRequest == null) {
            return queryWrapper;
        }
        Long questionId = commentQueryRequest.getQuestionId();
        String sortField = commentQueryRequest.getSortField();
        String sortOrder = commentQueryRequest.getSortOrder();
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
    @Override
    public List<CommentVo> getCommentsByQuestionId(Long id) {
        final Question question = questionFeignClient.getQuestionById(id);
        if(question==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        final List<Comment> comments = commentMapper.getCommentsByQuestionId(id);
        List<CommentVo> commentVos =new ArrayList<>();
        for (Comment comment : comments) {
            final CommentVo commentVo = CommentVo.objToVo(comment);
            final User user = userFeignClient.getById(comment.getUserId());
            final UserVO userVO = userFeignClient.getUserVO(user);
            commentVo.setUserVO(userVO);
            commentVos.add(commentVo);
        }
        return commentVos;
    }

    @Override
    public boolean AddComment(CommentAddRequest commentAddRequest) {
        Comment comment =new Comment();
        BeanUtils.copyProperties(commentAddRequest,comment);
        final int insert = commentMapper.insert(comment);
        if(insert==0){
            return false;
        }
        return true;
    }

    @Override
    public boolean updateComment(CommentUpdateRequest commentUpdateRequest) {
        Comment comment =new Comment();
        BeanUtils.copyProperties(commentUpdateRequest,comment);
        final int result = commentMapper.updateById(comment);
        if(result==0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"点赞失败");
        }
        return true;
    }

    @Override
    public boolean DeleteCommentById(Long commentId) {
        final int delete = commentMapper.deleteById(commentId);
        if(delete==0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"删除失败");
        }
        return true;
    }
}




