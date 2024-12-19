package com.majian.springbootinit.model.vo;



import com.baomidou.mybatisplus.annotation.TableField;
import com.majian.springbootinit.model.entity.Comment;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
@Data
public class CommentVo implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 创建用户 id
     */
    private UserVO userVO;

    /**
     * 评论点赞数
     */
    private Long commentThumb;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public static Comment voToObj(CommentVo commentVo) {
        if (commentVo == null) {
            return null;
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentVo, comment);
        return comment ;
    }

    /**
     * 对象转包装类
     *
     * @param comment
     * @return
     */
    public static CommentVo objToVo(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentVo commentVO = new CommentVo();
        BeanUtils.copyProperties(comment, commentVO);
        return commentVO;
    }
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
