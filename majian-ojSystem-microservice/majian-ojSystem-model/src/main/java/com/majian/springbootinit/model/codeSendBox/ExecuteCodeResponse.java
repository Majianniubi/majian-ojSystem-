package com.majian.springbootinit.model.codeSendBox;

import com.majian.springbootinit.model.dto.questionSubmit.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecuteCodeResponse {
    /**
     * 代码的输出
     */
    private List<String> outputList;
    /**
     * 代码沙箱的信息
     */
    private String message;
    /**
     * 执行信息
     */
    private JudgeInfo judgeInfo;
    /**
     * 执行状态
     */
    private Integer status;

}
