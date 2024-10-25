package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RegistrationPage extends BasePage {
	
	//Constructor
	
	public RegistrationPage(WebDriver driver)
	{
		super(driver);
	}

	 //Locators
	
	@FindBy(xpath="//input[@id='input-firstname']")
	WebElement txtFirstName;
	@FindBy(xpath="//input[@id='input-lastname']")
	WebElement txtLastName;
	@FindBy(xpath="//input[@id='input-email']")
	WebElement txtEmail;
	@FindBy(xpath="//input[@id='input-telephone']")
	WebElement txtTelephone;
	@FindBy(xpath="//input[@id='input-password']")
	WebElement txtPassword;
	@FindBy(xpath="//input[@id='input-confirm']")
	WebElement txtConfirmPwd;
	@FindBy(xpath="//input[@name='agree']")
	WebElement chkdPolicy;
	@FindBy(xpath="//input[@value='Continue']")
	WebElement continueBtn;
	@FindBy(xpath="//h1[normalize-space()='Your Account Has Been Created!']")
	WebElement confrmMsg;
	
	//Action Methods
	
	public void setFirstName(String fname)
	{
		 txtFirstName.sendKeys(fname);
	}
	
	public void setLastName(String lname)
	{
		 txtLastName.sendKeys(lname);
	}
	public void setEmail(String email)
	{
		txtEmail.sendKeys(email);
	}
	
	public void setTelephone(String tel)
	{
		txtTelephone.sendKeys(tel);
	}
    public void setPassword(String pwd)
    {
	    txtPassword.sendKeys(pwd);
     }
	public void setConfirmPassword(String pwd)
	{
		txtConfirmPwd.sendKeys(pwd);
	}
	public void setPrivacyPolicy()
	{
		 chkdPolicy.click();
	}
	public void clickContinue()
	{
		continueBtn.click();
	}
	public String getConfirmationMsg()
	{
		try {
			return(confrmMsg.getText());
		}catch(Exception e) {
			return (e.getMessage());
		}
	}
}
