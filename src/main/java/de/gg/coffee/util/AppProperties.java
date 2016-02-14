package de.gg.coffee.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ========== AppProperties.java ==========
 * <p>
 * $Revision: 47 $<br/>
 * $Author: Olezha $<br/>
 * $HeadURL: file:///D:/work/local_repository/tetris/trunk/src/main/java/oov/tetris/AppProperties.java $<br/>
 * $Id: AppProperties.java 47 2014-07-11 12:25:33Z Olezha $
 * <p>
 * 03.09.13 18:00: Original version (OOBUKHOV)<br/>
 */
public class AppProperties {


    private static volatile Properties properties;

    public static String get(String key) {
        if (properties == null) {
            synchronized (AppProperties.class) {
                if (properties == null) {
                    InputStream inputStream = AppProperties.class.getClassLoader().getResourceAsStream("application.properties");
                    properties = new Properties();
                    try {
                        properties.load(inputStream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return properties.getProperty(key);
    }

    public static String get(String key, String default_) {
        String r = get(key);
        if (r == null) {
            r = default_;
        }
        return r;
    }

}
