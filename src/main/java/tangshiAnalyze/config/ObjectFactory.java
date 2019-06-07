package tangshiAnalyze.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ResponseTransformer;
import spark.Spark;
import tangshiAnalyze.analyze.dao.AnalyzeDao;
import tangshiAnalyze.analyze.dao.implement.AnalyzeDaoImplement;
import tangshiAnalyze.analyze.service.AnalyzeService;
import tangshiAnalyze.analyze.service.AnalyzeServiceImplement;
import tangshiAnalyze.crawler.Crawler;
import tangshiAnalyze.crawler.common.Page;
import tangshiAnalyze.crawler.parse.DataPageParse;
import tangshiAnalyze.crawler.parse.DocumentParse;
import tangshiAnalyze.crawler.pipeline.ConsolePipeline;
import tangshiAnalyze.crawler.pipeline.DatabasePipeline;
import tangshiAnalyze.web.WebController;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Author:zhaofan
 * Created:2019/4/22
 */
public final class ObjectFactory {
    private final Logger logger = LoggerFactory.getLogger(ObjectFactory.class);
    //工厂对象：单例饿汉式
    private static final ObjectFactory instanse = new ObjectFactory();
    //存放所有对象
    private final Map<Class,Object> objectHashMap = new HashMap<>();

    private ObjectFactory(){
        //初始化配置对象
        initConfigProperties();

        //数据源对象
        initDataSource();

        //爬虫对象
        initCrawler();

        //Web对象
        initWebContorller();

        //对象清单打印输出
        printObject();
    }

    private void initWebContorller() {
        DataSource dataSource = getObject(DataSource.class);
        AnalyzeDao analyzeDao = new AnalyzeDaoImplement(dataSource);
        AnalyzeService analyzeService = new AnalyzeServiceImplement(analyzeDao);
        WebController webController = new WebController(analyzeService);
        objectHashMap.put(WebController.class,webController);
    }

    private void initCrawler() {
        ConfigProperties configProperties = getObject(ConfigProperties.class);
        DataSource dataSource = getObject(DataSource.class);
        final Page page = new Page(configProperties.getCrawlerBase(),configProperties.getCrawlerPath(),configProperties.isCrawlerDetail());
        Crawler crawler = new Crawler();
        crawler.addParse(new DocumentParse());
        crawler.addParse(new DataPageParse());

        if(configProperties.isEnableConsole()){
            crawler.addPipeline(new ConsolePipeline());
        }
        crawler.addPipeline(new DatabasePipeline(dataSource));
        crawler.addPage(page);
        objectHashMap.put(Crawler.class,crawler);
    }

    private void initDataSource() {
        ConfigProperties configProperties = getObject(ConfigProperties.class);
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(configProperties.getDbUserName());
        dataSource.setPassword(configProperties.getDbPassword());
        dataSource.setDriverClassName(configProperties.getDbDriverClass());
        dataSource.setUrl(configProperties.getDbUrl());
        objectHashMap.put(DataSource.class,dataSource);
    }

    private void initConfigProperties() {
        ConfigProperties configProperties = new ConfigProperties();
        objectHashMap.put(ConfigProperties.class,configProperties);
        logger.info("ConfigProperties info:\n{}",configProperties.toString());
    }

    public  <T> T getObject(Class classz){
        if(!objectHashMap.containsKey(classz)){
            throw new IllegalArgumentException("Class"+classz.getName()+"not foud");
        }
        return (T) objectHashMap.get(classz);
    }

    public static ObjectFactory getInstanse(){
        return  instanse;
    }

    private void printObject(){
        logger.info("=============这是对象工厂list===================");
        for(Map.Entry<Class,Object> entry : objectHashMap.entrySet()){
            logger.info(String.format("[%-5s]====>[%s]",entry.getKey().getCanonicalName(),entry.getValue().getClass().getCanonicalName()));
        }
        logger.info("================================================\n");
    }


}
