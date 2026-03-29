package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardOrderTest {

    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldSubmitFormSuccessfully() {
        driver.findElement(By.cssSelector("[data-test-id=name] input"))
                .sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input"))
                .sendKeys("+79001234567");
        driver.findElement(By.cssSelector("[data-test-id=agreement]"))
                .click();
        driver.findElement(By.cssSelector("button.button")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]"))
                .getText()
                .strip();

        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", actual);
    }

    @Test
    public void shouldShowErrorWhenNameIsInLatin() {
        driver.findElement(By.cssSelector("[data-test-id=name] input"))
                .sendKeys("Ivan Ivanov");
        driver.findElement(By.cssSelector("[data-test-id=phone] input"))
                .sendKeys("+79001234567");
        driver.findElement(By.cssSelector("[data-test-id=agreement]"))
                .click();
        driver.findElement(By.cssSelector("button.button")).click();

        String errorText = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"))
                .getText()
                .strip();
        assertEquals("Имя и Фамилия указаны неверно. Допустимы только русские буквы, пробелы и дефисы.", errorText);
    }

    @Test
    public void shouldShowErrorWhenPhoneHasNoPlus() {
        driver.findElement(By.cssSelector("[data-test-id=name] input"))
                .sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input"))
                .sendKeys("79001234567");
        driver.findElement(By.cssSelector("[data-test-id=agreement]"))
                .click();
        driver.findElement(By.cssSelector("button.button")).click();

        String errorText = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub"))
                .getText()
                .strip();

        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", errorText);
    }

    @Test
    public void shouldShowErrorWhenCheckboxNotChecked() {
        driver.findElement(By.cssSelector("[data-test-id=name] input"))
                .sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input"))
                .sendKeys("+79001234567");

        driver.findElement(By.cssSelector("button.button")).click();

        WebElement checkboxError = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid"));
        Assertions.assertTrue(checkboxError.isDisplayed());
    }

    @Test
    public void shouldShowErrorWhenNameIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id=phone] input"))
                .sendKeys("+79001234567");
        driver.findElement(By.cssSelector("[data-test-id=agreement]"))
                .click();
        driver.findElement(By.cssSelector("button.button")).click();

        String errorText = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"))
                .getText()
                .strip();

        assertEquals("Поле обязательно для заполнения", errorText);
    }

    @Test
    public void shouldShowErrorWhenPhoneIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id=name] input"))
                .sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=agreement]"))
                .click();
        driver.findElement(By.cssSelector("button.button")).click();

        String errorText = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub"))
                .getText()
                .strip();

        assertEquals("Поле обязательно для заполнения", errorText);
    }
}
