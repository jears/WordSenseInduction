package org.kde9.util.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kde9.util.fileOperation.ReadFile;
import org.kde9.util.fileOperation.WriteFile;

/**
 * 配置文件类
 * <p />
 * 配置由key、value对组成，并以key、value对的形式保存在文件中。
 * 其中key为String类型，value可以为String、int、boolean等类型。
 * <p />
 * 通过该类提供的若干静态方法，可以存储和读取这些配置。
 * <p />
 * <strong>
 * 注意：
 * <br />
 * 1. 使用该类的其他方法前，必须首先调用{@link #init}方法指定配置文件的路径。
 * <br />
 * 2. key相同的配置，即使value不同（包括值不同和类型不同），也会被视为同一个配置而导致相互覆盖。
 * </strong>
 */
public class Config {

    private static Configuration configuration;

    /**
     * 初始化Config类，并设定配置文件的路径。使用该类时必须首先调用该方法。
     * @param configFilePath 配置文件的路径
     */
    public static void init(String configFilePath) {
        Config.configuration = new Configuration(configFilePath);
    }

    /**
     * 获取名称为key的配置的String值。
     * @param key 配置的名称
     * @param defaultValue 配置的默认值
     * @return 当配置文件中存在以key为名称的配置时，返回该配置的值；若配置文件中不存在以key为名称的配置，则返回默认值
     */
    public static String get(String key, String defaultValue) {
        String ret = configuration.getConfig(key);
        if (ret == null) {
            set(key, defaultValue);
            return defaultValue;
        }
        return ret;
    }

    /**
     * 获取名称为key的配置的boolean值。
     * @param key 配置的名称
     * @param defaultValue 配置的默认值
     * @return 当配置文件中存在以key为名称的配置时，返回该配置的值；若配置文件中不存在以key为名称的配置，则返回默认值
     */
    public static boolean get(String key, boolean defaultValue) {
        String ret = get(key, String.valueOf(defaultValue));
        return Boolean.valueOf(ret);
    }

    /**
     * 获取名称为key的配置的int值。
     * @param key 配置的名称
     * @param defaultValue 配置的默认值
     * @return 当配置文件中存在以key为名称的配置时，返回该配置的值；若配置文件中不存在以key为名称的配置，则返回默认值
     */
    public static int get(String key, int defaultValue) {
        String ret = get(key, String.valueOf(defaultValue));
        return Integer.valueOf(ret);
    }

    /**
     * 设置名称为key的配置的String类型的值。
     * @param key 配置的名称
     * @param value 配置的值
     */
    public static void set(String key, String value) {
        configuration.setConfig(key, value);
    }

    /**
     * 设置名称为key的配置的boolean类型的值。
     * @param key 配置的名称
     * @param value 配置的值
     */
    public static void set(String key, boolean value) {
        configuration.setConfig(key, String.valueOf(value));
    }

    /**
     * 设置名称为key的配置的int类型的值。
     * @param key 配置的名称
     * @param value 配置的值
     */
    public static void set(String key, int value) {
        configuration.setConfig(key, String.valueOf(value));
    }
}

class Configuration implements Constants {

    private String configFilePath;
    private LinkedHashMap<String, String> config;
    private ReadFile rf;
    private WriteFile wf;

    public Configuration(String configFilePath) {
        this.configFilePath = configFilePath;
        config = new LinkedHashMap<String, String>();
        try {
            rf = new ReadFile(configFilePath, "UTF-8");
            String item = "", content = "";
            while (true) {
                item = rf.readLine();
                while (item != null && item.length() == 0) {
                    item = rf.readLine();
                }
                content = rf.readLine();
                while (content != null && content.length() == 0) {
                    content = rf.readLine();
                }
                if (item == null || content == null) {
                    break;
                }
                config.put(item, content);
            }
            rf.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.WARNING,
                    "Configuration[FileNotFoundException], configFilePath[" + configFilePath + "]", ex);
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE,
                    "Configuration[IOException], configFilePath[" + configFilePath + "]", ex);
        }
    }

    public String getConfig(String item) {
        return config.get(item);
    }

    public boolean setConfig(String item, String value) {
        config.put(item, value);
        if (save()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean save() {
        try {
            wf = new WriteFile(configFilePath, false, UTF_8);
            String temp = new String();
            for (String item : config.keySet()) {
                temp += item;
                temp += NEW_LINE;
                temp += config.get(item);
                temp += NEW_LINE;
                temp += NEW_LINE;
            }
            wf.write(temp);
            wf.close();
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE,
                    "save[IOException], configFilePath[" + configFilePath + "]", ex);
        }
        return true;
    }
}
