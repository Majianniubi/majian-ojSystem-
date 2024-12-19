package com.example.mj.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExecuteCodeRequest  {
    /**
     * 接受参数的一组输入
     */
    private List<String> inputList;
    /**
     * 接受的代码
     */
    private String code;
    /**
     * 代码的语言类型
     */
    private String language;
}
