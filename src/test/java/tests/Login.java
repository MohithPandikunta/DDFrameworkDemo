package tests;

import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.hc.core5.util.Timeout;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializer;

import base.Base;
import io.github.bonigarcia.wdm.WebDriverManager;
import util.DataUtil;
import util.MyXLSReader;

@Test
public class Login extends Base {
	WebDriver  driver;
	MyXLSReader exceReader;
	
	@AfterMethod
	public void teardown() {
		driver.quit();
		
	}
	public void testLogin(HashMap<String, String> hMap) {
		if(!DataUtil.isRunnable(exceReader, "LoginTest", "exceReader") || hMap.get("Runmode").equals("N"))  {
			throw new SkipException("Run mode is set to N, hence not executed ");
		}
		
		driver = openBrowser(hMap.get("Browser"));
		
		
		driver.get("https://tutorialsninja.com/demo/");
		driver.findElement(By.xpath("//span[normalize-space()='My Account']")).click();
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.id("input-email")).sendKeys(hMap.get("Username"));
		driver.findElement(By.id("input-password")).sendKeys(hMap.get("Password"));
		driver.findElement(By.xpath("//input[@value='Login']")).click();
		
		String expectedResult = hMap.get("ExpectedResult");
		boolean expectedConvertedResult = false;
		
		if(expectedResult.equalsIgnoreCase("Success")) {
			expectedConvertedResult = true;
		} else if(expectedResult.equalsIgnoreCase("Failure")) {
			expectedConvertedResult = false;
		}
		boolean actualResult = false;
		try {
		 actualResult = driver.findElement(By.linkText("Edit your account information")).isDisplayed();
		}catch (Throwable e) {
			actualResult = false;
			
		}
		Assert.assertEquals(actualResult, expectedConvertedResult);
		
		
		driver.quit();
		
	}
	@DataProvider
	public Object[][] datasupplier() {
		Object[][] data = null;
		try {
			 exceReader = new MyXLSReader("src\\test\\resources\\TutorialsNinja.xlsx");
			DataUtil.getTestData(exceReader, "LoginTest", "Data");
		} catch(Throwable e) {
			e.printStackTrace();
		}
		return data;
	}

}
