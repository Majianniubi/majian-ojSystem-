package com.example.mj.controller;

import com.example.mj.JavaNativeCodeSendBox;
import com.example.mj.model.ExecuteCodeRequest;
import com.example.mj.model.ExecuteCodeResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class test {
    private static final String REQUEST_HEADER = "majian";

    private static final  String REQUEST_SECRET = "hello";
    @Resource
    private JavaNativeCodeSendBox javaNativeCodeSendBox;

    @RequestMapping("/test")
    public String test01() {
        return "ok";
    }

    @PostMapping("/testSendBox")
    public ExecuteCodeResponse testSendBox(@RequestBody ExecuteCodeRequest executeCodeRequest,
                                           HttpServletRequest request, HttpServletResponse response) {
        final String header = request.getHeader(REQUEST_HEADER);
        if(!header.equals(REQUEST_SECRET)){
            System.out.println("错误");
            response.setStatus(403);
            return null;
        }
        if (executeCodeRequest == null) {
            throw new RuntimeException("请求参数为空");
        }
        final ExecuteCodeResponse execute = javaNativeCodeSendBox.execute(executeCodeRequest);
        System.out.println(execute);
        return execute;
    }
}
