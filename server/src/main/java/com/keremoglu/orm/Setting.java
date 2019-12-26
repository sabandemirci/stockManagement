package com.keremoglu.orm;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "SETTING")
public class Setting extends BaseEntity {

    @Basic
    @Column(name = "mail_server_host")
    private String mailServer;

    @Basic
    @Column(name = "mail_port")
    private Integer mailPort;
    @Basic
    @Column(name = "appName")
    private String appName;

    public String getMailServer() {
        return mailServer;
    }

    public void setMailServer(String mailServer) {
        this.mailServer = mailServer;
    }

    public Integer getMailPort() {
        return mailPort;
    }

    public void setMailPort(Integer mailPort) {
        this.mailPort = mailPort;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public Map toMap() {
        Map map = new HashMap();
        map.put("mailServer", mailServer);
        map.put("mailPort", mailPort);
        map.put("appName", appName);
        return map;
    }
}