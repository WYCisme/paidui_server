package com.jiaohao.jiaohao_server.controllor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiaohao.jiaohao_server.bean.RespBean;
import com.jiaohao.jiaohao_server.websocket.MyWebsocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.websocket.Session;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @projectName: jiaohao_server
 * @className: paidui
 * @author: WenYichong
 * @date: 2020/7/30-21:47
 * @description: 连我自己都不知道在写什么......
 */

@RestController
@Slf4j
@RequestMapping("/api")

public class paiduiControllor {
    public static BlockingDeque<String> duiwu = new LinkedBlockingDeque<>(); //队伍
    public static Map<String,Boolean> windows =null ; //窗口数据
    public static AtomicInteger no = new AtomicInteger(0);


    @RequestMapping("/test")
    @ResponseBody
    public RespBean test(){
        log.info("客户端连接测试成功");
        return RespBean.ok("连接成功！");
    }

    @RequestMapping("/windowInit")
    public RespBean windowInit(@RequestBody String str){
        if (windows == null) {
            windows = new HashMap<>();
            JSONArray jbs = JSONObject.parseArray(str);
            for (Object it : jbs) {
                JSONObject jb = (JSONObject) it;
                windows.put(jb.getString("num"), jb.getBoolean("isSelected"));
            }
        }
        return RespBean.ok("窗口数据 " ,mapStr(windows));
    }

    private JSONArray mapStr(Map<String,Boolean> map){
        JSONArray ress= new JSONArray();
        for (String k:windows.keySet()){
            JSONObject res = new JSONObject();
            res.put("num",k);
            res.put("isSelected",windows.get(k));
            ress.add(res);
        }
        return ress;
    }

    @RequestMapping("/paiduiData")
    public RespBean paiduiData(){
        RespBean res = RespBean.ok("排队数据 " ,duiwu.toArray());
        return res;
    }
    @RequestMapping("/quhao")
    public RespBean quhao(){
        String cradID = "A" + String.format("%03d", no.addAndGet(1) );
        try {
            duiwu.put(cradID);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RespBean res = RespBean.ok(cradID,"quhao" ,duiwu.toArray());

        //给客户端发
        try {
            MyWebsocket.sendMsg(res,MyWebsocket.session_quhao);//取号端
            for(Map.Entry<String, Session> entry : MyWebsocket.sessions_jiaohao.entrySet()){
//                String n = entry.getKey();
                Session ss = entry.getValue();
                MyWebsocket.sendMsg(res,ss);//叫号端
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    @RequestMapping("/jiaohao/{windowName}")
    public RespBean jiaohao(@PathVariable String windowName){
        RespBean res = null;
        try {
            String s = duiwu.poll();
            res = RespBean.ok("请"+s + "号到" + windowName + "办理业务","jiaohao", duiwu.toArray());

            //给客户端发
             MyWebsocket.sendMsg(res,MyWebsocket.session_quhao);//取号端
            for(Map.Entry<String, Session> entry : MyWebsocket.sessions_jiaohao.entrySet()){
                Session ss = entry.getValue();
                MyWebsocket.sendMsg(res,ss);//叫号端
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }
}
