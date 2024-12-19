package com.example.mj;

import com.example.mj.model.ExecuteCodeRequest;
import com.example.mj.model.ExecuteCodeResponse;
public interface CodeSendBox {
    ExecuteCodeResponse execute(ExecuteCodeRequest executeCodeRequest);
}
