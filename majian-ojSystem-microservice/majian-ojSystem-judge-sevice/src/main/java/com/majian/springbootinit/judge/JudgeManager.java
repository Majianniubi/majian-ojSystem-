package com.majian.springbootinit.judge;

import com.majian.springbootinit.judge.strategy.DefaultJudgeStrategy;
import com.majian.springbootinit.judge.strategy.JavaJudgeStrategy;
import com.majian.springbootinit.judge.strategy.JudgeContext;
import com.majian.springbootinit.judge.strategy.JudgeStrategy;
import com.majian.springbootinit.model.dto.questionSubmit.JudgeInfo;
import org.springframework.stereotype.Service;

@Service
public class JudgeManager {
    private JudgeStrategy judgeStrategy;
    public JudgeInfo doJudge(String language, JudgeContext judgeContext){
        this.judgeStrategy=new DefaultJudgeStrategy();
        if(language.equals("java")){
            this.judgeStrategy=new JavaJudgeStrategy();
        }
        return this.judgeStrategy.doJudge(judgeContext);
    }
}
