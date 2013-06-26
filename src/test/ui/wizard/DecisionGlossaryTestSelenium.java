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
public class DecisionGlossaryTestSelenium {
	private Logger log;
	private String baseUrl = "http://bpmnserver:8080";
	private StringBuffer verificationErrors = new StringBuffer();

	private String testName = "DecisionGlossaryTest";

	private String testNameUpdate = "testUpdateName";
	
	private static final String XPATH_ORDER_ID = "html/body/table/tbody/tr/td/span/table/tbody/tr[last()]//td[1]";
	private static final String XPATH_ORDER_NAME = "html/body/table/tbody/tr/td/span/table/tbody/tr[last()]//td[4]";

	WebDriver driver = new FirefoxDriver();

	@Test
	public void test() throws Exception {
		try{
		driver.get(baseUrl + "/planspielBPMN/crudtest/decisionglossary");
		WebElement element = 
		driver.findElement(By.name("create:decisionname"));
		element.sendKeys(testName);
		driver.findElement(By.id("create:createNewDecisionGlossary")).click();
		new WebDriverWait(driver, 4).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ORDER_NAME)));
		Thread.sleep(500);
		String ourName = driver.findElement(By.xpath(XPATH_ORDER_NAME))
				.getText();
		assertTrue(testName.equals(ourName));
		
		element = driver.findElement(By.id("update:decisionGlossaryID"));
		WebElement lastId = driver.findElement(By.xpath(XPATH_ORDER_ID));
		String lastIdText = lastId.getText();
		element.sendKeys(lastIdText);
		element = driver.findElement(By.id("update:name"));
		element.sendKeys(testNameUpdate);
		driver.findElement(By.id("update:updateDecisionGlossary")).click();
		
		new WebDriverWait(driver, 4).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ORDER_NAME)));
		String ourNameUpdate = driver.findElement(By.xpath(XPATH_ORDER_NAME))
				.getText();
		assertTrue(testNameUpdate.equals(ourNameUpdate));
		
		element = driver.findElement(By.id("delete:decisionGlossaryID"));	
		
		//WebElement testid = driver.findElement(By.xpath("html/body/table/tbody/tr/td/span/table/tbody/tr[last()]//td[1]"));
		//String ourId = testid.getText();
		element.sendKeys(lastIdText);
		Thread.sleep(1000);

		driver.findElement(By.id("delete:deleteDecisionGlossary")).click();
		Thread.sleep(1000);
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
			log.warn(e.getMessage());
		}
	}
	
	@After
	public void tearDown() {
		driver.quit();	
	}
}
