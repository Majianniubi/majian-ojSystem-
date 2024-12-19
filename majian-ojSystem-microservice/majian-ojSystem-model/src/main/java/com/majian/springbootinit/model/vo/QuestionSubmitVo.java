package com.majian.springbootinit.model.vo;

import cn.hutool.json.JSONUtil;
import com.majian.springbootinit.model.dto.questionSubmit.JudgeInfo;
import com.majian.springbootinit.model.entity.question.QuestionSubmit;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * 题目
 */
@Data
public class QuestionSubmitVo {
    /**
     * id
     */
    private Long id;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 判题信息(json对象)
     */
    private JudgeInfo judgeInfo;

    /**
     * 判题状态(0 -带判题、1-判题中、2-成功、3-失败
     */
    private Integer status;

    /**
     * 题目 id
     */
    private Long questionId;
    /**
     * 提交用户 id
     */
    private Long userId;
    /**
     * 提交者的信息
     */
    private UserVO userVO;
    /**
     * 提交的对应题目信息
     */
    private QuestionVo questionVo;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public static QuestionSubmit voToObj(QuestionSubmitVo questionSubmitVo) {
        if (questionSubmitVo == null) {
            return null;
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitVo, questionSubmit);
        final JudgeInfo judgeInfo1 = questionSubmitVo.getJudgeInfo();
        if(judgeInfo1!=null){
            questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo1));
        }
        return questionSubmit ;
    }

    /**
     * 对象转包装类
     *
     * @param questionSubmit
     * @return
     */
    public static QuestionSubmitVo objToVo(QuestionSubmit questionSubmit) {
        if (questionSubmit == null) {
            return null;
        }
        QuestionSubmitVo questionSubmitVoVO = new QuestionSubmitVo();
        BeanUtils.copyProperties(questionSubmit, questionSubmitVoVO);
        final JudgeInfo judgeInfo1 = JSONUtil.toBean(questionSubmit.getJudgeInfo(), JudgeInfo.class);
        questionSubmitVoVO.setJudgeInfo(judgeInfo1);
        return questionSubmitVoVO;
    }

    private static final long serialVersionUID = 1L;
}
