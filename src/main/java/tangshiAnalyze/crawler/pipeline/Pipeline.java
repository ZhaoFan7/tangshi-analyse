package tangshiAnalyze.crawler.pipeline;

import tangshiAnalyze.crawler.common.Page;

/**
 * Author:zhaofan
 * Created:2019/3/17
 */
public interface Pipeline {
    //管道处理page中的数据
    void pipeline(final Page page);
}
