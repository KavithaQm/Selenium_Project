package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import baseTest.BaseClass;

public class ExtentReportManager implements ITestListener {

    private ExtentSparkReporter sparkReporter;
    private ExtentReports extent;
    private ExtentTest test;
    private static String reportName;
    private static List<ITestResult> totalTestCases = new ArrayList<>();
    
    public static int passedTests = 0;
    public static int failedTests = 0;
    public static int skippedTests = 0;

    public void onStart(ITestContext testContext) {
        // Initialize report name with timestamp
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        reportName = "Test-Report-" + timeStamp + ".html";

        // Setting up ExtentSparkReporter
        sparkReporter = new ExtentSparkReporter("./reports/" + reportName);
        sparkReporter.config().setDocumentTitle("Automation Test Report");
        sparkReporter.config().setReportName("Functional Testing");
        sparkReporter.config().setTheme(Theme.DARK);

        // Initialize ExtentReports
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Application", "opencart");
        extent.setSystemInfo("Module", "Admin");
        extent.setSystemInfo("Sub Module", "Customers");
        extent.setSystemInfo("User Name", System.getProperty("user.name"));
        extent.setSystemInfo("Environment", "QA");

        // Add additional environment info
        String os = testContext.getCurrentXmlTest().getParameter("os");
        extent.setSystemInfo("Operating System", os);
        String browser = testContext.getCurrentXmlTest().getParameter("browser");
        extent.setSystemInfo("Browser", browser);
        
        System.out.println("Test Suite Started: " + testContext.getName());
    }

    public void onTestSuccess(ITestResult result) {
        test = extent.createTest(result.getTestClass().getName() + " - " + result.getName());
        test.assignCategory(result.getMethod().getGroups());
        test.log(Status.PASS, result.getName() + " executed successfully");
        passedTests++;
        totalTestCases.add(result);
    }

    public void onTestFailure(ITestResult result) {
        test = extent.createTest(result.getTestClass().getName() + " - " + result.getName());
        test.assignCategory(result.getMethod().getGroups());
        test.log(Status.FAIL, result.getName() + " failed");
        test.log(Status.INFO, result.getThrowable().getMessage());

        // Capture and attach screenshot in case of failure
        try {
            String base64Screenshot = new BaseClass().captureScreenBase64();
            test.addScreenCaptureFromBase64String(base64Screenshot, result.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        failedTests++;
        totalTestCases.add(result);
    }

    public void onTestSkipped(ITestResult result) {
        test = extent.createTest(result.getTestClass().getName() + " - " + result.getName());
        test.assignCategory(result.getMethod().getGroups());
        test.log(Status.SKIP, result.getName() + " was skipped");
        test.log(Status.INFO, result.getThrowable().getMessage());
        skippedTests++;
        totalTestCases.add(result);
    }

    public void onFinish(ITestContext testContext) {
        // Flush report
        extent.flush();

        // Get path of the report
        String reportPath = System.getProperty("user.dir") + "/reports/" + reportName;
        File extentReport = new File(reportPath);

        // Open the report in the default browser
        try {
            Desktop.getDesktop().browse(extentReport.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Send the report via email with attachment
        try {
            sendEmailWithAttachment(reportPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to send email with report attachment
    public static void sendEmailWithAttachment(String reportPath) throws MessagingException {
        String host = "smtp.office365.com";
        final String user = "parameswarareddy@quality-matrix.com";
        final String password = "qhub@2019";

        // Set email properties
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Create email session
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        // Compose email
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(user));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress("kavitha_bandiga@quality-matrix.com"));
        message.setSubject("Automation Test Report");

        // Create HTML body for email
        String htmlTemplate = createEmailBody();

        // Email body part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(htmlTemplate, "text/html");

        // Attachment part
        MimeBodyPart attachmentPart = new MimeBodyPart();
        DataSource source = new FileDataSource(reportPath);
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setFileName(reportName);

        // Combine body and attachment
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        multipart.addBodyPart(attachmentPart);

        // Set content to message
        message.setContent(multipart);

        // Send email
        Transport.send(message);
        System.out.println("Email sent successfully with attachment!");
    }

    // Generate HTML body for the email
    private static String createEmailBody() {
        String htmlTemplate = "<html><body>" +
            "<h1>Selenium Test Report</h1>" +
            "<h2>Test Cases Summary</h2>" +
            "<p>Total Tests: " + (passedTests + failedTests + skippedTests) + "</p>" +
            "<p>Passed: " + passedTests + "</p>" +
            "<p>Failed: " + failedTests + "</p>" +
            "<p>Skipped: " + skippedTests + "</p>" +
            "</body></html>";
        return htmlTemplate;
    }

    public static List<ITestResult> getTestResults() {
        return totalTestCases;
    }
}
