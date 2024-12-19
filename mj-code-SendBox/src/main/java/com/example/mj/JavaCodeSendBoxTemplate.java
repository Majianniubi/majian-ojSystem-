package com.example.mj;

import cn.hutool.core.io.FileUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.example.mj.Utils.processUtils;
import com.example.mj.model.ExecuteCodeRequest;
import com.example.mj.model.ExecuteCodeResponse;
import com.example.mj.model.JudgeInfo;
import com.example.mj.model.ProcessExitMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Slf4j
public abstract class JavaCodeSendBoxTemplate implements CodeSendBox{
    private Long TIME_OUT = 5000L;
    private String CODE_DEPOSIT_DIR = "temCode";

    private String CODE_EXECUTE_FILE = "MainApplication.java";
    private String[] BLACK_WORD={"file","exec"};
    public File createFile(String code){
        final String systemPath = System.getProperty("user.dir");
        String executeCodeFileDir= systemPath + File.separator + CODE_DEPOSIT_DIR;
        if(!FileUtil.exist(executeCodeFileDir)){
            FileUtil.mkdir(executeCodeFileDir);
        }
        String UserCodeDepositDir = executeCodeFileDir + File.separator + "user"+ UUID.randomUUID().toString();
        String executeCodeFilePath  = UserCodeDepositDir + File.separator+CODE_EXECUTE_FILE;
        final File executeCodeFile = FileUtil.writeString(code, executeCodeFilePath, StandardCharsets.UTF_8);
        return executeCodeFile;
    }
    public ProcessExitMessage compilerFile(File executeCodeFile){
        String compileCmd = String.format("javac -encoding utf-8 %s",executeCodeFile.getAbsoluteFile());
        try {
            final Process process = Runtime.getRuntime().exec(compileCmd);
            final ProcessExitMessage processExitMessage = processUtils.runProcessAndGetMessage(process, "编译");
            return processExitMessage;
        } catch (Exception e) {
            throw new RuntimeException("编译错误"+e.getMessage());
        }
    }
    public  List<ProcessExitMessage>  runFIle(List<String> inputList,File executeCodeFile){
        final String UserCodeDepositDir = executeCodeFile.getParentFile().getAbsolutePath();
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
                            log.error("错误代码时间过长");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }).start();
                final ProcessExitMessage processExitMessage = processUtils.runProcessAndGetMessage(runProcess, "运行");
                processExitMessageList.add(processExitMessage);
            }catch (Exception e){
                throw new RuntimeException("执行异常"+e.getMessage());
            }
        }
        return processExitMessageList;
    }
    public ExecuteCodeResponse getExecuteCodeResponse(Throwable e){
        ExecuteCodeResponse executeCodeResponse=new ExecuteCodeResponse();
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        executeCodeResponse.setMessage(e.getMessage());
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setStatus(3);
        return executeCodeResponse;
    }
    public boolean deleteFile(File file){
        final File parentFile = file.getParentFile();
        if (parentFile.exists()){
            FileUtil.del(parentFile);
            return true;
        }else {
            return true;
        }
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
        //用户代码封装成文件
        final File executeCodeFile = this.createFile(code);
        //编译文件
        final ProcessExitMessage Message = this.compilerFile(executeCodeFile);
        //运行文件
        final List<ProcessExitMessage> processExitMessageList = this.runFIle(inputList,executeCodeFile);
        //删除文件
        final boolean result = deleteFile(executeCodeFile);
        List<String> outputList =new ArrayList<>();
        ExecuteCodeResponse executeCodeResponse=new ExecuteCodeResponse();
        Long maxTime=0L;
        Long maxMemory = 0L;
        for (ProcessExitMessage processExitMessage : processExitMessageList) {
            if(processExitMessage.getErrorMessage()!=null){
                executeCodeResponse.setStatus(3);//表示程序执行失败
                break;
            }
            outputList.add(processExitMessage.getSuccessMessage());
            maxTime = Math.max(processExitMessage.getTime(),maxTime);
            maxMemory = Math.max(processExitMessage.getMemory(), maxMemory);
        }
        executeCodeResponse.setOutputList(outputList);
        if(outputList.size()==processExitMessageList.size()){
            executeCodeResponse.setStatus(2);
        }
        JudgeInfo judgeInfo=new JudgeInfo();
        judgeInfo.setTime(maxTime);
        judgeInfo.setMemory(maxMemory);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
