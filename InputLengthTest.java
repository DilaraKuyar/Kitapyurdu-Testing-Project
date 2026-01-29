import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class InputLengthTest {

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get("https://www.kitapyurdu.com");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    public void search(String text) {
        WebElement input = driver.findElement(By.id("search-input"));
        input.clear();
        input.sendKeys(text);
        input.sendKeys(Keys.RETURN);
    }

    boolean isSearchPerformed() {
        String currentUrl = driver.getCurrentUrl();
        return !currentUrl.equals("https://www.kitapyurdu.com/");
    }

    boolean isResultFound() {
        if (!isSearchPerformed()) {
            return false;
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("product-cr")));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }



    @Test
    void testE1_TooShort_NoResult() {
        boolean searches = searchFor0Len() || searchFor1Len();
        assertFalse(searches, "Expected no result for short input");
    }

    @Test
    void testE2_ValidLength_RelatedResults() {
        boolean searches = searchFor2Len() && searchFor99Len() && searchFor100Len();
        assertTrue(searches, "Expected related results for valid input");
    }

    //It is impossible to do a search query with negative input because a String cannot has negative length.
    //So the testU1_NegativeLength_Impossible func is a kind of placeholder for negative valued search query.
    @Test
    void testU1_NegativeLength_Impossible() {
        search("");
        assertFalse(isResultFound(), "Expected no result for empty string (interpreted as impossible)");
    }


    @Test
    void testU2_TooLong_ErrorMessage() {
        String longQuery = "a".repeat(165);
        search(longQuery);

        WebElement body = driver.findElement(By.tagName("body"));
        assertTrue(body.getText().contains("hata") || !doesInputLengthMatch(165),
                "Expected an error or no result for overly long input");
    }



    private boolean searchFor2Len() {
        search("ab");
        return isResultFound() && doesInputLengthMatch(2);
    }

    private boolean searchFor1Len() {
        search("a");
        return isResultFound();
    }

    private boolean searchFor0Len() {
        search("");
        return isResultFound();
    }

    private boolean searchFor99Len() {
        String query = "a".repeat(99);
        search(query);
        return doesInputLengthMatch(99);
    }

    private boolean searchFor100Len() {
        String query = "a".repeat(100);
        search(query);
        return doesInputLengthMatch(100);
    }

    private boolean doesInputLengthMatch(int inputLen){
        WebElement searchInput = driver.findElement(By.id("search-input"));
        String inputValue = searchInput.getAttribute("value");
        int length = inputValue.length();
        return length == inputLen;
    }


}
