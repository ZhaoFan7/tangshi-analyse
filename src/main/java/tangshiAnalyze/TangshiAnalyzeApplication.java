package tangshiAnalyze;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tangshiAnalyze.config.ObjectFactory;
import tangshiAnalyze.crawler.Crawler;
import tangshiAnalyze.web.WebController;

/**
 * 唐诗分析程序的主类
 * Author:zhaofan
 * Created:2019/4/3
 */
public class TangshiAnalyzeApplication {
    //使用日志，消除System.out.*
    private static final Logger LOGGER = LoggerFactory.getLogger(TangshiAnalyzeApplication.class);

    public static void main(String[] args){
        //是否启动爬虫
        if(args.length==1&&args[0].equals("run-crawler")){
            Crawler crawler = ObjectFactory.getInstanse().getObject(Crawler.class);
            LOGGER.info("Crawler started ...");
            crawler.start();
        }
        //运行了web服务，提供接口
        WebController webController = ObjectFactory.getInstanse().getObject(WebController.class);
        LOGGER.info("Web Server launch ...");
        webController.launch();
    }
}
