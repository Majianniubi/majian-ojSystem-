package com.majian.springbootinit.judge.codeSendBox.imp;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.majian.springbootinit.common.ErrorCode;
import com.majian.springbootinit.exception.BusinessException;
import com.majian.springbootinit.judge.codeSendBox.CodeSendBox;
import com.majian.springbootinit.model.codeSendBox.ExecuteCodeRequest;
import com.majian.springbootinit.model.codeSendBox.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;

public class RemoteCodeSendBox implements CodeSendBox {
    private static final String REQUEST_HEADER = "majian";

    private static final String REQUEST_SECRET = "hello";
    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest executeCodeRequest ) {
        String url = "http://localhost:8088/testSendBox";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        final String body = HttpUtil.createPost(url)
                .header(REQUEST_HEADER,REQUEST_SECRET)
                .body(json)
                .execute()
                .body();
        if(StringUtils.isBlank(body)){
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"execute remoteSenBox error message="+body);
        }
        final ExecuteCodeResponse executeCodeResponse = JSONUtil.toBean(body, ExecuteCodeResponse.class);
        return executeCodeResponse;
    }
}
