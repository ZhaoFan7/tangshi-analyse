package tangshiAnalyze.analyze.service;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import tangshiAnalyze.analyze.dao.AnalyzeDao;
import tangshiAnalyze.analyze.entity.Poetry;
import tangshiAnalyze.analyze.model.AuthorCount;
import tangshiAnalyze.analyze.model.WordCount;

import java.util.*;

/**
 * Author:zhaofan
 * Created:2019/4/19
 */
public class AnalyzeServiceImplement implements AnalyzeService {

    private final AnalyzeDao analyzeDao;

    public AnalyzeServiceImplement(AnalyzeDao analyzeDao) {
        this.analyzeDao = analyzeDao;
    }

    @Override
    public List<AuthorCount> analyzeAuthorCount() {
        List<AuthorCount> authorCounts = analyzeDao.analyzeAuthorCount();
        //排序:按照count升序
        authorCounts.sort(Comparator.comparing(AuthorCount::getCount));
        return authorCounts;
    }

    @Override
    public List<WordCount> analyzeWordCloud() {
        //1、查询出所有数据
        //2、取出 title和content
        //3、分词:过滤/w null空 length<2
        //4、统计 k-v k是词，v是词频
        List<Poetry> poetries = analyzeDao.queryAllPoetryInfo();

        Map<String,Integer> map = new HashMap<>();
        for(Poetry poetry : poetries) {
            List<Term> terms = new ArrayList<>();
            String title = poetry.getTitle();
            String content = poetry.getContent();
            terms.addAll(NlpAnalysis.parse(title).getTerms());
            terms.addAll(NlpAnalysis.parse(content).getTerms());
            //过滤 ArrayList是并发修改的，不能用for循环过滤并删除，使用迭代器
            Iterator<Term> iterator = terms.iterator();
            while (iterator.hasNext()) {
                Term term = iterator.next();
                //词性的过滤、词的过滤
                if (term.getNatureStr().equals("w") || term.getNatureStr() == null || term.getRealName().length() < 2) {
                    iterator.remove();
                    continue;
                }
                //统计
                String realName = term.getRealName();
                int count;
                if (map.containsKey(realName)) {
                    System.out.println(map.get(realName));
                    count = map.get(realName) + 1;
                } else {
                    count = 1;
                }
                map.put(realName, count);
            }
        }
        //将map转化为ArrayList
        List<WordCount> wordCounts = new ArrayList<>();
        for(Map.Entry<String,Integer> entry : map.entrySet()){
            WordCount wordCount = new WordCount();
            wordCount.setWord(entry.getKey());
            wordCount.setCount(entry.getValue());
            wordCounts.add(wordCount);
        }
        return wordCounts;
    }

}

