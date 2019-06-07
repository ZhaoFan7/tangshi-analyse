
package tangshiAnalyze.crawler.parse;

import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import tangshiAnalyze.crawler.common.Page;

/**
 *详情页面解析
 * Author:zhaofan
 * Created:2019/3/17
 */
public class DataPageParse implements Parse {
    @Override
    public void parse(final Page page){
        if(!page.getDetail()){
            return;
        }

        HtmlPage htmlPage = page.getHtmlPage();
        //title
        ///html/body/div[3]/div[1]/div[2]/div[1]/h1
        //body > div.main3 > div.left > div:nth-child(2) > div.cont > h1
        String titlePath = "//div[@class='cont']/h1/text()";   //text():mean h1 to string
        DomText titleDom = (DomText) htmlPage.getByXPath(titlePath).get(0);
        String title = titleDom.asText();

        //dynasty
        String dynastyPath = "//div[@class='cont']/p/a[1]/text()";
        DomText dynastyDom= (DomText)htmlPage.getByXPath(dynastyPath).get(0);
        //System.out.println(dynastyDom.getClass().getName());
        String dynasty = dynastyDom.asText();

        //author
        //body > div.main3 > div.left > div:nth-child(2) > div.cont > p > a:nth-child(3)
        String authorPath = "//div[@class='cont']/p/a[2]/text()";
        DomText authorDom = (DomText) htmlPage.getByXPath(authorPath).get(0);
        String author = authorDom.asText();

        //content
        String contentpath = "//div[@class='cont']/div[@class='contson']";
        HtmlDivision contentDiv = (HtmlDivision) htmlPage.getByXPath(contentpath).get(0);
        String content = contentDiv.asText();

        page.getDataSet().putData("author",author);
        page.getDataSet().putData("content",content);
        page.getDataSet().putData("dynasty",dynasty);
        page.getDataSet().putData("title",title);
        //加更多的数据，但是在清洗的时候要不要由自己决定
    }
}
