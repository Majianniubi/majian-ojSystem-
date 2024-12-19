package com.majian.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.majian.springbootinit.common.ErrorCode;
import com.majian.springbootinit.constant.CommonConstant;
import com.majian.springbootinit.exception.BusinessException;
import com.majian.springbootinit.mapper.QuestionSubmitMapper;
import com.majian.springbootinit.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.majian.springbootinit.model.dto.questionSubmit.QuestionSubmitQueryRequest;
import com.majian.springbootinit.model.entity.User;
import com.majian.springbootinit.model.entity.question.Question;
import com.majian.springbootinit.model.entity.question.QuestionSubmit;
import com.majian.springbootinit.model.enums.QuestionSubmitLanguageEnum;
import com.majian.springbootinit.model.enums.QuestionSubmitStatusEnum;
import com.majian.springbootinit.model.vo.QuestionSubmitVo;
import com.majian.springbootinit.rabbitmq.MyMessageSender;
import com.majian.springbootinit.serice.JudgeFeignClient;
import com.majian.springbootinit.serice.UserFeignClient;
import com.majian.springbootinit.service.QuestionService;
import com.majian.springbootinit.service.QuestionSubmitService;
import com.majian.springbootinit.utils.SqlUtils;
import com.qcloud.cos.utils.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import com.majian.springbootinit.model.entity.rabbitMqConstant;
import java.util.stream.Collectors;

/**
* @author majian
* @description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2024-11-15 10:55:50
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService {
    @Resource
    private QuestionService questionService;
    @Resource
    private UserFeignClient userFeignClient;
    @Resource
    private JudgeFeignClient judgeFeignClient;
    @Resource
    private MyMessageSender myMessageSender;

    /**
     * 点赞
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        QuestionSubmitLanguageEnum languageEnum=QuestionSubmitLanguageEnum.getEnumByValue(questionSubmitAddRequest.getLanguage());
        if(languageEnum == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"没有该语言");
        }
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionSubmitAddRequest.getQuestionId());
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        long userId = loginUser.getId();
        QuestionSubmit questionSubmit =new QuestionSubmit();
        questionSubmit.setLanguage(questionSubmitAddRequest.getLanguage());
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setJudgeInfo("{}");
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setQuestionId(questionSubmitAddRequest.getQuestionId());
        questionSubmit.setUserId(userId);
        boolean save = this.save(questionSubmit);
        if(!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"数据插入失败");
        }
        final Long questionSubmitId = questionSubmit.getId();
        myMessageSender.sendMessage(rabbitMqConstant.EXCHANGE_NAME,rabbitMqConstant.ROUTING_KEY,String.valueOf(questionSubmitId));
        return questionSubmitId;
    }
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        String language = questionSubmitQueryRequest.getLanguage();
        Long userId = questionSubmitQueryRequest.getUserId();
        Integer status = questionSubmitQueryRequest.getStatus();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();
        // 拼接查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status)!=null, "questionId", questionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSubmitVo getQuestionVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVo questionSubmitVo= QuestionSubmitVo.objToVo(questionSubmit);
        final Long userId = questionSubmit.getUserId();
        if(userId.equals(questionSubmitVo.getUserId())&&!userFeignClient.isAdmin(loginUser)){
            questionSubmitVo.setCode(null);
        }
        return questionSubmitVo;
    }

    @Override
    public Page<QuestionSubmitVo> getQuestionVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        final List<QuestionSubmit> records = questionSubmitPage.getRecords();
        Page<QuestionSubmitVo> questionSubmitVoPage=new Page<>(questionSubmitPage.getCurrent(),questionSubmitPage.getPages(),
                questionSubmitPage.getTotal());
        if(CollectionUtils.isNullOrEmpty(records)){
            return questionSubmitVoPage;
        }
        final List<QuestionSubmitVo> questionSubmitVoCollect = records.stream().map(questionSubmit ->getQuestionVO(questionSubmit,loginUser)
        ).collect(Collectors.toList());
        questionSubmitVoPage.setRecords(questionSubmitVoCollect);
        return questionSubmitVoPage;
    }

}




