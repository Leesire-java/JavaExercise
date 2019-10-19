package cn.coderoom.webmagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @package：cn.coderoom.webmagic
 * @description:
 * @author: Leesire
 * @email:coderoom.cn@gmail.com
 * @createtime: 2019/10/18
 */
public class CookieTest implements PageProcessor {

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
            .addCookie("whp.zjsafety.gov.cn","Md5", "1675abc67161ba65f12abfa3beb0e0f0")
            .addCookie("whp.zjsafety.gov.cn","PositionName", "%e4%b8%bb%e5%ad%90%e8%b4%a6%e5%8f%b7")
            .addCookie("whp.zjsafety.gov.cn","UserType", "Enterprise")
            .addCookie("whp.zjsafety.gov.cn","Userhashkey", "3328")
            .addCookie("whp.zjsafety.gov.cn","td_cookie", "2520012167");


    @Override
    public void process(Page page) {

        //page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());


        System.out.println(page.getHtml().toString());



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
        Spider.create(new CookieTest()).addUrl("http://whp.zjsafety.gov.cn/WebManage/EnterpriseRiskMain.aspx").run();
    }

}
