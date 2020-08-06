package com.jiaohao.jiaohao_server.websocket;


import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiaohao.jiaohao_server.bean.RespBean;
import com.jiaohao.jiaohao_server.controllor.paiduiControllor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: jiaohao_server
 * @className: updata
 * @author: WenYichong
 * @date: 2020/7/30-22:02
 * @description: 连我自己都不知道在写什么......
 */


@Component
@ServerEndpoint(value = "/websocket/{clientId}")
public class MyWebsocket {


    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd ");



    public static Session session_quhao; //当前排队端session
//    public static Session session_jiaohao; //当前叫号端session
    public static Map<String , Session> sessions_jiaohao = new HashMap<>();//当前叫号端sessions,多个叫号端

    //给客户端发送消息
    synchronized public static void sendMsg(RespBean resp , Session session) throws IOException {

        session.getBasicRemote().sendText(JSON.toJSONString(resp));

    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen( Session session, @PathParam("clientId") String clientId) {


        if (clientId.equals("quhao")){//取号
            System.out.println("取号端连接成功！"  );

            session_quhao = session;//保存唯一session
        }else if (clientId.startsWith("jiaohao") && clientId.endsWith("窗口")){ //叫号
            String windowName = clientId.split("_")[1];
            System.out.println("叫号端连接成功！"  + "当前窗口："+ windowName);

            sessions_jiaohao.put(windowName,session);
            paiduiControllor.windows.put(windowName,true);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        System.out.println("有一连接关闭" + session);

        for(Map.Entry<String,Session> s : sessions_jiaohao.entrySet()){
            if (s.getValue().equals(session)){
                sessions_jiaohao.remove(s.getKey(),s.getValue());
                paiduiControllor.windows.put(s.getKey(),false);
                break;
            }
        }

        MyWebsocket.numToFile("todayNum.txt", formatter.format(new Date()) + paiduiControllor.no.toString());//保存今天的人数


    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject jb = JSON.parseObject(message);
        System.out.println("来自客户端的消息:" + jb);

    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }


    public static void numToFile(String fileName, String content) {
        try {
// 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
// 文件长度，字节数
            long fileLength = randomFile.length();
// 将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content+"\r\n");
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
