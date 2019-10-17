package cn.coderoom.webmagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Scanner;

/**
 * @package：cn.coderoom.webmagic
 * @description:
 * @author: Leesire
 * @email:coderoom.cn@gmail.com
 * @createtime: 2019/10/17
 */
public class WebMagicAPP implements PageProcessor {

    public static final String URL_LIST = "http://blog\\.sina\\.com\\.cn/s/articlelist_1487828712_0_\\d+\\.html";

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override
    public void process(Page page) {

        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());

        String __EVENTTARGET = page.getHtml().xpath("//*[@id=\"__EVENTTARGET\"]/@value").toString();
        String __EVENTARGUMENT = page.getHtml().xpath("//*[@id=\"__EVENTARGUMENT\"]/@value").toString();
        String __VIEWSTATE = page.getHtml().xpath("//*[@id=\"__VIEWSTATE\"]/@value").toString();

        //*[@id="__VIEWSTATEGENERATOR"]
        String __VIEWSTATEGENERATOR = page.getHtml().xpath("//*[@id=\"__VIEWSTATEGENERATOR\"]/@value").toString();
        String __EVENTVALIDATION = page.getHtml().xpath("//*[@id=\"__EVENTVALIDATION\"]/@value").toString();

        String txtUserName = page.getHtml().xpath("//*[@id=\"txtUserName\"]/@value").toString();
        String txtPassword = page.getHtml().xpath("//*[@id=\"txtPassword\"]/@value").toString();
        String txtCaptcha = page.getHtml().xpath("//*[@id=\"txtCaptcha\"]/@value").toString();

        System.out.println(__EVENTTARGET);
        System.out.println(__EVENTARGUMENT);
        System.out.println(__VIEWSTATE);
        System.out.println(__VIEWSTATEGENERATOR);
        System.out.println(__EVENTVALIDATION);
        System.out.println(txtUserName);
        System.out.println(txtPassword);
        System.out.println(txtCaptcha);

        Scanner sc = new Scanner(System.in);
        System.out.println("Please Enter txtCaptcha:");
        String name = sc.nextLine();  //读取字符串型输入


    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new WebMagicAPP()).addUrl("http://whp.zjsafety.gov.cn/WebManage/EnterpriseRiskLogin.aspx").run();
    }

}
