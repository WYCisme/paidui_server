package com.jiaohao.jiaohao_server.websocket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
/**
 * @projectName: jiaohao_server
 * @className: WebSocketConfig
 * @author: WenYichong
 * @date: 2020/7/30-22:07
 * @description: 连我自己都不知道在写什么......
 */
@Configuration

public class WebSocketConfig {


    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
