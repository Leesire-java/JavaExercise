package cn.coderoom.webmagic;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * @package：cn.coderoom.webmagic
 * @description:
 * @author: Leesire
 * @email:coderoom.cn@gmail.com
 * @createtime: 2019/10/18
 */
public class phantomjs implements PageProcessor {

    private Site site = Site
            .me()
            .setRetryTimes(3)
            .setSleepTime(1000)
            .setTimeOut(10000)
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
            .addHeader("Connection", "keep-alive")
            .addHeader("X-Requested-With", "XMLHttpRequest")
            .addHeader("Content-Type",
                    "application/x-www-form-urlencoded;charset=utf-8")
            .addHeader(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
            .addCookie("whp.zjsafety.gov.cn","ASP.NET_SessionId", "5aru51410gz0eydbgfuapg5k")
            .addCookie("whp.zjsafety.gov.cn","DefaultApp", "AnnouncementAndRisk")
            .addCookie("whp.zjsafety.gov.cn","EnterType", "RISK")
            .addCookie("whp.zjsafety.gov.cn","Md5", "efa3e200c647420778355bbb19c39db4")
            .addCookie("whp.zjsafety.gov.cn","PositionName", "%e4%b8%bb%e5%ad%90%e8%b4%a6%e5%8f%b7")
            .addCookie("whp.zjsafety.gov.cn","UserType", "Enterprise")
            .addCookie("whp.zjsafety.gov.cn","Userhashkey", "3328")
            .addCookie("whp.zjsafety.gov.cn","td_cookie", "2524445300");


    @Override
    public void process(Page page) {

        //page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());

        WebDriver driver= PhantomJsDriver.getPhantomJSDriver();

        driver.get(page.getRequest().getUrl());
        //System.out.println(page.getHtml().toString());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement webElement = driver.findElement(By.className("container"));
        String str = webElement.getAttribute("outerHTML");

        Html html = new Html(str);

        System.out.println(html.toString());



    }

    @Override
    public Site getSite() {

        // 将获取到的cookie信息添加到webmagic中
        /*for (org.apache.http.cookie.Cookie cookie : cookies) {
            site.addCookie(cookie.getName().toString(), cookie.getValue()
                    .toString());
        }*/

        return site;

    }

    public static void main(String[] args) {
        Spider.create(new phantomjs()).addUrl("http://whp.zjsafety.gov.cn/WebManage/EnterpriseRiskMain.aspx").run();
    }

}
