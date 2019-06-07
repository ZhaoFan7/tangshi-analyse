package tangshiAnalyze.crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tangshiAnalyze.crawler.common.Page;
import tangshiAnalyze.crawler.parse.Parse;
import tangshiAnalyze.crawler.pipeline.Pipeline;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author:zhaofan
 * Created:2019/3/17
 */
public class Crawler {
    private final Logger logger = LoggerFactory.getLogger(Crawler.class);

    //存放文档页面（超链接)和存放详情页面
    //存放未被采集和解析的页面  page  少 htmlpage dataSet这个属性
    private final Queue<Page> docQueue = new LinkedBlockingQueue<>();

    //存放详情页面（处理完成，数据在dataSet中）
    private final Queue<Page> datailQueue = new LinkedBlockingQueue<>();

    //采集器
    private final WebClient webClient;

    //所有的解析器
    private final List<Parse> parseList = new LinkedList<>();

    //所有的清洗器（管道）
    private final List<Pipeline> pipelineList = new LinkedList<>();

    //线程调度器
    private final ExecutorService executorService;

    public Crawler(){
        this.webClient = new WebClient(BrowserVersion.CHROME);
        this.webClient.getOptions().setCssEnabled(false);
        this.webClient.getOptions().setJavaScriptEnabled(false);

        this.executorService = Executors.newFixedThreadPool(8, new ThreadFactory() {
            private  final AtomicInteger id = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("Crawler-Thread"+id.getAndIncrement());
                return thread;
            }
        });
    }

    public void start(){
        //爬取
        //解析
        this.executorService.submit(this::parse);
        //清洗
        this.executorService.submit(this::pipeline);
    }

    //停止线程池
    public void stop(){
        if(!this.executorService.isShutdown()&&this.executorService!=null){
            this.executorService.shutdown();
        }
        logger.info("Crawler stop ...");
    }

    //爬取
    public void parse(){
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Parse occur exception {} .",e.getMessage());
            }
            //当前page中只有base、path、detail----->采集后有htmlpage
            final Page page = this.docQueue.poll();
            if(page==null){
                continue;
            }
            this.executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        //采集
                        HtmlPage htmlPage = Crawler.this.webClient.getPage(page.getUrl());
                        page.setHtmlPage(htmlPage);

                        for(Parse parse:Crawler.this.parseList){
                            parse.parse(page);
                        }
                        if(!page.getDetail()){
                            Iterator<Page> iterator = page.getSubPage().iterator();
                            while(iterator.hasNext()){
                                Page subPage = iterator.next();
                                Crawler.this.docQueue.add(subPage);
                                //System.out.println(page);
                                iterator.remove();
                            }
                        }else{
                            Crawler.this.datailQueue.add(page);
                        }

                    } catch (IOException e) {
                        logger.error("Parse task occur exception {} .",e.getMessage());
                    }
                }
            });
        }
    }

    public void pipeline(){
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Pipeline occur exception {} .",e.getMessage());
            }
            final Page page = this.datailQueue.poll();
            if(page==null){
                continue;
            }
            this.executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for(Pipeline pipeline:Crawler.this.pipelineList){
                        pipeline.pipeline(page);
                    }
                }
            });
        }
    }

    public void addPage(Page page){
        this.docQueue.add(page);
    }
    public void addParse(Parse parse){
        this.parseList.add(parse);
    }
    public  void  addPipeline(Pipeline pipeline){
        this.pipelineList.add(pipeline);
    }
}
