package com.example.mj;

import com.example.mj.model.ExecuteCodeRequest;
import com.example.mj.model.ExecuteCodeResponse;
import org.springframework.stereotype.Component;

@Component
public class JavaNativeCodeSendBox extends JavaCodeSendBoxTemplate {
    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest executeCodeRequest) {
        return super.execute(executeCodeRequest);
    }
}
