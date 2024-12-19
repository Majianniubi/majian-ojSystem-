package com.majian.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.majian.springbootinit.model.entity.Comment;
import com.majian.springbootinit.model.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author majian
* @description 针对表【comment(评论)】的数据库操作Mapper
* @createDate 2024-12-05 13:13:19
* @Entity new.domain.Comment
*/
public interface CommentMapper extends BaseMapper<Comment> {
    List<Comment> getCommentsByQuestionId(@Param("questionId") Long id);
}




