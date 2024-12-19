package com.majian.springbootinit.endPoint;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.majian.springbootinit.common.ErrorCode;
import com.majian.springbootinit.config.MyEndpointConfigurator;
import com.majian.springbootinit.exception.BusinessException;
import com.majian.springbootinit.model.entity.Comment;
import com.majian.springbootinit.model.entity.User;
import com.majian.springbootinit.model.vo.CommentVo;
import com.majian.springbootinit.service.CommentService;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.majian.springbootinit.constant.UserConstant.USER_LOGIN_STATE;

@ServerEndpoint(value = "/ws/sc", configurator = MyEndpointConfigurator.class)
@Component
public class WsServerPoint {
    private static Map<Long, Session> webSessionMap;
    private static Map<String, Long> webSessionIdMap;

    static {
        webSessionIdMap = new ConcurrentHashMap<>();
        webSessionMap = new ConcurrentHashMap<>();
    }

    @OnOpen
    public void OnOpen(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        User user = (User) httpSession.getAttribute(USER_LOGIN_STATE);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        webSessionIdMap.put(session.getId(), user.getId());
        webSessionMap.put(user.getId(), session);
    }

    @OnMessage
    public void OnMessage(String message, Session session) {
        webSessionMap.forEach((key, value) -> {
            System.out.println(key);
            final Session sessionExist = findSession(key);
            try {
                sessionExist.getBasicRemote().sendText(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Session findSession(Long userId) {
        final boolean flag = webSessionMap.containsKey(userId);
        if (flag) {
            final Session session = webSessionMap.get(userId);
            return session;
        }
        return null;
    }

    @OnError
    public void OnError(Throwable error) {

    }

    @OnClose
    public void OnClose(Session session) {
        if (session != null) {
            final Long userId = webSessionIdMap.remove(session.getId());
            webSessionMap.remove(userId);
        }
    }
}
