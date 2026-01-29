import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class MaxPriceHorrorTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static final int WAIT_TIMEOUT_SECONDS = 10;

    @BeforeAll
    public static void setup() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
        driver.manage().window().maximize();
    }

    @BeforeEach
    public void goToHorrorGenre() {
        driver.get("https://www.kitapyurdu.com/index.php?route=product/search&filter_name=korku");
        wait.until(ExpectedConditions.urlContains("filter_name=korku"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".product-cr")));
    }

    @AfterAll
    public static void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void setMaxPrice(String maxPrice) {
        WebElement maxPriceInput = findMaxPriceInput();

        scrollToElement(maxPriceInput);
        clearAndSetPrice(maxPriceInput, maxPrice);

        waitForLoadingToDisappear();
        waitForResultsToLoad();
    }

    private WebElement findMaxPriceInput() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("selected_sell_price_max")));
        } catch (TimeoutException e) {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("selected_sell_price_max")));
        }
    }

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    private void clearAndSetPrice(WebElement maxPriceInput, String maxPrice) {
        maxPriceInput.clear();
        String formattedPrice = maxPrice.replace('.', ',');
        maxPriceInput.sendKeys(formattedPrice);
        maxPriceInput.sendKeys(Keys.RETURN);
    }

    private void waitForLoadingToDisappear() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("loading")));
        } catch (TimeoutException ignored) {
        }
    }

    private void waitForResultsToLoad() {
        wait.until(driver -> driver.findElements(By.cssSelector(".product-cr")).size() > 0 ||
                driver.findElements(By.cssSelector(".no-result")).size() > 0);
    }

    private int getResultCount() {
        WebElement productsCountElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#faceted-search-list-total h2"))
        );

        String text = productsCountElement.getText();
        System.out.println("Product count text: " + text);

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+)");
        java.util.regex.Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            System.out.println("No number found in product count text");
            return 0;
        }
    }

    private String getMaxPriceInputValue() {
        java.util.List<WebElement> elements = driver.findElements(By.id("selected_sell_price_max"));
        if (elements.isEmpty()) {
            return null;
        }
        return elements.get(0).getAttribute("value");
    }

    @Test
    public void testE1_MaxPriceWithinRange() {
        setMaxPrice("1");
        Assertions.assertEquals(19, getResultCount());

        setMaxPrice("2");
        Assertions.assertEquals(20, getResultCount());

        setMaxPrice("958");
        Assertions.assertEquals(492, getResultCount());

        setMaxPrice("958.40");
        Assertions.assertEquals(493, getResultCount());
    }

    @Test
    public void testE2_MaxPriceAboveRange() {
        setMaxPrice("986");
        Assertions.assertEquals(493, getResultCount());

        setMaxPrice("999999999999999999998");
        Assertions.assertEquals(493, getResultCount());

        setMaxPrice("9999999999999999999999");
        Assertions.assertEquals(493, getResultCount());
    }

    @Test
    public void testU1_NegativeMaxPrice_Error() {
        String price = "-5";
        setMaxPrice(price);

        wait.until(driver -> {
            String currentValue = getMaxPriceInputValue();
            return currentValue == null || !price.equals(currentValue);
        });

        String currentValue = getMaxPriceInputValue();
        if (currentValue == null) {
            Assertions.assertTrue(true);
        } else {
            Assertions.assertFalse(price.equals(currentValue),
                    "Expected the input to reject the negative price and change its value");
            Assertions.assertEquals("5", currentValue,
                    "Expected the input value to be corrected to 5");
        }
    }



    @Test
    public void testU2_TooLargeMaxPrice_TypeError() {
        String price = "100000000000000000000000";
        setMaxPrice(price);

        wait.until(driver -> {
            String currentValue = getMaxPriceInputValue();
            return currentValue == null || !price.equals(currentValue);
        });

        String currentValue = getMaxPriceInputValue();
        if (currentValue == null) {
            Assertions.assertTrue(true);
        } else {
            Assertions.assertFalse(price.equals(currentValue),
                    "Expected the input to reject the too large price and change its value");
        }
    }

}