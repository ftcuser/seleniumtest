package com.citizant.seleniumtest;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/*
 * This is a standalone project that only run Selenium 
 * test cases. It's not tied with particular project
 * source code.
 * 
 */

public class UserManagerTest {
	  private WebDriver driver;
	  private ScreenshotHelper screenshotHelper;
	  private WebDriverWait driverWait;
	  
	  private WebElement addUserButton;
	  private WebElement emailField;
	  private WebElement firstNameField;
	  private WebElement lastNameField;
	  private WebElement addButton;
	  
	  private String seleniumHub = "http://50.19.179.31:4444/wd/hub";
	  private String baseUrl =  "http://50.19.179.31:9090/jenkinsmanager/index.html";

	  
	  @Before
	  public void openBrowser() {
	  
	   // driver = new ChromeDriver();
	    //driver = new HtmlUnitDriver();
	    //((HtmlUnitDriver)driver).setJavascriptEnabled(true);
		  
		//try to get Selenium HUB and bas test URL from JVM parameters
		//This should set on Jenkins
		String hub =  System.getProperty("selenium.hub");
		if(hub == null) {
			hub  = seleniumHub;
		}
		
		String base = System.getProperty("app.baseurl");
		if(base == null) {
			base = baseUrl;
		}
		System.out.println("Selenium HUB : "  + hub);
		System.out.println("The app URL : "  + base);
		  
	    URL hubUrl = null;
	    try{
	    	hubUrl = new URL(hub);
	    }catch(Exception e){
	    	
	    }
	    
	    Capabilities cap = DesiredCapabilities.firefox();
	    driver = new RemoteWebDriver(hubUrl, cap);
	    driverWait = new WebDriverWait(driver, 30);
	    driver.get(base);
	   // screenshotHelper = new ScreenshotHelper();
	  }
	  
	  @After
	  public void saveScreenshotAndCloseBrowser() throws IOException {
	   // screenshotHelper.saveScreenshot("screenshot.png");
	    driver.quit();
	  }
	  
	  @Test
	  public void pageTitleAfterSearchShouldBeginWithDrupal() throws IOException {
		System.out.println("Page Title : " + driver.getTitle());
	    assertEquals("The page title should equal user manager at the start of the test.", "User Manager", driver.getTitle());
	    
	    addUserButton = driver.findElement(By.id("btnAddUser"));
	    
	    if (addUserButton.isDisplayed()) {
	    	System.out.println("Add User button is displayed");
	    	addUserButton.click();
	    	
	    	driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
	    	emailField = driver.findElement(By.id("email"));
	    	emailField.sendKeys("abc@xyz.com");
	    	
	    	firstNameField = driver.findElement(By.id("firstName"));
	    	firstNameField.sendKeys("Aaron");
	    	
	    	lastNameField = driver.findElement(By.id("lastName"));
	    	lastNameField.sendKeys("Smith");
	    	try{
	    		Thread.sleep(2000);
	    	} catch (Exception e){
	    		
	    	}
	    	addButton = driver.findElement(By.id("btnSubmit"));
	    	addButton.click();  
	    	try{
	    		Thread.sleep(2000);
	    	} catch (Exception e){
	    		
	    	}
	    	driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("d_"+"abc@xyz.com")));
	    	
	    	WebElement deleteButton = driver.findElement(By.id("d_"+"abc@xyz.com"));
	    	
	    	deleteButton.click();
	    	
	    	try{
	    		Thread.sleep(2000);
	    	} catch (Exception e){
	    		
	    	}
	    }
	    /*
	    WebElement searchField = driver.findElement(By.name("q"));
	    searchField.sendKeys("Drupal!");
	    searchField.submit();
	    */
	  }
	  
	  private class ScreenshotHelper {
	  
	    public void saveScreenshot(String screenshotFileName) throws IOException {
	      File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	      FileUtils.copyFile(screenshot, new File(screenshotFileName));
	    }
	  }
}
