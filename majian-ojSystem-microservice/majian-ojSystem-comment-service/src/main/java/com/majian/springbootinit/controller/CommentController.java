package com.majian.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.majian.springbootinit.common.BaseResponse;
import com.majian.springbootinit.common.ErrorCode;
import com.majian.springbootinit.common.ResultUtils;
import com.majian.springbootinit.exception.BusinessException;
import com.majian.springbootinit.model.dto.comment.CommentAddRequest;
import com.majian.springbootinit.model.dto.comment.CommentQueryRequest;
import com.majian.springbootinit.model.dto.comment.CommentUpdateRequest;
import com.majian.springbootinit.model.entity.Comment;
import com.majian.springbootinit.model.entity.User;
import com.majian.springbootinit.model.entity.question.Question;
import com.majian.springbootinit.model.vo.CommentVo;
import com.majian.springbootinit.model.vo.UserVO;
import com.majian.springbootinit.serice.QuestionFeignClient;
import com.majian.springbootinit.serice.UserFeignClient;
import com.majian.springbootinit.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class CommentController {
    @Resource
    private CommentService commentService;
    @Resource
    private UserFeignClient userFeignClient;
    @Resource
    private QuestionFeignClient questionFeignClient;

    @GetMapping("/get")
    public BaseResponse<List<CommentVo>> getCommentsByQuestionId(@RequestParam("questionId") Long questionId) {
        final List<CommentVo> commentVos = commentService.getCommentsByQuestionId(questionId);
        return ResultUtils.success(commentVos);
    }

    @PostMapping("/add")
    public BaseResponse<Boolean> AddComment(@RequestBody CommentAddRequest commentAddRequest,HttpServletRequest request) {
        final User loginUser = userFeignClient.getLoginUser(request);
        final boolean result = commentService.AddComment(commentAddRequest);
        return ResultUtils.success(result);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateComment(@RequestBody CommentUpdateRequest commentUpdateRequest) {
        final boolean result = commentService.updateComment(commentUpdateRequest);
        return ResultUtils.success(result);
    }
    @PostMapping("/query/by/condition")
    public BaseResponse<List<CommentVo>> getByCondition(@RequestBody CommentQueryRequest commentQueryRequest) {
        if (commentQueryRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        final Question question = questionFeignClient.getQuestionById(commentQueryRequest.getQuestionId());
        if(question==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        final QueryWrapper<Comment> queryWrapper = commentService.getQueryWrapper(commentQueryRequest);
        final List<Comment> comments = commentService.list(queryWrapper);
        List<CommentVo> commentVos =new ArrayList<>();
        for (Comment comment : comments) {
            final CommentVo commentVo = CommentVo.objToVo(comment);
            final User user = userFeignClient.getById(comment.getUserId());
            final UserVO userVO = userFeignClient.getUserVO(user);
            commentVo.setUserVO(userVO);
            commentVos.add(commentVo);
        }
        return ResultUtils.success(commentVos);
    }

    @GetMapping("/delete")
    public BaseResponse<Boolean> DeleteById(@RequestParam("id") Long commentId, HttpServletRequest request) {
        final boolean result = commentService.DeleteCommentById(commentId);
        final User loginUser = userFeignClient.getLoginUser(request);
        if(!userFeignClient.isAdmin(loginUser)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success(result);
    }
}
