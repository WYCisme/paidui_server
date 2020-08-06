package com.jiaohao.jiaohao_server.websocket;

import lombok.Data;

/**
 * @projectName: jiaohao_server
 * @className: websocketModel
 * @author: WenYichong
 * @date: 2020/7/30-22:58
 * @description: 连我自己都不知道在写什么......
 */
@Data
public class websocketModel {
    //想前端发送哪些消息的实体类
    private String type;
    private String username;
    private String nickName;
    private String password;
    private String  avatar;
    private String uid;
    private String authToken;
    private String roleType;
    private String organizationName;
    private String organizationCode;
    private String auditStatus;
    private String  gpmsSchoolYear;
    private String  organization;
    private String  roleGroup;

}
