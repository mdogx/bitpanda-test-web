package tk.mdogx.bitpanda.test;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import tk.mdogx.bitpanda.test.pages.*;
import tk.mdogx.bitpanda.test.utils.EmailsGenerator;
import tk.mdogx.bitpanda.test.utils.Utils;

import java.util.concurrent.TimeUnit;

public class CreateAccountFormTest {

	private WebDriver driver;

	private Homepage homepage;
	private CreateAccount createAccount;
	private CreateAccountForm createAccountForm;
	private SignInForm signin;
	private Account account;

	@BeforeClass
	public void setup() {
		driver = Utils.getDriver();
		driver.manage().timeouts().implicitlyWait(Utils.TIMEOUT, TimeUnit.SECONDS);

		homepage = new Homepage(driver);
		createAccount = new CreateAccount(driver);
		createAccountForm = new CreateAccountForm(driver);
		signin = new SignInForm(driver);
		account = new Account(driver);

		driver.navigate().to(Utils.BASEURL);
		driver.manage().window().maximize();
	}

	@AfterClass
	public void closeAll() {
		account.getAccountLogout().click();
	}

	@Test(priority = 1)
	public void authenticationPage() {
		homepage.getSignInBtn().click();

		Assert.assertTrue(createAccount.getCreateAccountForm().isDisplayed());
		Assert.assertTrue(createAccount.getCreatAccountEmailField().isDisplayed());
		Assert.assertTrue(createAccount.getCreateAccountBtn().isDisplayed());
		Assert.assertTrue(signin.getSignInForm().isDisplayed());
	}

	@Test(priority = 2)
	public void authenticationPageEmailField() {
		// Without email
		createAccount.getCreateAccountBtn().click();
		Assert.assertTrue(createAccount.getEmailErrorMessage().isDisplayed());

		// Wrong email format
		createAccount.setCreateAccountEmailField("wrong");
		createAccount.getCreateAccountBtn().click();
		Assert.assertTrue(createAccount.getEmailErrorMessage().isDisplayed());
		Assert.assertTrue(createAccount.getEmailFieldHighlightedRed().isDisplayed());

		// Registered email
		createAccount.setCreateAccountEmailField("email@email.email");
		createAccount.getCreateAccountBtn().click();
		Assert.assertTrue(createAccount.getEmailBeenRegistered().isDisplayed());

		// Correct email
		createAccount.setCreateAccountEmailField("mapko123ct@gmail.com");
		createAccount.getCreateAccountBtn().click();
		Assert.assertTrue(createAccountForm.getAccountCreationForm().isDisplayed());
	}

	@Test(priority = 3)
	public void personalInfoFields() {
		createAccountForm.setCustomerFirstNameField("Maksim");
		createAccountForm.setCustomerLastNameField("Brigadirenko");
		createAccountForm.setCustomerEmailField("mdogx@gmail.com");
		createAccountForm.setCustomerPasswordField("test123");

		createAccountForm.getAccountCreationForm().click();

		Assert.assertTrue(createAccountForm.getFirstNameHighlightedGreen().isDisplayed());
		Assert.assertTrue(createAccountForm.getLastNameHighlightedGreen().isDisplayed());
		Assert.assertTrue(createAccountForm.getEmailHighlightedGreen().isDisplayed());
		Assert.assertTrue(createAccountForm.getPasswordHighlightedGreen().isDisplayed());

		// Without values
		createAccountForm.setCustomerFirstNameField("");
		createAccountForm.setCustomerLastNameField("");
		createAccountForm.setCustomerEmailField("");
		createAccountForm.setCustomerPasswordField("");

		createAccountForm.getAccountCreationForm().click();

		Assert.assertTrue(createAccountForm.getFirstNameHighlightedRed().isDisplayed());
		Assert.assertTrue(createAccountForm.getLastNameHighlightedRed().isDisplayed());
		Assert.assertTrue(createAccountForm.getEmailHighlightedRed().isDisplayed());
		Assert.assertTrue(createAccountForm.getPasswordHighlightedRed().isDisplayed());
	}

