package tangshiAnalyze.config;

import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Author:zhaofan
 * Created:2019/4/18
 */
@Data
public class ConfigProperties {
    private String crawlerBase;
    private String crawlerPath;
    private boolean crawlerDetail;

    private String dbUserName;
    private String dbPassword;
    private String dbUrl;
    private String dbDriverClass;

    private boolean enableConsole;

    public ConfigProperties(){
        //从外部文件加载
        InputStream inputStream = ConfigProperties.class.getClassLoader().getResourceAsStream("config.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.crawlerBase = String.valueOf(properties.get("crawler.base"));
        this.crawlerPath = String.valueOf(properties.get("crawler.path"));
        this.crawlerDetail = Boolean.parseBoolean(String.valueOf(properties.get("crawler.detail")));
        this.dbUserName = String.valueOf(properties.getProperty("db.username"));
        this.dbPassword = String.valueOf(properties.get("db.password"));
        this.dbDriverClass = String.valueOf(properties.get("db.driver_class"));
        this.dbUrl = String.valueOf(properties.get("db.url"));
        this.enableConsole = Boolean.valueOf(String.valueOf(properties.getProperty("config.enable_console","false")));
    }


}
