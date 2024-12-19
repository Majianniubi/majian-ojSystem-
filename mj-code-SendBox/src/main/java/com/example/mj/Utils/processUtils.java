package com.example.mj.Utils;

import cn.hutool.core.date.StopWatch;
import com.example.mj.model.ProcessExitMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class processUtils {
    public static ProcessExitMessage runProcessAndGetMessage(Process process, String processType) {
        Runtime runtime =Runtime.getRuntime();
        long startTotalMemory = runtime.totalMemory();
        ProcessExitMessage processExitMessage = new ProcessExitMessage();
        try {
            StopWatch stopWatch=new StopWatch();
            stopWatch.start();
            final Integer exitCode = process.waitFor();
            processExitMessage.setExitCode(exitCode);
            if (exitCode.equals(0)) {
                System.out.println(processType+"成功" + exitCode);
                StringBuilder successMessageBuilder = new StringBuilder();
                final BufferedReader successBufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String successResult;
                while ((successResult = successBufferedReader.readLine()) != null) {
                    successMessageBuilder.append(successResult);
                }
                processExitMessage.setSuccessMessage(successMessageBuilder.toString());
                successBufferedReader.close();
                process.getInputStream().close();
                process.destroy();
            } else {
                System.out.println(processType+"失败 错误码:" + exitCode);
                StringBuilder errorMessageBuilder = new StringBuilder();
                final BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String errorResult;
                while ((errorResult = errorBufferedReader.readLine()) != null) {
                    errorMessageBuilder.append(errorResult);
                }
                processExitMessage.setErrorMessage(errorMessageBuilder.toString());
                errorBufferedReader.close();
                process.getErrorStream().close();
                process.destroy();
            }

            stopWatch.stop();
            final long lastTaskTimeMillis = stopWatch.getLastTaskTimeMillis();
            processExitMessage.setTime(lastTaskTimeMillis);
            processExitMessage.setMemory(startTotalMemory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return processExitMessage;
    }
}
