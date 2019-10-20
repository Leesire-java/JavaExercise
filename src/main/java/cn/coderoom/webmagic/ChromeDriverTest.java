package cn.coderoom.webmagic;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class ChromeDriverTest {

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "D:\\Program Files\\chromedriver\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-infobars");
        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors");
        WebDriver driver = new ChromeDriver(options);

        driver.get("http://whp.zjsafety.gov.cn/WebManage/EnterpriseRiskLogin.aspx");


        driver.findElement(By.xpath("//*[@id=\"weblogo\"]")).click();

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
            BufferedImage eleScreenshot = fullImg.getSubimage(point.getX() + 120, point.getY() - rollHeight.intValue(),
                    eleWidth, eleHeight);
            ImageIO.write(eleScreenshot, "jpg", screenshot);
            File screenshotLocation = new File("D:\\Program Files\\chromedriver\\captcha.jpg");
            FileUtils.copyFile(screenshot, screenshotLocation);

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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
        System.out.println(" Page title is: " + driver.getTitle());


        // 关闭浏览器
        driver.quit();
        driver.close();

    }

}
