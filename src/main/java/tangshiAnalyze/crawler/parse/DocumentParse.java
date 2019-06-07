package tangshiAnalyze.crawler.parse;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import tangshiAnalyze.crawler.common.Page;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 *链接解析
 * Author:zhaofan
 * Created:2019/4/13
 */
public class DocumentParse implements Parse{

    @Override
    public void parse(final Page page) {
        if(page.getDetail()){
            return;
        }
        HtmlPage htmlPage = page.getHtmlPage();
        htmlPage.getBody().getElementsByAttribute("div","class","typecont")
                .forEach(new Consumer<HtmlElement>() {
                    @Override
                    public void accept(HtmlElement htmlElement) {
                        htmlElement.getElementsByTagName("a").forEach(aNode ->{
                            String path = aNode.getAttribute("href");
                            Page subPage = new Page(page.getBase(),path,true);
                            page.getSubPage().add(subPage);
                        });
                    }
                });
    }
}
