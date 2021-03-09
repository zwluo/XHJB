package com.maoqi.xhjb.controller;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ServerEndpoin 注解声明该类为 WebSocket 的服务端端点
 * value 值为监听客户端访问的 URL
 */
@Component
@ServerEndpoint(value = "/page_room/{username}")
public class WsByTomcat {

    //这里只是简单测试用，真正的场景请考虑线程安全的容器或其它并发解决方案
    private static List<Session> sessions = new ArrayList<>();

    /**
     * @param session  与客户端的会话对象【可选】
     * @param username 路径参数值 【可选】
     * @throws IOException
     * @OnOpen 标注此方法在 ws 连接建立时调用，可用来处理一些准备性工作 可选参数
     * EndpointConfig（端点配置信息对象） Session 连接会话对象
     */
    @OnOpen
    public void OnOpen(Session session, @PathParam("username") String username) throws IOException {
        sessions.add(session);
        //sendTextMsg("好友【" + username + "】加入群聊");
    }

    /**
     * @param msg      接受客户端消息
     * @param username RESTful 路径方式获取用户名
     * @throws IOException
     * @OnMessage 在收到客户端消息调用 消息形式不限于文本消息，还可以是二进制消息(byte[]/ByteBuffer等)，ping/pong 消息
     */
    @OnMessage
    public void OnMsg(String msg, @PathParam("username") String username) throws IOException {
        sendTextMsg("" + username + ":" + msg);
    }


    /**
     * @OnClose 连接关闭调用
     */
    @OnClose
    public void OnClose(Session session, @PathParam("username") String username) throws IOException {
        sessions.remove(session);
        //sendTextMsg("好友【"+username + "】退出群聊");
    }

    /**
     * @param e 异常参数
     * @OnError 连接出现错误调用
     */
    @OnError
    public void OnError(Throwable e) {
        e.printStackTrace();
    }

    private void sendTextMsg(String msg) {
        for (Session session : sessions) {
            session.getAsyncRemote().sendText(msg);
        }
    }
}