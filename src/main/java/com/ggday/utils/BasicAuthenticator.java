package com.ggday.utils;

import javax.annotation.Nullable;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class BasicAuthenticator extends Authenticator {

    private String login = SysProperties.getProperty(SysProperties.SITE_LOGIN);
    private String password = SysProperties.getProperty(SysProperties.SITE_PASSWORD);


    public BasicAuthenticator(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public BasicAuthenticator() { }

    @Nullable
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        if ((login != null) || (password != null)) {
            return new PasswordAuthentication(login, password.toCharArray());
        }
        return null;
    }
}
