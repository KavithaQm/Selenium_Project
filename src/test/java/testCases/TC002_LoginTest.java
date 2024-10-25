package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import baseTest.BaseClass;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;

public class TC002_LoginTest extends BaseClass{
	
	@Test(groups= {"Sanity","Master"})
	public void verify_login()
	{
		logger.info("Starting TC_002_LoginTest");
	try
	   {
		HomePage hp=new HomePage(getDriver()); //here driver is coming from base class
		hp.clickMyAccount();
		hp.clickLogin();
		
		LoginPage lp=new LoginPage(getDriver());
		lp.setEmail(p.getProperty("email"));
		lp.setPassword(p.getProperty("password"));
		lp.clickLogin();
		
		MyAccountPage macc=new MyAccountPage(getDriver());
		boolean targetPage=macc.isMyAccountPageExists();
		
		//Assert.assertEquals(targetPage, true, "Login Failed");
		
		//or
		
		Assert.assertTrue(targetPage);
		
	 }catch(Exception e) //when exception comes catch block will execute
	{
		 Assert.fail();
	}
	
	 logger.info("Finished TC_002_LoginTest");	
	}

}
