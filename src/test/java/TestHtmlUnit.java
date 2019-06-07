import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;

/**
 * Author:zhaofan
 * Created:2019/4/13
 */
public class TestHtmlUnit {
    public static void main(String[] args) {
        try(WebClient webClient = new WebClient(BrowserVersion.CHROME
        )){

            //1、采集
            //不执行js文件
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setCssEnabled(false);
                HtmlPage htmlPage = webClient.getPage("https://so.gushiwen.org/shiwenv_45c396367f59.aspx");
                HtmlElement body = htmlPage.getBody();
               // String text = body.asText();
                //System.out.print(text);
                //2、解析
            HtmlTextArea id = (HtmlTextArea) htmlPage.getHtmlElementById("txtare20788");
            String con = id.asText();



            String titlePath = "//div[@class='cont']/h1/text()";   //text():mean h1 to string
            Object o = htmlPage.getByXPath(titlePath).get(0);
            System.out.println(o.getClass().getName());




            System.out.println(con);
            System.out.println(id.getClass().getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
