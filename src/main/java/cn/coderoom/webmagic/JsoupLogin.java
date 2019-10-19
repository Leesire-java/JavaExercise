package cn.coderoom.webmagic;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

/**
 * @package：cn.coderoom.webmagic
 * @description:
 * @author: Leesire
 * @email:coderoom.cn@gmail.com
 * @createtime: 2019/10/18
 */
public class JsoupLogin  implements PageProcessor {

    private static Map<String, String> cookies = null;

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

        //page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());

        String __EVENTTARGET = page.getHtml().xpath("//*[@id=\"__EVENTTARGET\"]/@value").toString();
        __EVENTTARGET = "BtnLogin";
        String __EVENTARGUMENT = page.getHtml().xpath("//*[@id=\"__EVENTARGUMENT\"]/@value").toString();
        String __VIEWSTATE = page.getHtml().xpath("//*[@id=\"__VIEWSTATE\"]/@value").toString();

        //*[@id="__VIEWSTATEGENERATOR"]
        String __VIEWSTATEGENERATOR = page.getHtml().xpath("//*[@id=\"__VIEWSTATEGENERATOR\"]/@value").toString();
        String __EVENTVALIDATION = page.getHtml().xpath("//*[@id=\"__EVENTVALIDATION\"]/@value").toString();

        String txtUserName = page.getHtml().xpath("//*[@id=\"txtUserName\"]/@value").toString();
        String txtPassword = page.getHtml().xpath("//*[@id=\"txtPassword\"]/@value").toString();
        String txtCaptcha = page.getHtml().xpath("//*[@id=\"txtCaptcha\"]/@value").toString();
        txtUserName = "MTIz";
        txtPassword = "QWpAMTIzNDU=";

        String imgCaptcha = page.getHtml().xpath("//*[@id=\"imgCaptcha\"]/@src").toString();

        System.out.println(__EVENTTARGET);
        System.out.println(__EVENTARGUMENT);
        System.out.println(__VIEWSTATE);
        System.out.println(__VIEWSTATEGENERATOR);
        System.out.println(__EVENTVALIDATION);
        System.out.println(txtUserName);
        System.out.println(txtPassword);
        System.out.println(txtCaptcha);
        System.out.println(imgCaptcha);

        Scanner sc = new Scanner(System.in);
        System.out.println("Please Enter postLogin txtCaptcha:");
        txtCaptcha = sc.nextLine();  //读取字符串型输入

        try {

            login( __EVENTTARGET, __EVENTARGUMENT, __VIEWSTATE, __VIEWSTATEGENERATOR, __EVENTVALIDATION, txtUserName, txtPassword, txtCaptcha);

        } catch (IOException e){
            e.printStackTrace();
        }



    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new JsoupLogin()).addUrl("http://whp.zjsafety.gov.cn/WebManage/EnterpriseRiskLogin.aspx").run();
    }


    /**
     * 模拟登录获取cookie和sessionid
     */
    public void login(String __EVENTTARGET,String __EVENTARGUMENT,String __VIEWSTATE,String __VIEWSTATEGENERATOR,String __EVENTVALIDATION,String txtUserName,String txtPassword,String txtCaptcha) throws IOException {
        String urlLogin = "http://whp.zjsafety.gov.cn/WebManage/EnterpriseRiskLogin.aspx";
        Connection connect = Jsoup.connect(urlLogin);
        // 伪造请求头
        connect.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3").header("Accept-Encoding",
                "gzip, deflate");
        connect.header("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7").header("Connection", "keep-alive");
        connect.header("Content-Length", "526").header("Content-Type", "application/x-www-form-urlencoded");
        connect.header("Host", "whp.zjsafety.gov.cn").header("Origin", "http://whp.zjsafety.gov.cn").header("Referer", "http://whp.zjsafety.gov.cn/WebManage/EnterpriseRiskLogin.aspx");
        connect.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36");

        // 携带登陆信息
        connect.data("__LASTFOCUS", "" ).data("__EVENTTARGET", __EVENTTARGET ).data("__EVENTARGUMENT", __EVENTARGUMENT)
                .data("__VIEWSTATE", __VIEWSTATE).data("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR)
                .data("__EVENTVALIDATION",__EVENTVALIDATION).data("txtUserName", "txtUserName")
                .data("txtPassword", txtPassword).data("txtCaptcha", txtCaptcha);

        //请求url获取响应信息
        Response res = connect.ignoreContentType(true).method(Method.POST).execute();// 执行请求
        // 获取返回的cookie
        cookies = res.cookies();

        String url = "http://qiaoliqiang.cn/Exam/view/testPerson/outEmployeeAllot.jsp";
        // 直接获取DOM树，带着cookies去获取
        Document document = Jsoup.connect(url).cookies(cookies).post();
        System.out.println(document.toString());
    }


}
