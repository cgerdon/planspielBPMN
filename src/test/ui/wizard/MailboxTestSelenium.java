package test.ui.wizard;

import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
/**
 * 
 * @author Felix
 *
 */
public class MailboxTestSelenium {
	private Logger log;
	private String baseUrl = "http://bpmnserver:8080";
	private StringBuffer verificationErrors = new StringBuffer();
	
	private static final String XPATH_ORDER_ID = "html/body/table/tbody/tr/td/span/table/tbody/tr[last()]//td[1]";
	private static final String XPATH_ORDER_NAME = "html/body/table/tbody/tr/td/span/table/tbody/tr[last()]//td[4]";

	WebDriver driver = new FirefoxDriver();

	@Test
	public void test() throws Exception {
		try{
		driver.get(baseUrl + "/planspielBPMN/crudtest/mailbox");
		String oldLastId = driver.findElement(By.xpath(XPATH_ORDER_ID))
				.getText();
		driver.findElement(By.id("create:createNewMailbox")).click();
		new WebDriverWait(driver, 4).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ORDER_NAME)));
		WebElement lastId = driver.findElement(By.xpath(XPATH_ORDER_ID));
		String lastIdText = lastId.getText();
		assertTrue(lastIdText != oldLastId);
		
		WebElement element = driver.findElement(By.id("delete:mailboxId"));	
		
		//WebElement testid = driver.findElement(By.xpath("html/body/table/tbody/tr/td/span/table/tbody/tr[last()]//td[1]"));
		//String ourId = testid.getText();
		element.sendKeys(lastIdText);

		driver.findElement(By.id("delete:deleteMailbox")).click();
		Thread.sleep(500);

		String newLastId = driver.findElement(By.xpath(XPATH_ORDER_ID))
				.getText();
		Thread.sleep(500);

		if(newLastId.equals("")){
			assertTrue(newLastId.equals(""));
		}
		else{
			assertTrue(lastIdText != newLastId);
			driver.quit();
		}
		
		driver.quit();
		}
		catch(Exception e) {
			log.warn(e.getMessage());
			System.out.println(e.getMessage());
		}
	}
	
	@After
	public void tearDown() {
		driver.quit();
	}

}
