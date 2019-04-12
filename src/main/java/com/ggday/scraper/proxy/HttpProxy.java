package com.ggday.scraper.proxy;

public class HttpProxy {
    private String ip;
    private int port;
    private String username;
    private String password;

    public HttpProxy(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public HttpProxy(String ip, int port, String username, String password) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        String str = "proxy:http://";
        if (username != null || password != null) {
            str += username + ":" + password + "@";
        }
        str += ip + ":" + port + "/";
        return str;
    }
}
