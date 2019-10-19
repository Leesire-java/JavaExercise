package cn.coderoom.webmagic;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.*;

/**
 * @package：cn.coderoom.webmagic
 * @description:
 * @author: Leesire
 * @email:coderoom.cn@gmail.com
 * @createtime: 2019/10/17
 */
public class WebMagicAPP implements PageProcessor {

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

    private void post(){

        //设置POST请求
        Request request = new Request("http://PostRequestUrl.com");
        //只有POST请求才可以添加附加参数
        request.setMethod(HttpConstant.Method.POST);

        //设置POST参数
        List<NameValuePair> nvs = new ArrayList<NameValuePair>();
        nvs.add(new BasicNameValuePair("key1", "value1"));
        nvs.add(new BasicNameValuePair("key2", "value2"));

        //转换为键值对数组
        NameValuePair[] values = nvs.toArray(new NameValuePair[] {});

        //将键值对数组添加到map中
        Map<String, Object> params = new HashMap<String, Object>();
        //key必须是：nameValuePair
        params.put("nameValuePair", values);

        //设置request参数
        request.setExtras(params);

        // 开始执行
        try {
            Spider.create(new Login()).addRequest(request).thread(5).run();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