	@Test(priority = 4)
	public void requiredFieldsEmpty() {
		createAccountForm.getAddressAliasField().clear();
		createAccountForm.setCustomerEmailField("");
		createAccountForm.selectCountry("-");
		createAccountForm.getRegisterBtn().click();

		Assert.assertTrue(createAccountForm.getPhoneNumberError().isDisplayed());
		Assert.assertTrue(createAccountForm.getLastNameError().isDisplayed());
		Assert.assertTrue(createAccountForm.getFirstNameError().isDisplayed());
		Assert.assertTrue(createAccountForm.getEmailRequiredError().isDisplayed());
		Assert.assertTrue(createAccountForm.getPasswordRequiredError().isDisplayed());
		Assert.assertTrue(createAccountForm.getCountryRequiredError().isDisplayed());
		Assert.assertTrue(createAccountForm.getAddressRequiredError().isDisplayed());
		Assert.assertTrue(createAccountForm.getAddressAliasRequiredError().isDisplayed());
		Assert.assertTrue(createAccountForm.getCityRequiredError().isDisplayed());
		Assert.assertTrue(createAccountForm.getCountryUnselectedError().isDisplayed());

		createAccountForm.selectCountry("United States");
		createAccountForm.getRegisterBtn().click();

		Assert.assertTrue(createAccountForm.getStateRequredError().isDisplayed());
		Assert.assertTrue(createAccountForm.getPostalCodeError().isDisplayed());
	}

	@Test(priority = 5)
	public void requiredFieldsInputFormat() throws Exception {
		// Wrong format
		createAccountForm.setCustomerEmailField("mdogx@gmail");
		createAccountForm.setCustomerPasswordField("qwe");
		createAccountForm.setPostalCodeField("qwe");
		createAccountForm.setHomePhoneField("qwe");
		createAccountForm.setMobilePhoneField("qwe");

		createAccountForm.getRegisterBtn().click();

		Assert.assertTrue(createAccountForm.getEmailInvalidError().isDisplayed());
		Assert.assertTrue(createAccountForm.getPasswordInvalidError().isDisplayed());
		Assert.assertTrue(createAccountForm.getPostalCodeError().isDisplayed());
		Assert.assertTrue(createAccountForm.getHomePhoneInvalidError().isDisplayed());
		Assert.assertTrue(createAccountForm.getMobilePhoneInvalidError().isDisplayed());

		// Correct format
		createAccountForm.setCustomerEmailField("email1@gmail.com");
		createAccountForm.setCustomerPasswordField("qwe");
		createAccountForm.setPostalCodeField("21000");
		createAccountForm.setHomePhoneField("123");
		createAccountForm.setMobilePhoneField("123");

		Assert.assertTrue(createAccountForm.getEmailInvalidError().isDisplayed());
		Assert.assertTrue(createAccountForm.getPasswordInvalidError().isDisplayed());
		Assert.assertTrue(createAccountForm.getPostalCodeError().isDisplayed());
		Assert.assertTrue(createAccountForm.getHomePhoneInvalidError().isDisplayed());
		Assert.assertTrue(createAccountForm.getMobilePhoneInvalidError().isDisplayed());
	}

	@Test(priority = 6)
	public void createAccountSuccessfully() {
		// Required fields filled
		createAccountForm.setCustomerFirstNameField("John");
		createAccountForm.setCustomerLastNameField("Doe");
		createAccountForm.setCustomerPasswordField("test123");
		createAccountForm.selectCustomerDateOfBirthDay("20");
		createAccountForm.selectCustomerDateOfBirthMonth("10");
		createAccountForm.selectCustomerDateOfBirthYear("2000");
		createAccountForm.setAddressField("Centar");
		createAccountForm.setCityField("Krakow");
		createAccountForm.selectState("7");
		createAccountForm.setPostalCodeField("21000");
		createAccountForm.setHomePhoneField("056");
		createAccountForm.setMobilePhoneField("066");
		createAccountForm.setAddressAliasField("Address");
		createAccountForm.getRegisterBtn().click();

		Assert.assertTrue(createAccountForm.getEmailBeenRegistered().isDisplayed());

		createAccountForm.setCustomerEmailField(EmailsGenerator.getNextEmail());
		createAccountForm.setCustomerPasswordField("test123");
		createAccountForm.getRegisterBtn().click();

		Assert.assertTrue(createAccountForm.successfullyCreatedAccount().isDisplayed());
	}
}
