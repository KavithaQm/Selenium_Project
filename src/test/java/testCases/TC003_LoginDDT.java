package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import baseTest.BaseClass;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import utilities.DataProviders;


public class TC003_LoginDDT extends BaseClass{

	

	@Test(dataProvider="LoginData",dataProviderClass=DataProviders.class) //getting data provider from different class
	public void verify_loginDDT(String email, String pwd, String exp)
	{
	    logger.info("Starting TC_003_LoginDDT");
	    
	    try
	    {
		   HomePage hp=new HomePage(getDriver()); //here driver is coming from base class
		   hp.clickMyAccount();
		   hp.clickLogin();
		
		   LoginPage lp=new LoginPage(getDriver());
		   lp.setEmail(email);
		   lp.setPassword(pwd);
		   lp.clickLogin();
		
		   MyAccountPage macc=new MyAccountPage(getDriver());
		   boolean targetPage=macc.isMyAccountPageExists();
		
		/* Data is valid - login success - test passed - logout
		 *                  login failed - test failed 
		 *                  
		 *   Data is invalid - login success - test fail - logout
		 *                      login failed - test pass
		 */
		
		   if(exp.equalsIgnoreCase("valid"))
		   {
			  if(targetPage==true)  //login is successful
			  {
				 macc.clickLogout(); //logout
				 Assert.assertTrue(true); //test passed
				
			  }
		      else
		      {
			    Assert.assertTrue(false);
		      }
		   }
		   if(exp.equalsIgnoreCase("invalid"))
		   {
			 if(targetPage==true)  //login is successful
			 {
				macc.clickLogout(); //logout 
				Assert.assertTrue(false); //test failed
				
			}
		   else     //login failed
		    {
			    Assert.assertTrue(true); //test pass
		     }
		}
	    }catch(Exception e)
	    {
	    	Assert.fail();
	    }
		   logger.info("Finished TC_003_LoginDDT");
	}
}
