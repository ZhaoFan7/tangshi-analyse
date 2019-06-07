package tangshiAnalyze.crawler.pipeline;

import tangshiAnalyze.crawler.common.Page;
import java.util.Map;

/**
 * Author:zhaofan
 * Created:2019/4/5
 */
public class ConsolePipeline implements Pipeline {

    @Override
    public void pipeline(final Page page) {
        Map<String,Object> data = page.getDataSet().getData();
        //存储
        System.out.println(data);
    }
}
