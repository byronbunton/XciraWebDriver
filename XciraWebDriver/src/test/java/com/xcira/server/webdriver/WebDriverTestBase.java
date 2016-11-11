package com.xcira.server.webdriver;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

public class WebDriverTestBase {
	
	static protected Actions actions;
	static protected WebDriver driver;
	static protected String baseUrl;
	static protected boolean acceptNextAlert = true;
	static protected StringBuffer verificationErrors = new StringBuffer();
	static protected List<WebElement> pageOptionButtons;

	protected WebElement verifyAttributeValues(By by, Map<String,String> attributeValues) {
		
		WebElement element = driver.findElement(by);
		
		for(String key : attributeValues.keySet()) {
		
			assertEquals(attributeValues.get(key), element.findElement(By.name("creditLimit")).getAttribute(key));
		}
		
		return element.findElement(By.name("creditLimit"));
	}
	
	protected List<WebElement> getPageOptionButtons() {
		
		return driver.findElement(By.cssSelector("div.pageOptions")).findElements(By.tagName("button"));
	}
	
	protected void clickPageOptionButton(String buttonText) {
		
		for(WebElement button : pageOptionButtons) {
			
			if(button.getText().equals(buttonText)) {
			
				actions.click(button).perform();
			}
		}
	}

	protected boolean isAlertPresent() {

		try {
		  
			driver.switchTo().alert();
		
			return true;
		
		} catch (NoAlertPresentException e) {
			  
			return false;
		}
	}

	protected String closeAlertAndGetItsText() {
	
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
	  
	protected boolean isElementPresent(By by) {
		  
		try {
			  
			driver.findElement(by);
		      
			return true;
		      
		} catch (NoSuchElementException e) {
			  
		   return false;
		}
	}
	
	protected boolean isElementPresent(String path) throws Exception {
		
		try {
			  
			find(path);
		      
			return true;
		      
		} catch (NoSuchElementException e) {
			  
		   return false;
		}
	}
	
	protected boolean isEditable(String path) throws Exception {
		
		return find(path).isEnabled();
	}
	
	protected boolean isVisible(String path) throws Exception {
		
		return find(path).isDisplayed();
	}
	
	protected void pause(long pauseTime) throws Exception {
		
		Thread.sleep(pauseTime);
	}
	
	protected WebElement find(String path) throws Exception {
		
		By by;
		
		String array[] = path.split("=");
		
		switch (array[0]) {
		
			case "id" : 	by = By.id(array[1]);
							break;
			
			case "name" : 	by = By.name(array[1]);
							break;
							
			case "link" :	by = By.linkText(array[1]);
							break;
							
			case "css"  : 	by = By.cssSelector(array[1]);
							break;
							
			default : throw new Exception("invalid search type");
		}
		
		
		return driver.findElement(by);
	}
	
	protected void click(String path) throws Exception {
		
		find(path).click();
	}
	
	protected void type(String path, String content) throws Exception {
		
		find(path).sendKeys(content);
	}
	
	protected void open(String url) {
		
		driver.get(url);
	}
	
	protected void close() {
		
		driver.close();
	}
	
	protected void setupWebDriver(String browserName, String url, String geckoDriverPath, int implicitlyWait) {
		
		if(browserName.equalsIgnoreCase("ie")) {
			
			driver = new InternetExplorerDriver(); 
			
		} else if(browserName.equalsIgnoreCase("chrome")) {
			
			driver = new ChromeDriver();
			
		} else if (browserName.equalsIgnoreCase("safari")) {
			
			driver = new SafariDriver();
			
		} else {
			
			if(geckoDriverPath != null) {
				
				DesiredCapabilities desiredCapability = DesiredCapabilities.firefox();
				
				System.out.println("geckopath set to (" + geckoDriverPath + ")");
				System.setProperty("webdriver.gecko.driver", geckoDriverPath);
				
				driver = new FirefoxDriver(desiredCapability);
				
			} else {
				
				driver = new FirefoxDriver();
			}

			
		}
		
		baseUrl = url;
		
		driver.manage().timeouts().implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
		
		actions = new Actions(driver);
	}
}