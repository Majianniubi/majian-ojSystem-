package com.majian.springbootinit.judge.codeSendBox;

import com.majian.springbootinit.judge.codeSendBox.imp.ExampleCodeSendBox;
import com.majian.springbootinit.judge.codeSendBox.imp.RemoteCodeSendBox;
import com.majian.springbootinit.judge.codeSendBox.imp.ThirdPartCodeSendBox;

public class CodeSendBoxFactory {

    public static CodeSendBox newInstance(String type){
        switch (type){
            case "example":
                return new ExampleCodeSendBox();
            case "remote":
                return new RemoteCodeSendBox();
            case "thirdPart":
                return new ThirdPartCodeSendBox();
            default:
                return new ExampleCodeSendBox();
        }
    }
}
