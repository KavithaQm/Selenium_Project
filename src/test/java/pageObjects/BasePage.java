package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import baseTest.BaseClass;

public class BasePage {
    
    WebDriver driver;   // WebDriver variable
    
    // Constructor that accepts a WebDriver instance
    public BasePage(WebDriver driver) {   
        this.driver = driver; // Assign the WebDriver passed from the child class
        PageFactory.initElements(driver, this);  // Initialize elements
    }
}
