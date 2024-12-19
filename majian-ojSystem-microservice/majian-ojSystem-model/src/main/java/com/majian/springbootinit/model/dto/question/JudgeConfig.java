package com.majian.springbootinit.model.dto.question;

import lombok.Data;

/**
判题配置
 */
@Data
public class JudgeConfig {
    /**
     * 时间限制(s)
     */
    private Long timeLimit;
    /**
     * 内存限制(kb)
     */
    private Long memoryLimit;
    /**
     *堆栈限制(kb)
     */
    private Long stackLimit;
}
