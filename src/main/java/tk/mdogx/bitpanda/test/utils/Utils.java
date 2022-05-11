package tk.mdogx.bitpanda.test.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.Properties;


public class Utils {

	private static WebDriver webDriver;

	public final static String BROWSER = getPropertyByKey("browser").toLowerCase();
	public final static String BASEURL = getPropertyByKey("baseUrl").toLowerCase();
	public final static Long TIMEOUT = Long.parseLong(getPropertyByKey("timeout"));

	public final static String MAIL_PATTERN = getPropertyByKey("mail_pattern").toLowerCase();

	public static WebDriver getDriver() {
		if (webDriver != null) {
			return webDriver;
		}
		switch (BROWSER) {
			case "firefox":
				WebDriverManager.firefoxdriver().setup();
				webDriver = new FirefoxDriver();
				break;
			case "chrome":
				WebDriverManager.chromedriver().setup();
				webDriver = new ChromeDriver();
				break;
		}
		return webDriver;
	}

	public static String getPropertyByKey(String key) {
		// Take properties from file
		Properties prop = new Properties();
		try (FileInputStream fis = new FileInputStream("src\\main\\java\\tk\\mdogx\\bitpanda\\test\\test.properties");
			 InputStreamReader isr = new InputStreamReader(fis, "UTF-8")) {
			prop.load(isr);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop.getProperty(key);
	}

	public static String getRandomString(int length) {
		final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom rnd = new SecureRandom();
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}


	public static boolean isPresent(WebDriver webdriver, By selector) {
		// try to find element by specified selector
		try {
			webdriver.findElement(selector);
		} catch (NoSuchElementException e) {
			// if element not exist return false
			return false;
		}
		return true;
	}

	public static WebElement waitToBeClickable(WebDriver driver, By selector, int waitInterval) {
		WebElement element = (new WebDriverWait(driver, waitInterval)).until(ExpectedConditions.elementToBeClickable(selector));
		return element;
	}
	
	public static WebElement waitForElementPresence(WebDriver driver, By selector, int waitInterval) {
		WebElement element = (new WebDriverWait(driver, waitInterval)).until(ExpectedConditions.presenceOfElementLocated(selector));
		return element;
	}
	
	public static void waitForTitle(WebDriver driver, String title, int waitInterval){
		 (new WebDriverWait(driver, waitInterval)).until(ExpectedConditions.titleIs(title));
	}

}
