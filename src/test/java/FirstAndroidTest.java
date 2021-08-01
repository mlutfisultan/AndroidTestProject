import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class FirstAndroidTest {
    public AndroidTouchAction actions;
    AndroidDriver driver;
    AppiumDriverLocalService appiumService;
    String appiumServiceUrl;

    private void scrollDown() {
        Dimension dimension = driver.manage().window().getSize();
        int scrollStart = (int) (dimension.getHeight() * 0.8);
        int scrollEnd = (int) (dimension.getHeight() * 0.1);

        actions = new AndroidTouchAction(driver)
                .press(PointOption.point(0, scrollStart))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
                .moveTo(PointOption.point(0, scrollEnd))
                .release()
                .perform();
    }

    @BeforeTest
    public void setUp() throws MalformedURLException {
        appiumService = AppiumDriverLocalService.buildDefaultService();
        appiumService.start();
        appiumServiceUrl = appiumService.getUrl().toString();
        System.out.println("Appium Service Address : - " + appiumServiceUrl);

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("platformVersion", "10");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("app", System.getProperty("user.dir") + "/Apps/ApiDemos.apk");
        //SMS
        /*caps.setCapability("appPackage","com.google.android.apps.messaging");
        caps.setCapability("appActivity",".ui.ConversationListActivity");*/


        driver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), caps);
        driver = new AndroidDriver<>(new URL(appiumServiceUrl), caps);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    }

    @Test(priority = 1)
    public void Click_App_Btn() {
        driver.findElementByAccessibilityId("App").click();
    }

    @Test(priority = 2)
    public void scroll_test() {
        AndroidElement views =
                (AndroidElement) driver.findElementByAccessibilityId("Views");
        // Tap
        actions = new AndroidTouchAction(driver);
        actions.tap(ElementOption.element(views)).perform();
        // ScrollDown
        scrollDown();
        AndroidElement lists = (AndroidElement) driver.findElementByAccessibilityId("Lists");
        actions.tap(ElementOption.element(lists)).perform();
    }

    @Test(priority = 3)
    public void drag_drop() {
        AndroidElement views = (AndroidElement) driver.findElementByAccessibilityId("Views");

        actions = new AndroidTouchAction(driver);
        actions.tap(ElementOption.element(views)).perform();

        AndroidElement drag_drop =
                (AndroidElement) driver.findElementByAccessibilityId("Drag and Drop");
        actions.tap(ElementOption.element(drag_drop)).perform();

        AndroidElement drag = (AndroidElement) driver.findElement(By.id("drag_dot_1"));
        AndroidElement drop = (AndroidElement) driver.findElement(By.id("drag_dot_2"));

        actions.longPress(ElementOption.element(drag))
                .waitAction().moveTo(ElementOption.element(drop))
                .release()
                .perform();
    }

    @Test(priority = 4)
    public void swipe_test() {
        AndroidElement views = (AndroidElement)
                driver.findElementByAccessibilityId("Views");

        //Tap
        actions = new AndroidTouchAction(driver);
        actions.tap(ElementOption.element(views)).perform();

        AndroidElement gallery = (AndroidElement)
                driver.findElementByAccessibilityId("Gallery");
        actions.tap(ElementOption.element(gallery)).perform();

        AndroidElement photo = (AndroidElement)
                driver.findElementByAccessibilityId("1. Photos");
        actions.tap(ElementOption.element(photo)).perform();

        AndroidElement pic1 =
                (AndroidElement) driver.findElements(By.className("android.widget.ImageView")).get(0);

        actions.press(ElementOption.element(pic1))
                .waitAction()
                .moveTo(PointOption.point(-20, 210))
                .release()
                .perform();
    }

    @Test(enabled = false)
    public void send_SMS() {
        driver.sendSMS("555-123-4567", "Hello from TAU");
    }

   /* @Test(priority = 5.6)
    public void send_Photo() throws IOException {

        classPath = new File(System.getProperty("user.dir"));
        imageDir = new File(classPath, "/resources/images");
        img = new File(imageDir.getCanonicalFile(), "TAU-logo.png");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(backupBtn)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(touchOutsideBtn)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(keepOffBtn)).click();

        String Android_Photo_Path = "mnt/sdcard/Pictures";
        driver.pushFile(Android_Photo_Path + "/" + img.getName(), img);
        wait.until(ExpectedConditions.numberOfElementsToBe(photo, 1));
    }*/

    @Test(priority = 6)
    public void clipboard_test() {
        String text = "Hello TAU";
        driver.setClipboardText(text);
        MobileElement nameTxt = (MobileElement) driver.findElementByAccessibilityId("my_text_fieldCD");
        nameTxt.clear();
        nameTxt.sendKeys(driver.getClipboardText());
        Assert.assertEquals(text, nameTxt.getText());
    }

    @AfterTest
    public void End() {
        System.out.println("Stop driver");
        driver.quit();
        System.out.println("Stop appium service");
        appiumService.stop();
    }
}
