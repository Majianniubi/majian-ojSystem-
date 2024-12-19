package com.majian.springbootinit.service.impl;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.majian.springbootinit.common.ErrorCode;
import com.majian.springbootinit.constant.CommonConstant;
import com.majian.springbootinit.exception.BusinessException;
import com.majian.springbootinit.exception.ThrowUtils;
import com.majian.springbootinit.mapper.QuestionMapper;
import com.majian.springbootinit.model.dto.question.QuestionQueryRequest;
import com.majian.springbootinit.model.entity.User;
import com.majian.springbootinit.model.entity.question.Question;
import com.majian.springbootinit.model.vo.QuestionVo;
import com.majian.springbootinit.model.vo.UserVO;
import com.majian.springbootinit.serice.UserFeignClient;
import com.majian.springbootinit.service.QuestionService;
import com.majian.springbootinit.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author majian
* @description 针对表【Question(题目)】的数据库操作Service实现
* @createDate 2024-11-15 10:48:43
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>implements QuestionService {
        @Resource
        private UserFeignClient userFeignClient;

        @Override
        public void validQuestion(Question question, boolean add) {
            if (question == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            Long id = question.getId();
            String title = question.getTitle();
            String content = question.getContent();
            String tags = question.getTags();
            String answer = question.getAnswer();
            Integer submitNum = question.getSubmitNum();
            Integer acceptedNum = question.getAcceptedNum();
            String judgeCase = question.getJudgeCase();
            String judgeConfig = question.getJudgeConfig();
            Integer thumbNum = question.getThumbNum();
            Integer favourNum = question.getFavourNum();
            Long userId = question.getUserId();
            Date createTime = question.getCreateTime();
            Date updateTime = question.getUpdateTime();
            Integer isDelete = question.getIsDelete();

            // 创建时，参数不能为空
            if (add) {
                ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
            }
            // 有参数则校验
            if (StringUtils.isNotBlank(title) && title.length() > 80) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
            }
            if (StringUtils.isNotBlank(content) && content.length() > 8192) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
            }
            if (StringUtils.isNotBlank(answer) && answer.length() > 8192) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
            }
            if (StringUtils.isNotBlank(judgeCase) && judgeCase.length() > 8192) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
            }
            if (StringUtils.isNotBlank(judgeConfig) && judgeConfig.length() > 8192) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
            }
        }

        /**
         * 获取查询包装类
         *
         * @param questionQueryRequest
         * @return
         */
        @Override
        public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
            QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
            if (questionQueryRequest == null) {
                return queryWrapper;
            }
            Long id = questionQueryRequest.getId();
            String title = questionQueryRequest.getTitle();
            String content = questionQueryRequest.getContent();
            List<String> tags = questionQueryRequest.getTags();
            Long userId = questionQueryRequest.getUserId();
            String sortField = questionQueryRequest.getSortField();
            String sortOrder = questionQueryRequest.getSortOrder();

            // 拼接查询条件
            queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
            queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
            if (CollUtil.isNotEmpty(tags)) {
                for (String tag : tags) {
                    queryWrapper.like("tags", "\"" + tag + "\"");
                }
            }
            queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
            queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
            queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "isDelete", false);
            queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                    sortField);
            return queryWrapper;
        }

        @Override
        public QuestionVo getQuestionVO(Question question, HttpServletRequest request) {
            QuestionVo questionVO = QuestionVo.objToVo(question);
            // 1. 关联查询用户信息
            Long userId = question.getUserId();
            User user = null;
            if (userId != null && userId > 0) {
                user = userFeignClient.getById(userId);
            }
            UserVO userVO = userFeignClient.getUserVO(user);
            questionVO.setUserVO(userVO);
            // 2. 已登录，获取题目
            return questionVO;
        }

        @Override
        public Page<QuestionVo> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {
            List<Question> questionList = questionPage.getRecords();
            Page<QuestionVo> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
            if (CollUtil.isEmpty(questionList)) {
                return questionVOPage;
            }
            // 1. 关联查询用户信息
            Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
            Map<Long, List<User>> userIdUserListMap = userFeignClient.listByIds(userIdSet).stream()
                    .collect(Collectors.groupingBy(User::getId));
            // 填充信息
            List<QuestionVo> questionVOList = questionList.stream().map(question -> {
                QuestionVo questionVO = QuestionVo.objToVo(question);
                Long userId = question.getUserId();
                User user = null;
                if (userIdUserListMap.containsKey(userId)) {
                    user = userIdUserListMap.get(userId).get(0);
                }
                questionVO.setUserVO(userFeignClient.getUserVO(user));
                return questionVO;
            }).collect(Collectors.toList());
            questionVOPage.setRecords(questionVOList);
            return questionVOPage;
        }
}




