package com.example.mj.model;

import lombok.Data;

/**
 * 运行的结果
 */
@Data
public class ProcessExitMessage {
    /**
     * 运行码
     */
    private Integer exitCode;
    /**
     * 成功信息
     */
    private String successMessage;
    /**
     * 失败信息
     */
    private String errorMessage;
    /**
     * 执行程序时间
     */
    private Long time;
    /**
     * 内存
     */
    private Long memory = 0L;
}
