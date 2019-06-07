package tangshiAnalyze.analyze.service;

import tangshiAnalyze.analyze.model.AuthorCount;
import tangshiAnalyze.analyze.model.WordCount;

import java.util.List;

/**
 * Author:zhaofan
 * Created:2019/4/19
 */
public interface AnalyzeService {
    //分析唐诗中作者的创作数量
    List<AuthorCount> analyzeAuthorCount();
    //词云分析
    List<WordCount> analyzeWordCloud();
}
