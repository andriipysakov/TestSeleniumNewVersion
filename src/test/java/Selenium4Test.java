import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.logging.Level;

public class Selenium4Test {

    public static WebDriver driver;

    @BeforeAll
    public static void setupDriver() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://www.amazon.com");

    }

    @AfterAll
    public static void closeDriver() {
        // driver.quit();
    }

    @Test
    public void captureElement() throws IOException {
        WebElement logo = driver.findElement(By.xpath("//*[@id='nav-logo-sprites']"));
        File file = logo.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(file, new File("logo.png"));
    }

    @Test
    public void newTabTest() throws IOException {
        WebDriver newTab = driver.switchTo().newWindow(WindowType.TAB);
        driver.get("http://www.amazon.ca");
        WebElement logo = driver.findElement(By.xpath("//*[@id='nav-logo-sprites']"));
        File file = logo.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(file, new File("logo_ca.png"));
        newTab.close();
    }

    @Test
    public void newWindowTest() throws IOException {
        WebDriver newTab = driver.switchTo().newWindow(WindowType.WINDOW);
        driver.get("http://www.amazon.ca");
        WebElement logo = driver.findElement(By.xpath("//*[@id='nav-logo-sprites']"));
        File file = logo.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(file, new File("logo_ca2.png"));
        newTab.close();
    }

    @Test
    public void getSizeTest() {
        WebElement logo = driver.findElement(By.xpath("//*[@id='nav-logo-sprites']"));
        int h = logo.getRect().getDimension().getHeight();
        int w = logo.getRect().getDimension().getWidth();

        System.out.println(h + "x" + w);

        Assertions.assertEquals(50, h);
        Assertions.assertEquals(113, w);


    }

    @Test
    public void relativeLocatorsTest() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[text()='Account & Lists']"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("createAccountSubmit"))).click();

        WebElement nameText = driver.findElement(By.xpath("//*[contains(text(), 'Your name')]"));
        WebElement nameInputBox = driver.findElement(with(By.xpath("//input")).near(nameText, 200));
        nameInputBox.sendKeys("Lev");

        WebElement iconAlert = driver.findElement(By.xpath("//*[@class='a-icon a-icon-alert']"));
        WebElement alertText = driver.findElement(with(By.xpath("//div[@class = 'a-alert-content']")).toRightOf(iconAlert));
        Assertions.assertEquals("Passwords must be at least 6 characters.", alertText.getText());
    }

    @Test
    public void logsTest() {
        LogEntries entries = driver.manage().logs().get(LogType.BROWSER);
        List<LogEntry> logs = entries.getAll();
        for (LogEntry e : logs) {
            System.out.println("Message : " + e.getMessage());
            System.out.println("Level : " + e.getLevel());
            Assertions.assertNotEquals(Level.SEVERE, e.getLevel());
        }
    }


}