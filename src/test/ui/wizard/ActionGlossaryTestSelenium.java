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
public class ActionGlossaryTestSelenium {
	private static Logger logger = Logger.getRootLogger();
	private String baseUrl = "http://bpmnserver:8080";
	private StringBuffer verificationErrors = new StringBuffer();

	private String testName = "ActionGlossaryTest";

	private String testNameUpdate = "testUpdateName";
	
	private static final String XPATH_ORDER_ID = "html/body/table/tbody/tr/td/span/table/tbody/tr[last()]//td[1]";
	private static final String XPATH_ORDER_NAME = "html/body/table/tbody/tr/td/span/table/tbody/tr[last()]//td[4]";


	WebDriver driver = new FirefoxDriver();

	@Test
	public void test() throws Exception {
		try{
			logger.info("Methode wird gerufen");
		driver.get(baseUrl + "/planspielBPMN/crudtest/actionglossary");
		WebElement element = 
		driver.findElement(By.name("create:name"));
		element.sendKeys(testName);
		driver.findElement(By.id("create:createNewActionGlossary")).click();
		
		new WebDriverWait(driver, 4).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ORDER_NAME)));
		Thread.sleep(500);
		String ourName = driver.findElement(By.xpath(XPATH_ORDER_NAME))
				.getText();

		assertTrue(testName.equals(ourName));
		
		element = driver.findElement(By.id("update:actionGlossaryID"));
		WebElement lastId = driver.findElement(By.xpath(XPATH_ORDER_ID));
		String lastIdText = lastId.getText();
		element.sendKeys(lastIdText);
		element = driver.findElement(By.id("update:name"));
		element.sendKeys(testNameUpdate);
		driver.findElement(By.id("update:updateActionGlossary")).click();

		new WebDriverWait(driver, 4).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ORDER_NAME)));
		Thread.sleep(500);
		String ourNameUpdate = driver.findElement(By.xpath(XPATH_ORDER_NAME))
				.getText();
		assertTrue(testNameUpdate.equals(ourNameUpdate));
		element = driver.findElement(By.id("delete:actionGlossaryID"));	
		Thread.sleep(500);
		//WebElement testid = driver.findElement(By.xpath("html/body/table/tbody/tr/td/span/table/tbody/tr[last()]//td[1]"));
		//String ourId = testid.getText();
		element.sendKeys(lastIdText);

		driver.findElement(By.id("delete:deleteActionGlossary")).click();

		String newLastId = driver.findElement(By.xpath(XPATH_ORDER_ID))
				.getText();
		Thread.sleep(500);
		if(newLastId.isEmpty()){
			assertTrue(newLastId.isEmpty());
		}
		else{
			assertTrue(lastIdText != newLastId);
			driver.quit();
		}
		
		driver.quit();
		}
		catch(Exception e) {
			logger.warn(e.getMessage());
		}
		
	}
	
	@After
	public void tearDown() {
		driver.quit();	
	}
}