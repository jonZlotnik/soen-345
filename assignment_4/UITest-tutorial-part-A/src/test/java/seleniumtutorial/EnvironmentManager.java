package test.java.seleniumtutorial;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class EnvironmentManager {

	
	public static void initWebDriver() {
		//setting the driver executable
		//don't forget to change this if you change the browser driver location
		//also don't forget to download a driver...
		System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
		
		//Initiating your chromedriver
		WebDriver driver=new ChromeDriver();
		
		RunEnvironment.setWebDriver(driver);
	}
	
	public static void shutDownDriver() {
	    RunEnvironment.getWebDriver().quit();
	}
}
