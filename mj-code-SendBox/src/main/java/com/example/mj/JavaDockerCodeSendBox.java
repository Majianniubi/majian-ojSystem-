package com.example.mj;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.ArrayUtil;
import com.example.mj.model.ExecuteCodeRequest;
import com.example.mj.model.ExecuteCodeResponse;
import com.example.mj.model.ProcessExitMessage;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class JavaDockerCodeSendBox extends JavaCodeSendBoxTemplate {
    private Long TIME_OUT = 5000L;


    private static boolean FIRST_INIT = true;
    @Override
    public List<ProcessExitMessage> runFIle(List<String> inputList, File executeCodeFile) {
        String UserCodeDepositDir = executeCodeFile.getParentFile().getAbsolutePath();
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        String image = "openjdk:8-alpine";
        try {
            if (FIRST_INIT) {
                PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
                PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
                    @Override
                    public void onNext(PullResponseItem item) {
                        System.out.println("下载状态" + item.getStatus());
                        super.onNext(item);
                    }
                };
                pullImageCmd.exec(pullImageResultCallback).awaitCompletion();
                FIRST_INIT = false;
            }
        } catch (Exception e) {
            System.out.println("拉取镜像失败");
            e.printStackTrace();
        }
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);
        HostConfig hostConfig = new HostConfig();
        hostConfig.setBinds(new Bind(UserCodeDepositDir, new Volume("/app")));
        hostConfig.withMemory(100 * 1000 * 1000L);
        hostConfig.withCpuCount(1L);
        CreateContainerResponse createContainerResponse = containerCmd
                .withHostConfig(hostConfig)
                .withAttachStdin(true)
                .withAttachStderr(true)
                .withAttachStdout(true)
                .withNetworkDisabled(true)
                .withTty(true).exec();
        String containerId = createContainerResponse.getId();
        dockerClient.startContainerCmd(containerId).exec();
        List<ProcessExitMessage> processExitMessageList = new ArrayList<>();
        for (String input : inputList) {
            String[] inputArray = input.split(" ");
            String[] execCmd = ArrayUtil.append(new String[]{"java", "-cp", "/app", "MainApplication"}, inputArray);
            ExecCreateCmd execCreateCmd = dockerClient.execCreateCmd(containerId);
            ExecCreateCmdResponse execCreateCmdResponse = execCreateCmd
                    .withCmd(execCmd)
                    .withAttachStderr(true)
                    .withAttachStdin(true)
                    .withAttachStdout(true).exec();
            String createCmdResponseId = execCreateCmdResponse.getId();
            final String[] errorMessage = new String[1];
            final String[] successMessage = new String[1];
            boolean isTimeOut = true;
            ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback() {
                @Override
                public void onComplete() {
                    super.onComplete();
                }

                @Override
                public void onNext(Frame frame) {

                    StreamType streamType = frame.getStreamType();
                    if (streamType.STDERR.equals(streamType)) {
                        errorMessage[0] = new String(frame.getPayload());
                        System.out.println("输出错误结果" + new String(frame.getPayload()));
                    } else {
                        successMessage[0] = new String(frame.getPayload());
                        System.out.println("输出正常结果" + new String(frame.getPayload()));
                    }
                    super.onNext(frame);
                }
            };
            StopWatch stopWatch = new StopWatch();
            final Long[] memoryMax = {0L};
            StatsCmd statsCmd = dockerClient.statsCmd(containerId);
            ResultCallback<Statistics> statisticsResultCallback = new ResultCallback<Statistics>() {
                @Override
                public void onStart(Closeable closeable) {

                }

                @Override
                public void onNext(Statistics statistics) {
                    Long usage = statistics.getMemoryStats().getUsage();
                    memoryMax[0] = Math.max(statistics.getMemoryStats().getUsage(), memoryMax[0]);
                    System.out.println("内存" + memoryMax[0]);
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {

                }

                @Override
                public void close() throws IOException {

                }
            };
            statsCmd.exec(statisticsResultCallback);
            try {
                stopWatch.start();
                dockerClient.execStartCmd(createCmdResponseId).exec(execStartResultCallback).awaitCompletion(TIME_OUT, TimeUnit.SECONDS);
                stopWatch.stop();
                statsCmd.close();
            } catch (InterruptedException e) {
                throw  new RuntimeException("运行错误"+e.getMessage());
            }
            long time = stopWatch.getLastTaskTimeMillis();
            ProcessExitMessage processExitMessage = new ProcessExitMessage();
            processExitMessage.setErrorMessage(errorMessage[0]);
            processExitMessage.setSuccessMessage(successMessage[0]);
            processExitMessage.setTime(time);
            processExitMessage.setMemory(memoryMax[0]);
            processExitMessageList.add(processExitMessage);
            dockerClient.removeContainerCmd(containerId).withForce(true).exec();
        }
        return processExitMessageList;
    }
    @Override
    public ExecuteCodeResponse execute(ExecuteCodeRequest executeCodeRequest) {
        return super.execute(executeCodeRequest);
    }
}
