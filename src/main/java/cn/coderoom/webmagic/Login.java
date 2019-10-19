package cn.coderoom.webmagic;

import org.openqa.selenium.*;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.Scanner;
import java.util.Set;

/**
 * @package：cn.coderoom.webmagic
 * @description:
 * @author: Leesire
 * @email:coderoom.cn@gmail.com
 * @createtime: 2019/10/18
 */
public class Login  implements PageProcessor {

    // 用来存储cookie信息
    private Set<Cookie> cookies;

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
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

    @Override
    public void process(Page page) {

        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());


        login(page);

        //System.out.println(page.getHtml().toString());


    }

    @Override
    public Site getSite() {


        return site;

    }

    public void login(Page page) {
        // 登陆
        WebDriver driver= PhantomJsDriver.getPhantomJSDriver();

        driver.get(page.getRequest().getUrl());

        System.out.println(">>>>>>>>>>");

        // 防止页面未能及时加载出来而设置一段时间延迟
        try {
            Thread.sleep(5000);

            WebElement webElement = driver.findElement(By.id("txtUserName"));
            String str = webElement.getAttribute("outerHTML");

            Html html = new Html(str);

            System.out.println(html.toString());

            // 设置用户名密码
            WebElement userEle = driver.findElement(By.xpath("//*[@id=\"txtUserName\"]"));
            userEle.sendKeys("123"); // 用户名

            WebElement passEle = driver.findElement(By.xpath("//*[@id=\"txtPassword\"]"));
            passEle.sendKeys("Aj@12345"); // 密码

        } catch (Exception e) {

            e.printStackTrace();

        }

        System.out.println(">>>>>>>>>>111");

        Scanner sc = new Scanner(System.in);
        System.out.println("Please Enter txtCaptcha:");
        String code = sc.nextLine();  //读取字符串型输入

        WebElement capEle = driver.findElement(By.id("txtCaptcha"));
        capEle.sendKeys(code); // 密码

        // 模拟点击
        driver.findElement(By.xpath("//*[@id=\"Button1\"]")).click(); // xpath语言：id为form-group-login的form下的button

        // 防止页面未能及时加载出来而设置一段时间延迟
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 获取cookie信息
        cookies = driver.manage().getCookies();

        System.out.println(">>>>>>>>>>"+cookies.toString());

        // 将获取到的cookie信息添加到webmagic中
        for (Cookie cookie : cookies) {
            site.addCookie(cookie.getName().toString(), cookie.getValue()
                    .toString());
            System.out.println(cookie.getName().toString()+":"+cookie.getValue()
                    .toString());
        }

        driver.close();

    }

    public static void main(String[] args) {

        Spider.create(new Login()).addUrl("http://whp.zjsafety.gov.cn/WebManage/EnterpriseRiskLogin.aspx").run();

    }

}
