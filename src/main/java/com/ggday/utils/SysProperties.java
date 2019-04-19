package com.ggday.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.Properties;

public class SysProperties {
    static {
        Properties prop = System.getProperties();
        try {
            FileInputStream input = new FileInputStream(new File("src/main/resources/config.properties"));
            prop.load(new InputStreamReader(input, Charset.forName("cp1251")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static final String SITE_LOGIN = "site.login";
    public static final String SITE_PASSWORD = "site.password";

    public static String getProperty(String name) {
        return Optional.ofNullable(System.getProperty(name))
                .orElseThrow(() -> new NullPointerException("Property '%s' is not found"));
    }
}
