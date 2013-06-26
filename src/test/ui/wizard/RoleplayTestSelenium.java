package test.ui.wizard;

import static org.junit.Assert.assertTrue;

import java.util.List;

import main.domain.bpmn.Roleplay;
import main.service.db.RoleplayService;
import main.ui.wizard.RoleplayController;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RoleplayTestSelenium {
	private Logger log;
	private static final String BASE_URL = "http://bpmnserver:8080";
	private static final String ROLEPLAY_INDEX_URL = "/planspielBPMN/crudtest/roleplay/index.xhtml";
	private static final String ROLEPLAY_INDEX2_URL = "/planspielBPMN/crudtest/roleplay/index2.xhtml";
	private static final String ROLEPLAY_INDEX3_URL = "/planspielBPMN/crudtest/roleplay/index3.xhtml";
	private static final String XPATH_ORDER_ID = "html/body/table/tbody/tr/td/span/table/tbody/tr[last()]//td[1]";
	private static final String XPATH_ORDER_NAME = "html/body/table/tbody/tr/td/span/table/tbody/tr[last()]//td[2]";
	private static final String XPATH_ORDER_ROUND_NO = "html/body/table/tbody/tr/td/span/table/tbody/tr[last()]//td[4]";
	private static final String XPATH_ORDER_STATUS = "html/body/table/tbody/tr/td/span/table/tbody/tr[last()]//td[3]";
	private static final String XPATH_ORDER_ALL_FROM_DB = "html/body/table/tbody/tr/td/span/table/tbody//tr";

	private String testName = "test";
	private Integer testRoundNo = 1;
	private String testStatus = "Aktiv";

	private String testNameUpdate = "testUpdate";
	private String testStatusUpdate = "finished";
	private Integer testRoundUpdate = 2;

	private StringBuffer verificationErrors = new StringBuffer();

	WebDriver driver = new FirefoxDriver();

	/**
	 * Selenium test for the site crudtest/index.xhtml
	 * 
	 * @throws Exception
	 */
	@Ignore
	public void testRoleplayIndex() throws Exception {
		try {
			driver.get(BASE_URL + ROLEPLAY_INDEX_URL);
			WebElement element = driver.findElement(By
					.name("form:rolePlayName"));
			element.sendKeys(testName);
			element = driver.findElement(By.name("form:rolePlayStatus"));
			element.sendKeys(testStatus);
			element = driver
					.findElement(By.name("form:rolePlayCurrentRoundNo"));
			element.sendKeys(testRoundNo.toString());
			driver.findElement(By.id("form:createNewRolePlay")).click();
			new WebDriverWait(driver, 4).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ORDER_NAME)));

			element = driver.findElement(By.name("form:rolePlayName"));

			String ourName = driver.findElement(By.xpath(XPATH_ORDER_NAME))
					.getText();
			String ourStatus = driver.findElement(By.xpath(XPATH_ORDER_STATUS))
					.getText();
			String ourRoundString = driver.findElement(
					By.xpath(XPATH_ORDER_ROUND_NO)).getText();

			assertTrue(testName.equals(ourName));
			assertTrue(testStatus.equals(ourStatus));
			assertTrue(String.valueOf(testRoundNo).equals(ourRoundString));
			driver.quit();
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}

	@Ignore
	public void testRoleplayIndex2() throws Exception {
		driver.get(BASE_URL + ROLEPLAY_INDEX_URL);

		String ourId = driver.findElement(By.xpath(XPATH_ORDER_ID)).getText();

		try {

			driver.get(BASE_URL + ROLEPLAY_INDEX2_URL);
			WebElement element = driver.findElement(By.name("form:rolePlayID"));
			element.sendKeys(ourId);
			element = driver.findElement(By.name("form:rolePlayName"));
			element.sendKeys(testNameUpdate);
			element = driver.findElement(By.name("form:rolePlayStatus"));
			element.sendKeys(testStatusUpdate);
			element = driver
					.findElement(By.name("form:rolePlayCurrentRoundNo"));
			element.sendKeys(testRoundUpdate.toString());
			driver.findElement(By.id("form:updateRoleplay")).click();
			new WebDriverWait(driver, 4).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_ORDER_NAME)));

			WebDriver indexDriver = new FirefoxDriver();

			indexDriver.get(BASE_URL + ROLEPLAY_INDEX_URL);
			String ourName = indexDriver
					.findElement(By.xpath(XPATH_ORDER_NAME)).getText();
			String ourStatus = indexDriver.findElement(
					By.xpath(XPATH_ORDER_STATUS)).getText();
			String ourRoundString = indexDriver.findElement(
					By.xpath(XPATH_ORDER_ROUND_NO)).getText();

			indexDriver.quit();

			assertTrue(testNameUpdate.equals(ourName));
			assertTrue(testStatusUpdate.equals(ourStatus));
			assertTrue(testRoundUpdate.toString().equals(ourRoundString));

			driver.quit();

		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}

	@Ignore
	public void testRoleplayIndex3() throws Exception {
		WebDriver indexDriver = new FirefoxDriver();
		indexDriver.get(BASE_URL + ROLEPLAY_INDEX_URL);
		String oldLastId = indexDriver.findElement(By.xpath(XPATH_ORDER_ID))
				.getText();
		//Count all Elements in the table. Result is a String. Convert to Integer
//		Integer allRpsBefore = indexDriver.findElements(
//				By.xpath("//*[@id=\"roleplays:tb]")).size();
		try { 
			RoleplayController rpc = new RoleplayController();
			// TODO Mit Christian klaeren. "No such Method Exception" in EMF
			// Zeile 32
			// List<Roleplay> rpList= rpc.getAllRoleplays();
			// Integer allRpsBefore = rpList.size();

			driver.get(BASE_URL + ROLEPLAY_INDEX3_URL);
			WebElement element = driver.findElement(By.name("form:rolePlayID"));
			element.sendKeys(oldLastId);
			driver.findElement(By.id("form:deleteRoleplay")).click();

			//Count all Elements in the table. Result is a String. Convert to Integer
//			Integer allRpsAfter = Integer.valueOf(indexDriver.findElement(
//					By.xpath(XPATH_ORDER_ALL_FROM_DB)).getText());
//			
//			Integer difference = allRpsBefore - allRpsAfter;
//			assertTrue(difference == 1);
			String newLastId = indexDriver.findElement(By.xpath(XPATH_ORDER_ID))
					.getText();
			if(newLastId.equals("")){
				assertTrue(newLastId.equals(""));
			}
			else{
				assertTrue(oldLastId != newLastId);
				driver.quit();
			}
			
			driver.quit();
					
			indexDriver.quit();
			driver.quit();
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();

	}
}