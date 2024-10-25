package baseTest;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public class BaseClass {

    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    public Logger logger;
    public Properties p;

    @BeforeClass(groups= {"Sanity", "Regression", "Master", "Datadriven"})
    @Parameters({"os", "browser"})
    public void setUp(String os, String br) throws IOException {
        FileReader file = new FileReader("./src/test/resources/config.properties");
        p = new Properties();
        p.load(file);

        logger = LogManager.getLogger(this.getClass());

        switch (br.toLowerCase()) {
            case "chrome":
                driver.set(new ChromeDriver());
                break;
            case "edge":
                driver.set(new EdgeDriver());
                break;
            case "firefox":
                driver.set(new FirefoxDriver());
                break;
            default:
                System.out.println("Invalid Browser name..");
                return;
        }

        getDriver().manage().deleteAllCookies();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        getDriver().get(p.getProperty("appURL"));
        getDriver().manage().window().maximize();
    }

    public WebDriver getDriver() {
        return driver.get();
    }

    @AfterClass(groups= {"Sanity", "Regression", "Master", "Datadriven"})
    public void tearDown() {
        getDriver().quit();
        driver.remove();
    }

    public String randomString() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public String randomNumber() {
        return RandomStringUtils.randomNumeric(10);
    }

    public String randomAlphaNumeric() {
        return RandomStringUtils.randomAlphabetic(3) + "@" + RandomStringUtils.randomNumeric(3);
    }

    // Capture screenshot as Base64
    public String captureScreenBase64() {
        TakesScreenshot takesScreenshot = (TakesScreenshot) getDriver();
        return takesScreenshot.getScreenshotAs(OutputType.BASE64);
    }
}
