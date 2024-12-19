package com.example.mj.docker;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.example.mj.CodeSendBox;
import com.example.mj.JavaDockerCodeSendBox;
import com.example.mj.Utils.processUtils;
import com.example.mj.model.ExecuteCodeRequest;
import com.example.mj.model.ExecuteCodeResponse;
import com.example.mj.model.JudgeInfo;
import com.example.mj.model.ProcessExitMessage;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class JavaNativeCodeSendBoxOld implements CodeSendBox {
    private Long TIME_OUT = 5000L;
    private String CODE_DEPOSIT_DIR = "temCode";

    private String CODE_EXECUTE_FILE = "MainApplication.java";

    private String[] BLACK_WORD={"file","exec"};
    public static void main(String[] args) {
        ExecuteCodeRequest executeCodeRequest=new ExecuteCodeRequest();
        executeCodeRequest.setLanguage("java");
        executeCodeRequest.setInputList(Arrays.asList("1 2","1 3"));
        String code= ResourceUtil.readStr("temCode/MainApplication.java",StandardCharsets.UTF_8);
        executeCodeRequest.setCode(code);
        final ExecuteCodeResponse execute = new JavaNativeCodeSendBoxOld().execute(executeCodeRequest);
        System.out.println(execute);
    }

    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest executeCodeRequest) {
        final List<String> inputList = executeCodeRequest.getInputList();
        final String code = executeCodeRequest.getCode();
        WordTree wordTree=new WordTree();
        wordTree.addWords(BLACK_WORD);
        final FoundWord foundWord = wordTree.matchWord(code);
        if(foundWord!=null){
            throw new RuntimeException("违规代码"+foundWord);
        }
        final String systemPath = System.getProperty("user.dir");
        String executeCodeFileDir= systemPath + File.separator + CODE_DEPOSIT_DIR;
        if(!FileUtil.exist(executeCodeFileDir)){
            FileUtil.mkdir(executeCodeFileDir);
        }
        String UserCodeDepositDir = executeCodeFileDir + File.separator + "user"+UUID.randomUUID().toString();
        String executeCodeFilePath  = UserCodeDepositDir + File.separator+CODE_EXECUTE_FILE;
        final File executeCodeFile = FileUtil.writeString(code, executeCodeFilePath, StandardCharsets.UTF_8);
        String compileCmd = String.format("javac -encoding utf-8 %s",executeCodeFile.getAbsoluteFile());
        try {
            final Process process = Runtime.getRuntime().exec(compileCmd);
            final ProcessExitMessage processExitMessage = processUtils.runProcessAndGetMessage(process, "编译");
        } catch (Exception e) {
            return this.getExecuteCodeResponse(e);
        }
        List<ProcessExitMessage> processExitMessageList =new ArrayList<>();
        if(inputList.isEmpty()){
            return null;
        }
        for (String input : inputList) {
            final String runCmd = String.format("java -Xmx256M -Dfile.encoding=UTF-8 -cp %s MainApplication %s", UserCodeDepositDir, input);
            try{
                final Process runProcess = Runtime.getRuntime().exec(runCmd);
                new Thread(()->{
                    try {
                        Thread.sleep(TIME_OUT);
                        final boolean alive = runProcess.isAlive();
                        if(alive){
                            runProcess.destroy();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }).start();
                final ProcessExitMessage processExitMessage = processUtils.runProcessAndGetMessage(runProcess, "运行");
                System.out.println(processExitMessage);
                processExitMessageList.add(processExitMessage);
            }catch (Exception e){
               return this.getExecuteCodeResponse(e);
            }
        }
        List<String> outputList =new ArrayList<>();
        ExecuteCodeResponse executeCodeResponse=new ExecuteCodeResponse();
        Long maxTime=0L;
        for (ProcessExitMessage processExitMessage : processExitMessageList) {
            if(processExitMessage.getErrorMessage()!=null){
                executeCodeResponse.setStatus(3);//表示程序执行失败
                break;
            }
            outputList.add(processExitMessage.getSuccessMessage());
            maxTime = Math.max(processExitMessage.getTime(),maxTime);
        }
        executeCodeResponse.setOutputList(outputList);
        if(outputList.size()==processExitMessageList.size()){
            executeCodeResponse.setStatus(2);
        }
        JudgeInfo judgeInfo=new JudgeInfo();
        judgeInfo.setTime(maxTime);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        File file=new File(UserCodeDepositDir);
        if (file.exists()){
            FileUtil.del(file);
        }
        return executeCodeResponse;
    }
    public ExecuteCodeResponse getExecuteCodeResponse(Throwable e){
        ExecuteCodeResponse executeCodeResponse=new ExecuteCodeResponse();
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        executeCodeResponse.setMessage(e.getMessage());
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setStatus(3);
        return executeCodeResponse;
    }
}
