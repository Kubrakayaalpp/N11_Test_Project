package base;

import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import static java.lang.System.getenv;

public class BaseClass {

    protected static WebDriver driver;
    protected static WebDriverWait webDriverWait;


    @BeforeScenario
    public void setUp() throws Exception{
        String baseUrl = "https://www.n11.com/";
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        if (StringUtils.isNotEmpty(getenv("key"))) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("test-type");
            options.addArguments("disable-popup-blocking");
            options.addArguments("ignore-certificate-errors");
            options.addArguments("--start-fullscreen");
            options.addArguments("--disable-web-security");
            options.addArguments("--no-proxy-server");
            options.addArguments("disable-translate");
            options.addArguments("start-maximized");
            options.addArguments("--no-sandbox");
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            capabilities.setCapability("key", getenv("key"));
            driver = new RemoteWebDriver(new URL("http://hub.testinium.io/wd/hub"), capabilities);
            ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
        } else {
            System.setProperty("webdriver.chrome.driver", "driver/chromedriver");
            driver = new ChromeDriver();
        }
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
        webDriverWait = new WebDriverWait(driver, 45, 150);
        driver.manage().window().maximize();
        driver.get(baseUrl);
    }
    @AfterScenario
    public void tearDown(){
        driver.quit();
    }
}

