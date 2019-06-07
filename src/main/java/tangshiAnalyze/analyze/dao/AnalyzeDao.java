package tangshiAnalyze.analyze.dao;

import tangshiAnalyze.analyze.entity.Poetry;
import tangshiAnalyze.analyze.model.AuthorCount;

import java.util.List;

/**
 * Author:zhaofan
 * Created:2019/4/19
 */
public interface AnalyzeDao {

    //分析唐诗中作者的创作数量
    List<AuthorCount> analyzeAuthorCount();

    //查询所有的诗文，提供给业务层进行行分析
    List<Poetry> queryAllPoetryInfo();
}
