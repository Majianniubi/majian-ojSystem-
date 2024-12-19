package com.majian.springbootinit.model.vo;

import cn.hutool.json.JSONUtil;
import com.majian.springbootinit.model.dto.question.JudgeConfig;
import com.majian.springbootinit.model.entity.question.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * 题目
 */
@Data
public class QuestionVo {
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
     * 题目提交数
     */
    private Integer submitNum;

    /**
     * 题目通过数
     */
    private Integer acceptedNum;
    /**
     * 判题配置(json)数组
     */
    private JudgeConfig judgeConfig;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 提交用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;
    /**
     * 创建题目人的信息
     */
    private UserVO userVO;

    public static Question voToObj(QuestionVo questionVO) {
        if (questionVO == null) {
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionVO, question);
        List<String> tagList = questionVO.getTags();
        if(tagList!=null){
            question.setTags(JSONUtil.toJsonStr(tagList));
        }
        final JudgeConfig judgeConfig1 = questionVO.getJudgeConfig();
        if(judgeConfig1!=null){
            question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig1));
        }
        return question;
    }

    /**
     * 对象转包装类
     *
     * @param question
     * @return
     */
    public static QuestionVo objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionVo questionVO = new QuestionVo();
        BeanUtils.copyProperties(question, questionVO);
        final JudgeConfig judgeConfig = JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class);
        questionVO.setJudgeConfig(judgeConfig);
        questionVO.setTags(JSONUtil.toList(question.getTags(), String.class));
        return questionVO;
    }

    private static final long serialVersionUID = 1L;
}
