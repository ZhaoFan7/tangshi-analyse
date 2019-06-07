package tangshiAnalyze.crawler.common;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * 定义网页
 * Author:zhaofan
 * Created:2019/3/17
 */
//equalsAndHashcode,get,set,toString，requiredArgsConstructor(构造方法),lombok.value
@Data
public class Page {

    //数据网站的根地址
    private final String base;

    //具体的网页路径
    private final String path;

    //网页DOM（文档对象模型）对象
    private  HtmlPage htmlPage;

    //子页面对象集合
    private  Set<Page> subPage = new HashSet<>();

    //是否详情网页
    private final Boolean detail;

    //数据对象(将hashmap抽象成一个类)
    private DataSet dataSet = new DataSet();

    public String getUrl(){
        return this.base+this.path;
    }
}
