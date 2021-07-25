import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class AndroidAppPackage {
    AppiumDriver driver;

    @BeforeTest
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("platformVersion", "11");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("appPackage", "com.android.calculator2");
        caps.setCapability("appActivity", " .Calculator");

        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), caps);
    }

    @Test
    public void Click_App_Btn() {
        driver.findElement(By.id("digit_5")).click();
        driver.findElement(By.id("op_add")).click();
        driver.findElement(By.id("digit_5")).click();
        driver.findElement(By.id("eq")).click();
        Assert.assertEquals(driver.findElement(By.id("result")), 10);

    }

    @AfterTest
    public void Tear_Down() {
        if (null != driver) {
            driver.quit();
        }
    }
}
