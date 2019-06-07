package tangshiAnalyze.web;

import com.google.gson.Gson;
import spark.ResponseTransformer;
import spark.Spark;
import tangshiAnalyze.analyze.model.AuthorCount;
import tangshiAnalyze.analyze.model.WordCount;
import tangshiAnalyze.analyze.service.AnalyzeService;
import tangshiAnalyze.config.ObjectFactory;
import tangshiAnalyze.crawler.Crawler;


import java.util.List;

/**
 * Web API
 * 1、使用Sparkjava 框架完成了Web API 开发
 * 2、也可以使用Servlet 技术实现Web API的开发
 * 3、Java—Httpd 实现Web API（纯java语言实现的web服务器，项目）
 *      通过Socket技术实现Java-Httpd Http协议要非常清楚* Author:zhaofan
 * Created:2019/4/22
 */
public class WebController {

    private final AnalyzeService analyzeService;

    public WebController(AnalyzeService analyzeService) {
        this.analyzeService = analyzeService;
    }

    //host->http://127.0.0.1:4567/
    //->/analyze/author_count
    private List<AuthorCount> analyzeAuthorcount(){
        return analyzeService.analyzeAuthorCount();
    }

    //host->http://127.0.0.1:4567/
    //->/analyze/word_cloud
    private List<WordCount> analyzeWordCloud(){
        return analyzeService.analyzeWordCloud();
    }

    public void launch(){
        JSONResponTransformer jsonResponTransformer = new JSONResponTransformer();

        //src/main/resources/static
        //前端静态文件的目录
        Spark.staticFileLocation("/static");

        //get请求
        //服务端接口
        Spark.get("/analyze/author_count",((request, response)->analyzeAuthorcount()),jsonResponTransformer);
        Spark.get("/analyze/word_cloud",((request, response)->analyzeWordCloud()),jsonResponTransformer);
        Spark.get("/crawler/stop",((request, response) -> {
            Crawler crawler = ObjectFactory.getInstanse().getObject(Crawler.class);
            crawler.stop();
            return "停止爬虫";
        } ));
    }

    public static class JSONResponTransformer implements  ResponseTransformer{

        private Gson gson = new Gson();

        @Override
        public String render(Object o) throws Exception {
            return gson.toJson(o);
        }
    }

}
