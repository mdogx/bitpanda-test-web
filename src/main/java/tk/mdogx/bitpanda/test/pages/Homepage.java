package tk.mdogx.bitpanda.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import tk.mdogx.bitpanda.test.utils.Utils;

public class Homepage {

    private WebDriver driver;

    public Homepage(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getSignInBtn() {
        return Utils.waitToBeClickable(driver, By.xpath("//a[contains(text(), \"Sign in\")]"), 30);
    }
}
