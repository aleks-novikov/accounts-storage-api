package ru.gaz_is.common.util;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class Config {
    private static final Logger LOG = Logger.getLogger(Config.class);
    private static final Config INSTANCE = new Config();
    private String url;
    private String serverIp;
    private int socketPort;

    public String getUrl() {
        return url;
    }

    public String getServerIp() {
        return serverIp;
    }

    public int getSocketPort() {
        return socketPort;
    }

    public static Config get() {
        return INSTANCE;
    }

    private Config() {
        LOG.info("Попытка получить доступ к файлу с настройками");
        String PROPS_PATH = "../common_resources/src/main/resources/project.properties";
        File propsFile = new File(PROPS_PATH);
        if (!propsFile.exists()) {
            propsFile = new File(PROPS_PATH.substring(1));
            if (!propsFile.exists()) {
                try {
                    throw new FileNotFoundException("Файл с настройками не был найден!");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        try (InputStream is = new FileInputStream(propsFile)) {
            Properties props = new Properties();
            props.load(is);
            url = props.getProperty("db.url");
            socketPort = Integer.valueOf(props.getProperty("socket.port"));
            serverIp = props.getProperty("server.ip");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
