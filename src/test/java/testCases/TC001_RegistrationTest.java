package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import baseTest.BaseClass;
import pageObjects.HomePage;
import pageObjects.RegistrationPage;

public class TC001_RegistrationTest extends BaseClass {

    @Test(groups= {"Master","Regression"})
    public void verify_Registration() {
    	
        logger.info("Starting TC_001_RegistartionTest");
        try {
            // Initialize HomePage with WebDriver
            HomePage hp = new HomePage(getDriver()); // Use getDriver() method
            hp.clickMyAccount();
            logger.info("Clicked on My Account");
            hp.clickRegister();
            
            logger.info("Clicked on Register");

            // Initialize RegistrationPage with WebDriver
            RegistrationPage rg = new RegistrationPage(getDriver());
            
            logger.info("Providing Customer Details...");
            
            rg.setFirstName(randomString().toUpperCase());
            rg.setLastName(randomString().toUpperCase());
            rg.setEmail(randomString()+"@gmail.com"); // random data
            rg.setTelephone(randomNumber());
            
            String password = randomAlphaNumeric();
            rg.setPassword(password);
            rg.setConfirmPassword(password);
            
            rg.setPrivacyPolicy();
            rg.clickContinue();
              
            logger.info("Validating expected Message...");
            String confmsg = rg.getConfirmationMsg();
            if(confmsg.equals("Your Account Has Been Created!")) {
               Assert.assertTrue(true);
            } else {
            	logger.error("Test Failed..");
            	logger.debug("Debug Logs..");
            	Assert.assertTrue(false);
            }
            
        } catch(Exception e) {
            Assert.fail();
        }
        
        logger.info("Ended TC001_RegistartionTest...");
    }
 

}
