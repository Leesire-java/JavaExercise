package cn.coderoom.webmagic;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;

/**
 * @package：cn.coderoom.webmagic
 * @description: chromeDriver是谷歌的浏览器驱动，用来适配Selenium,有图形页面存在，在调试爬虫下载运行的功能的时候会相对方便
 * @author: Leesire
 * @email:coderoom.cn@gmail.com
 * @createtime: 2019/10/18
 */
public class ChromedriverLogin implements PageProcessor {

    private static ChromeDriverService service;
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


        try {
            login(page);
        } catch (Exception e ){

        }

        //System.out.println(page.getHtml().toString());


    }

    @Override
    public Site getSite() {


        return site;

    }

    public void login(Page page) throws IOException {
        // 登陆
        WebDriver driver= ChromedriverLogin.getChromeDriver();

        driver.get(page.getRequest().getUrl());


        driver.findElement(By.xpath("//*[@id=\"weblogo\"]")).click();

        downloadVerificationCode(driver);

        // 设置用户名密码
        WebElement userEle = driver.findElement(By.xpath("//*[@id=\"txtUserName\"]"));
        userEle.sendKeys("123"); // 用户名

        WebElement passEle = driver.findElement(By.xpath("//*[@id=\"txtPassword\"]"));
        passEle.sendKeys("Aj@12345"); // 密码

        String imgCaptcha = driver.findElement(By.xpath("//*[@id=\"imgCaptcha\"]")).getAttribute("src");
        System.out.println(imgCaptcha);
        Scanner sc = new Scanner(System.in);
        System.out.println("Please Enter txtCaptcha:");
        String code = sc.nextLine();  //读取字符串型输入

        WebElement capEle = driver.findElement(By.id("txtCaptcha"));
        capEle.sendKeys(code); // 密码

        // 模拟点击
        driver.findElement(By.xpath("//*[@id=\"Button1\"]")).click(); // xpath语言：id为form-group-login的form下的button

        // 显示搜索结果页面的 title
        System.out.println(" Page title is: " +driver.getTitle());

        System.out.println(">>>>>>>>>>111");

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

        // 关闭浏览器
        driver.quit();
        // 关闭 ChromeDriver 接口
        service.stop();

        driver.close();;

    }

    public static WebDriver getChromeDriver() throws IOException {

        //System.setProperty("webdriver.chrome.driver","C:/Users/sunlc/AppData/Local/Google/Chrome/Application/chrome.exe");
        /**
         * linux需要注释
         */
        System.setProperty("webdriver.chrome.driver","D:\\Program Files\\chromedriver\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-infobars");
        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors");
        WebDriver driver = new ChromeDriver(options);
        // 创建一个 ChromeDriver 的接口，用于连接 Chrome（chromedriver.exe 的路径可以任意放置，只要在newFile（）的时候写入你放的路径即可）
        //service = new ChromeDriverService.Builder().usingDriverExecutable(new File("D:\\Program Files\\chromedriver\\chromedriver.exe")) .usingAnyFreePort().build();
        //service.start();

        // 创建一个 Chrome 的浏览器实例
        //return new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());

        return driver;

    }

    /**
     * 下载验证码
     *
     * @param driver
     * @return
     */
    public String downloadVerificationCode(WebDriver driver) {
        try {
            /** 设置图片名称 */
            Date now = new Date();

            WebElement ele = driver.findElement(By.xpath("//*[@id=\"txtCaptcha\"]"));// 获取验证码元素
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", ele);// 滚动条滚动至验证码位置
            Thread.sleep(3000);// 等待3秒，等待js执行完成

            /** 计算网页被卷去的高，即滚动条滚动距离 */
            String jsStr = "var yScroll; if (self.pageYOffset) { yScroll = self.pageYOffset; } else if (document.documentElement && document.documentElement.scrollTop) { yScroll = document.documentElement.scrollTop; } else if (document.body) { yScroll = document.body.scrollTop; } return yScroll;";
            Long rollHeight = (Long) ((JavascriptExecutor) driver).executeScript(jsStr);
            // System.out.println("网页被卷去的高: " + rollHeight);

            /** 保存验证码 */
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            BufferedImage fullImg;
            fullImg = ImageIO.read(screenshot);
            Point point = ele.getLocation();
            int eleWidth = ele.getSize().getWidth();
            int eleHeight = ele.getSize().getHeight();
            ////前两个值是坐标位置X、Y，后两个是长和宽
            BufferedImage eleScreenshot = fullImg.getSubimage(point.getX()+120, point.getY() - rollHeight.intValue(),
                    eleWidth, eleHeight);
            ImageIO.write(eleScreenshot, "jpg", screenshot);
            File screenshotLocation = new File("D:\\Program Files\\chromedriver\\captcha.jpg");
            FileUtils.copyFile(screenshot, screenshotLocation);

            return "captcha.jpg";

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }


    public static void main(String[] args) {
        Spider.create(new ChromedriverLogin()).addUrl("http://whp.zjsafety.gov.cn/WebManage/EnterpriseRiskLogin.aspx").run();
    }


}
