package com.fitchconnect.api;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DeskTop_TestCases {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeMethod
	public void setUp() throws Exception {
		// driver = new FirefoxDriver();
		System.setProperty("webdriver.chrome.driver",
				"C://Users//aislam//Desktop//AutomatoinWorkSpace//regression//chromedriver.exe");
		driver = new ChromeDriver();
		baseUrl = "https://login.fitchconnect.com/jsp/fitchconnect/FitchConnectLoginController.faces";
		driver.get(baseUrl);
		Thread.sleep(1000);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		 login();
	}

	
	public void login() throws Exception {
		


		WebElement UsrName = driver.findElement(By.id("loginForm:username"));
		WebElement PassWord = driver.findElement(By.id("loginForm:password"));

		if (UsrName.isDisplayed()) {
			UsrName.sendKeys("aminul.islam@fitchsolutions.com");

			PassWord.clear();
			PassWord.sendKeys("fr2016");
			driver.findElement(By.id("loginForm:login-submit")).click();

		} else {
			System.err.println("cannot find User name Element ");
		}
	}

	@Test
	public void BankOfAmerica() throws InterruptedException {
		Thread.sleep(1000);
		// driver.findElement(By.xpath("//input[@type='text']")).clear();
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys("Bank of America Corporation");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("span.up-item")).click();

		findingBank_oF_AC_Entity();
		verifyingCHdata();
        //driver.quit();
	
	}

	@Test
	public void ABSA_BankLimited() throws InterruptedException {
		Thread.sleep(1000);

		driver.findElement(By.xpath("//input[@type='text']")).clear();
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys("Absa Bank Limited");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.up-item")).click();

		findingBank_oF_ABSA_Entity();
		driver.quit();
		
	}

	public void findingBank_oF_AC_Entity() throws InterruptedException {

		String tittleText = driver.findElement(By.xpath(".//*[@id='entityInsuranceDetail']/div/div/div/div[1]"))
				.getText();

		Assert.assertEquals("ENTITY DETAIL", tittleText);

		WebElement EntityProfile = driver.findElement(By.xpath(".//*[@id='entityInsurance']/div/div[2]/a"));
		if (EntityProfile.isEnabled()) {
			Thread.sleep(5000);
			EntityProfile.click();
		} else {
			System.err.println("view entity is Not present");
		}

		Thread.sleep(4000);

		String Address = driver
				.findElement(By.xpath(".//*[@id='corporateSummaryRegion']/div/div/div[1]/div[3]/div[2]/div[1]"))
				.getText();

		Assert.assertTrue(Address.contains("Headquarters"));
		Assert.assertTrue(Address.contains("Bank of America Corporate Center"));
		Assert.assertTrue(Address.contains("100 North Tryon Street"));
		Assert.assertTrue(Address.contains("Charlotte, UNITED STATES 28255"));
		Assert.assertTrue(Address.contains("https://www.bankofamerica.com"));

		WebElement OwnrShip = driver.findElement(By.xpath(".//*[@id='hierarchyTabs']/div/div[1]/div/div[1]/a"));
		if (OwnrShip.isDisplayed()) {
			OwnrShip.click();

		} else {
			System.err.println("OwnerShip Link is not Available");
		}

		driver.findElement(By.xpath(".//*[@id='hierarchyTabs']/div/div[1]/div/div[2]/a")).click();

		WebElement CorpoRteHierchy = driver.findElement(By.xpath(".//*[@id='hierarchyTabs']/div/div[1]/div/div[3]/a"));

		if (CorpoRteHierchy.isDisplayed()) {
			CorpoRteHierchy.click();
		} else {
			System.err.println("CORPORATE HIERARCHY link is not available ");
		}

		WebElement ViewHistory = driver
				.findElement(By.xpath(".//*[@id='corporateSummaryRegion']/div/div/div[1]/div[2]/div[2]/a"));

		if (ViewHistory.isDisplayed()) {

			ViewHistory.click();

		} else {
			System.err.println("viewHistory link is not Available");
		}

	}

	public void findingBank_oF_ABSA_Entity() throws InterruptedException {

		String tittleText = driver.findElement(By.xpath(".//*[@id='entityInsuranceDetail']/div/div/div/div[1]"))
				.getText();

		Assert.assertEquals("ENTITY DETAIL", tittleText);

		WebElement EntityProfile = driver.findElement(By.xpath(".//*[@id='entityInsurance']/div/div[2]/a"));
		if (EntityProfile.isEnabled()) {
			Thread.sleep(5000);
			EntityProfile.click();
		} else {
			System.err.println("view entity is Not present");
		}

		Thread.sleep(4000);

		String Address = driver
				.findElement(By.xpath(".//*[@id='corporateSummaryRegion']/div/div/div[1]/div[3]/div[2]/div[1]"))
				.getText();

		Assert.assertTrue(Address.contains("Headquarters"));
		Assert.assertTrue(Address.contains("7th Floor Barclays Towers West"));
		Assert.assertTrue(Address.contains("15 Troye Street"));
		Assert.assertTrue(Address.contains("Johannesburg, SOUTH AFRICA 2001"));
		Assert.assertTrue(Address.contains("https://www.absa.co.za"));

		WebElement OwnrShip = driver.findElement(By.xpath(".//*[@id='hierarchyTabs']/div/div[1]/div/div[1]/a"));
		if (OwnrShip.isDisplayed()) {
			OwnrShip.click();

		} else {
			System.err.println("OwnerShip Link is not Available");
		}

		driver.findElement(By.xpath(".//*[@id='hierarchyTabs']/div/div[1]/div/div[2]/a")).click();

		WebElement CorpoRteHierchy = driver.findElement(By.xpath(".//*[@id='hierarchyTabs']/div/div[1]/div/div[3]/a"));

		if (CorpoRteHierchy.isDisplayed()) {
			CorpoRteHierchy.click();
		} else {
			System.err.println("CORPORATE HIERARCHY link is not available ");
		}

		WebElement BarclaysBank = driver.findElement(By.cssSelector("button.mr--.js-expand"));
		if (BarclaysBank.isDisplayed()) {
			BarclaysBank.click();
			Thread.sleep(1500);
			driver.findElement(By.linkText("Barclays Bank PLC")).click();
			Thread.sleep(1000);

		} else {
			System.err.println("Barclays Bank PLC link is not avaiable");
		}

	}

	public void verifyingCHdata() {

		driver.findElement(By.xpath(".//*[@id='hierarchyTabs']/div/div[1]/div/div[3]/a")).click();

		driver.findElement(By.cssSelector("button.mr--.js-expand")).click();
		driver.findElement(By.xpath("//div[@id='hierarchyTabs']/div/div[2]/div/table/tbody/tr[8]/td/div/button"))
				.click();
		driver.findElement(By.xpath("//div[@id='hierarchyTabs']/div/div[2]/div/table/tbody/tr[14]/td/div/button"))
				.click();
		driver.findElement(By.linkText("BAC CAPITAL TRUST VI")).click();

		String CpitalTrst = driver.findElement(By.xpath(".//*[@id='entityHeader']/div/div[1]/span")).getText();

		Assert.assertEquals(CpitalTrst, "BAC CAPITAL TRUST VI");

	}

	
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		// driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			// fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
