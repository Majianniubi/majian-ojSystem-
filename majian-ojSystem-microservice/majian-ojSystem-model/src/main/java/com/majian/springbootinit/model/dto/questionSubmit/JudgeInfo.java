package com.majian.springbootinit.model.dto.questionSubmit;

import lombok.Data;

/**
 * 判题信息
 */
@Data
public class JudgeInfo {
    /**
     * 消耗时间
     */
    private Long time;
    /**
     * 需要内存
     */
    private Long memory;
    /**
     * 程序执行信息
     */
    private String message;
}
