import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class KitapyurduTest {

        private WebDriver driver;
        private JavascriptExecutor js;
        private Map<String, Object> vars;

        @Before
        public void setUp() {
            driver = new FirefoxDriver();
            js = (JavascriptExecutor) driver;
            vars = new HashMap<>();
        }

        @After
        public void tearDown() {
            //driver.quit();
        }

        @Test
        public void loginValidUser() {
            driver.get("https://www.kitapyurdu.com/");
            driver.manage().window().maximize();

            js.executeScript("window.scrollTo(0,47)");
            driver.findElement(By.linkText("Giriş Yap")).click();

            driver.findElement(By.id("login-email")).sendKeys("test_user@example.com"); // Placeholder for test user email
            driver.findElement(By.id("login-password")).sendKeys("password"); // Use environment variables or placeholders for security
            driver.findElement(By.id("login-button")).click();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement userArea = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".user")));

            assertNotNull(userArea);
        }
    }


