package tk.mdogx.bitpanda.test;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import tk.mdogx.bitpanda.test.pages.Account;
import tk.mdogx.bitpanda.test.pages.CreateAccountForm;
import tk.mdogx.bitpanda.test.pages.SignInForm;
import tk.mdogx.bitpanda.test.utils.EmailsGenerator;
import tk.mdogx.bitpanda.test.utils.Utils;

import java.util.concurrent.TimeUnit;

public class LoginFormTest {

    private WebDriver driver;
    private CreateAccountForm createAccountForm;
    private SignInForm signin;
    private Account account;

    @BeforeClass
    public void setup() {

        driver = Utils.getDriver();
        driver.manage().timeouts().implicitlyWait(Utils.TIMEOUT, TimeUnit.SECONDS);

        createAccountForm = new CreateAccountForm(driver);
        signin = new SignInForm(driver);
        account = new Account(driver);

        driver.navigate().to(Utils.BASEURL+"?controller=authentication");
        driver.manage().window().maximize();
    }

    @AfterClass
    public void closeAll() {
        account.getAccountLogout().click();
    }

    @Test(priority = 1)
    public void loginPage() {
        signin.getSignInBtn().click();

        Assert.assertTrue(signin.getSignInForm().isDisplayed());
        Assert.assertTrue(signin.getSignInEmailField().isDisplayed());
        Assert.assertTrue(signin.getSignInPasswordField().isDisplayed());
        Assert.assertTrue(signin.getSignInBtn().isEnabled());
    }

    @Test(priority = 2)
    public void invalidCredentials() {
        // username: email@email.com
        // password: asddsa

        signin.setEmailField("test@gmail.com");
        signin.setPasswordField("asddsa");
        signin.getSignInBtn().click();

        Assert.assertTrue(signin.getAuthenticationFailedError().isDisplayed());
    }

    @Test(priority = 3)
    public void loginWithoutCredentials() {
        signin.setEmailField("");
        signin.setPasswordField("123");
        signin.getSignInBtn().click();

        Assert.assertTrue(signin.getEmailRequiredError().isDisplayed());

        signin.setEmailField("mdogx@gmail.com");
        signin.setPasswordField("");
        signin.getSignInBtn().click();

        Assert.assertTrue(signin.getPasswordRequiredError().isDisplayed());

        signin.setEmailField("");
        signin.setPasswordField("");
        signin.getSignInBtn().click();

        Assert.assertTrue(signin.getEmailRequiredError().isDisplayed());
    }

    @Test(priority = 4)
    public void emailFormat() {
        signin.setEmailField("email");
        signin.getSignInPasswordField().click();

        Assert.assertTrue(signin.getEmailHighlightedRed().isDisplayed());

        signin.setEmailField("email@email");
        signin.getSignInPasswordField().click();

        Assert.assertTrue(signin.getEmailHighlightedRed().isDisplayed());

        signin.setEmailField("email@email.email");
        signin.getSignInPasswordField().click();

        Assert.assertTrue(signin.getEmailHighlightedGreen().isDisplayed());
    }

    @Test(priority = 5)
    public void successfulLogin() {
        signin.setEmailField(EmailsGenerator.getCurrentEmail());
        signin.setPasswordField("test123");
        signin.getSignInBtn().click();

        Assert.assertTrue(createAccountForm.successfullyCreatedAccount().isDisplayed());
    }

}
