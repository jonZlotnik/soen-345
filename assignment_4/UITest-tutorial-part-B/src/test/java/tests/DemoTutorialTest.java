package test.java.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;

import test.java.seleniumtutorial.EnvironmentManager;
import test.java.seleniumtutorial.RunEnvironment;

public class DemoTutorialTest {
	private WebDriver driver;
	 
    @Before
    public void startBrowser() {
        EnvironmentManager.initWebDriver();
        this.driver = RunEnvironment.getWebDriver();
    }

    @Test
    public void demoTest() {

    	//Applied wait time
    	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //maximize window
        driver.manage().window().maximize();

        //open browser with desried URL
        driver.get("https://www.google.com");
        
        //basic assertion
        assertThat(driver.getTitle(), is("Google"));
        
    }

    @Test
    public void testcase2() {
        driver.get("https://www.google.ca/");
        driver.manage().window().setSize(new Dimension(518, 439));
        driver.findElement(By.name("q")).click();
        driver.findElement(By.name("q")).sendKeys("arbitrary stuff");
        driver.findElement(By.name("q")).sendKeys(Keys.ENTER);
        {
            List<WebElement> elements = driver.findElements(By.linkText("Images"));
            assert(elements.size() > 0);
        }
        {
            List<WebElement> elements = driver.findElements(By.linkText("Shopping"));
            assert(elements.size() > 0);
        }
        {
            List<WebElement> elements = driver.findElements(By.linkText("Videos"));
            assert(elements.size() > 0);
        }
        {
            List<WebElement> elements = driver.findElements(By.linkText("News"));
            assert(elements.size() > 0);
        }
        {
            List<WebElement> elements = driver.findElements(By.id("abar_button_opt"));
            assert(elements.size() > 0);
        }
    }

    @Test
    public void testcase3() {
        driver.get("https://en.m.wikipedia.org/wiki/Software_engineering");
        {
            List<WebElement> elements = driver.findElements(By.cssSelector(".page-actions-menu"));
            assert(elements.size() > 0);
        }
        {
            List<WebElement> elements = driver.findElements(By.id("ca-watch"));
            assert(elements.size() > 0);
        }
    }

    @Test
    public void testcase4() {
        driver.get("https://en.wikipedia.org/wiki/Software_engineering");
        {
            List<WebElement> elements = driver.findElements(By.cssSelector(".page-actions-menu"));
            assert(elements.size() == 0);
        }
        {
            List<WebElement> elements = driver.findElements(By.id("ca-watch"));
            assert(elements.size() == 0);
        }
    }

    @After
    public void tearDown() {
        //shut down the driver
        EnvironmentManager.shutDownDriver();
    }
}
