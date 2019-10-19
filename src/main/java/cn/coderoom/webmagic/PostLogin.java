package cn.coderoom.webmagic;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
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
 * @createtime: 2019/10/18
 */
public class PostLogin implements PageProcessor {

    public static final String URL_LIST = "http://blog\\.sina\\.com\\.cn/s/articlelist_1487828712_0_\\d+\\.html";

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

        post( __EVENTTARGET, __EVENTARGUMENT, __VIEWSTATE, __VIEWSTATEGENERATOR, __EVENTVALIDATION, txtUserName, txtPassword, txtCaptcha);


    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new PostLogin()).addUrl("http://whp.zjsafety.gov.cn/WebManage/EnterpriseRiskLogin.aspx").run();
    }

    private void post(String __EVENTTARGET,String __EVENTARGUMENT,String __VIEWSTATE,String __VIEWSTATEGENERATOR,String __EVENTVALIDATION,String txtUserName,String txtPassword,String txtCaptcha){

        // 登陆 Url
        String loginUrl = "http://whp.zjsafety.gov.cn/WebManage/EnterpriseRiskLogin.aspx";
        // 需登陆后访问的 Url
        String dataUrl = "http://whp.zjsafety.gov.cn/";
        HttpClient httpClient = new HttpClient();

        // 模拟登陆，按实际服务器端要求选用 Post 或 Get 请求方式
        PostMethod postMethod = new PostMethod(loginUrl);

        // 设置登陆时要求的信息，用户名和密码
        NameValuePair[] data = { new NameValuePair("__EVENTTARGET", __EVENTTARGET)
                , new NameValuePair("__EVENTARGUMENT", __EVENTARGUMENT)
                , new NameValuePair("__VIEWSTATE", __VIEWSTATE)
                , new NameValuePair("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR)
                , new NameValuePair("__EVENTVALIDATION", __EVENTVALIDATION)
                , new NameValuePair("txtUserName", txtUserName)
                , new NameValuePair("txtPassword", txtPassword)
                , new NameValuePair("txtCaptcha", txtCaptcha)};

        postMethod.setRequestBody(data);

        try {
            // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            int statusCode=httpClient.executeMethod(postMethod);

            // 获得登陆后的 Cookie
            Cookie[] cookies = httpClient.getState().getCookies();

            StringBuffer tmpcookies = new StringBuffer();
            for (Cookie c : cookies) {
                tmpcookies.append(c.toString() + ";");
                System.out.println("cookies = "+c.toString());
            }
            if(statusCode==302){//重定向到新的URL
                System.out.println("模拟登录成功");
                // 进行登陆后的操作
                GetMethod getMethod = new GetMethod(dataUrl);
                // 每次访问需授权的网址时需带上前面的 cookie 作为通行证
                getMethod.setRequestHeader("cookie", tmpcookies.toString());
                // 你还可以通过 PostMethod/GetMethod 设置更多的请求后数据
                // 例如，referer 从哪里来的，UA 像搜索引擎都会表名自己是谁，无良搜索引擎除外
                postMethod.setRequestHeader("Referer", "http://passport.mop.com/");
                postMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
                httpClient.executeMethod(getMethod);
                // 打印出返回数据，检验一下是否成功
                String text = getMethod.getResponseBodyAsString();
                System.out.println(text);
            }
            else {
                System.out.println("登录失败");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
