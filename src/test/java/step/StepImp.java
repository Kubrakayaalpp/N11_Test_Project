package step;

import WebAutomationBase.helper.ElementHelper;
import WebAutomationBase.helper.StoreHelper;
import WebAutomationBase.model.ElementInfo;
import base.BaseClass;
import com.thoughtworks.gauge.Step;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.ConcurrentHashMap;

public class StepImp extends BaseClass {

    public static ConcurrentHashMap<String,String> map = new ConcurrentHashMap<String, String>();

    @Step("<key> li butona tikla")
    public void clickButton(String key) {
       findElement(key).click();
    }

    @Step("<saniye> saniye bekle")
    public void wait(int saniye) throws InterruptedException {
        Thread.sleep(saniye * 1000);
    }

    @Step({"Write value <text> to element <key>",
            "<text> textini <key> elemente yaz"})
    public void sendKeys(String text, String key) {
        if (!key.equals("")) {
            findElement(key).sendKeys(text);
            //logger.info(key + " elementine " + text + " texti yazıldı.");
        }
    }

    @Step("Sayfada <text> var mı ?")
    public void getPageSourceControl(String text){
        Assert.assertTrue("Text bulunamadi!!",driver.getPageSource().contains(text));
    }

    @Step("<key> in degerini <mapKey> tut")
    public void degerTut(String key, String mapKey){
        WebElement webElement = findElement(key);
        String value = webElement.getText();
        map.put(mapKey,value);
        System.out.println(value);

    }
    @Step("<mapKey1> ile <mapKey2> urun fiyatları aynı mı?")
    public void UrunFiyatKontrolu(String mapKey1, String mapKey2){
        String expectedValue = map.get(mapKey1);
        String actualValue = map.get(mapKey2);
        Assert.assertTrue("!!.",expectedValue.contains(actualValue));
    }

    @Step("Sayfada <key> buton var mı ? Yoksa <msg>")
    public void getButtonControl(String key, String msg){
        Assert.assertTrue(msg,findElement(key).isDisplayed());
    }

    @Step({"<key> alanına kaydır"})
    public void scrollToElement(String key) {
        scrollToElementToBeVisible(key);
    }

 @Step("<key> alanı alanı ile aynı mı?")
 public void assertEquals(String key) {
     Assert.assertEquals("Sayfa doğru değil.",driver.getCurrentUrl(),"https://www.n11.com/arama?q=bilgisayar&pg=2");
 }

    private WebElement findElement(String key){
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        WebDriverWait webDriverWait = new WebDriverWait(driver, 15);
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }
    public WebElement scrollToElementToBeVisible(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        WebElement webElement =findElement(key);
        if (webElement != null) {
            scrollTo(webElement.getLocation().getX(), webElement.getLocation().getY() - 100);
        }
        return webElement;
    }
    private Object executeJS(String script, boolean wait) {
        return wait ? getJSExecutor().executeScript(script, "") : getJSExecutor().executeAsyncScript(script, "");
    }
    private void scrollTo(int x, int y) {
        String script = String.format("window.scrollTo(%d, %d);", x, y);
        executeJS(script, true);
    }
    private JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) driver;
    }


}


