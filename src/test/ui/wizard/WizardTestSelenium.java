package test.ui.wizard;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
/**
 * 
 * @author Felix
 *
 */
public class WizardTestSelenium{
	private String baseUrl = "http://bpmnserver:8080";
	private StringBuffer verificationErrors = new StringBuffer();
	WebDriver driver = new FirefoxDriver();
	private Logger log;
	  
	
	public void sendPost(String pushKey, String rfidid) {
		try {
			/** spaeter: bpmnserver:8080/planspielBPMN... */
			URL url = new URL("http://bpmnserver:8080/planspielBPMN/rest/create");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
				 
			String input = String.format("{\"id\":\"%s\",\"rfidid\":\"%s\"}", pushKey, rfidid);
			 
			OutputStream output = connection.getOutputStream();
			output.write(input.getBytes());
			output.flush();
			 
			if (connection.getResponseCode() != 204) {
				throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
			}
			else {
				System.out.println("POST sucessfully sent");
			}
			 
			connection.disconnect();
 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testwizard() throws Exception {
		try{
		
		driver.get(baseUrl + "/planspielBPMN/wizard/user-select.jsf");
		Thread.sleep(1000);
		driver.findElement(By.id("userForm:userSelect"));
		Thread.sleep(1000);
		new Select(driver.findElement(By.id("userForm:userSelect"))).selectByVisibleText("Mitarbeiter Schmidt");
		Thread.sleep(1000);
		driver.findElement(By.id("userForm:selectUser")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("/html/body/div/div[2]/div/div[3]/div/div")).click();
		Thread.sleep(1000);
		sendPost("pushkey1", "234");
		Thread.sleep(1000);
		driver.findElement(By.id("documentAddedForm:rfidNameNew")).sendKeys("seleniumDok");
		Thread.sleep(1000);
		driver.findElement(By.id("documentAddedForm:createDocumentSubmit")).click();
		Thread.sleep(1000);
		sendPost("pushkey1", "123");
		Thread.sleep(1000);
		driver.findElement(By.id("documentAddedForm:addDocumentSubmit")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/form/span[5]/ul/li/div/div")).click();
		Thread.sleep(500);
		driver.findElement(By.id("wizardForm:createAction")).click();
		driver.findElement(By.id("actionCreateForm:createActionInput")).sendKeys("pruefen");
		Thread.sleep(500);
		driver.findElement(By.id("actionCreateForm:submitCreateAction")).click();
		Thread.sleep(500);
		new Select(driver.findElement(By.id("wizardForm:actionGlossarySelect"))).selectByVisibleText("pruefen");
		Thread.sleep(500);
		driver.findElement(By.id("wizardForm:createDecision")).click();
		driver.findElement(By.id("decisionCreateForm:createDecisionInput")).sendKeys("annehmen");
		Thread.sleep(500);
		driver.findElement(By.id("decisionCreateForm:submitCreateDecision")).click();
		Thread.sleep(500);
		new Select(driver.findElement(By.id("wizardForm:decisionGlossary"))).selectByVisibleText("annehmen");
		Thread.sleep(500);
		driver.findElement(By.id("wizardForm:createReason")).click();
		Thread.sleep(500);
		driver.findElement(By.id("reasonCreateForm:createReasonInput")).sendKeys("Leistung passt");
		Thread.sleep(500);
		driver.findElement(By.id("reasonCreateForm:submitCreateReason")).click();
		Thread.sleep(500);
		new Select(driver.findElement(By.id("wizardForm:reasonGlossary"))).selectByVisibleText("Leistung passt");
		driver.findElement(By.xpath("/html/body/div/div[3]/div/div/div/form/div[6]/div")).click();
		Thread.sleep(2000);
		
		driver.quit();
		}
		catch(Exception e) {
			log.warn(e.getMessage());
		}
	}
	
	@After
	public void tearDown() {
		driver.quit();	
	}

	
}
