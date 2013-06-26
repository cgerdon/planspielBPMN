package test.ui.wizard;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumTestSelenium {
	private Logger log;
	private static final String BASE_URL = "http://bpmnserver:8080";
	private static final String ROLEPLAY_INDEX_URL = "/planspielBPMN/crudtest/roleplay/index.xhtml";
	private static final String ROLEPLAY_INDEX2_URL = "/planspielBPMN/crudtest/roleplay/index2.xhtml";
	private static final String ROLEPLAY_INDEX3_URL = "/planspielBPMN/crudtest/roleplay/index3.xhtml";

	private StringBuffer verificationErrors = new StringBuffer();

	WebDriver driver = new FirefoxDriver();

	/**
	 * Selenium test for the site crudtest/index.xhtml
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRoleplayIndex() throws Exception {
		try {
			driver.get(BASE_URL + ROLEPLAY_INDEX_URL);
			WebElement element = driver.findElement(By
					.name("form:rolePlayName"));
			element.sendKeys("testName");
			element = driver.findElement(By.name("form:rolePlayStatus"));
			element.sendKeys("running");
			element = driver
					.findElement(By.name("form:rolePlayCurrentRoundNo"));
			element.sendKeys("1");
			driver.findElement(By.id("form:createNewRolePlay")).click();
			
			element = driver.findElement(By.name("form:rolePlayName"));

			WebElement testid = driver
					.findElement(By.xpath("html/body/table/tbody/tr/td/span/table/tbody/tr[last()]//td[1]"));
			String ourId = testid.getText();
			element.sendKeys(ourId);
			// driver.quit();
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
	}

//	@Test
//	public void testRoleplayIndex2() throws Exception {
//		final String TEST_ID = "5";
//		try {
//
//			driver.get(BASE_URL + ROLEPLAY_INDEX2_URL);
//			WebElement element = driver.findElement(By.name("form:rolePlayID"));
//
//			WebElement testid = driver
//					.findElement(By
//							.xpath("html/body/table/tbody/tr/td/span/table/tbody/tr[last()]//td[1]"));
//			String ourId = testid.toString();
//			element.sendKeys(ourId);
//			element = driver.findElement(By.name("form:rolePlayName"));
//			element.sendKeys("testNameUpdate");
//			element = driver.findElement(By.name("form:rolePlayStatus"));
//			element.sendKeys("finished");
//			element = driver
//					.findElement(By.name("form:rolePlayCurrentRoundNo"));
//			element.sendKeys("2");
//			driver.findElement(By.id("form:updateRoleplay")).click();
//			driver.quit();
//		} catch (Exception e) {
//			log.warn(e.getMessage());
//		}
//	}
//
//	@Test
//	public void testRoleplayIndex3() throws Exception {
//		final String TEST_ID = "5";
//		try {
//			driver.get(BASE_URL + ROLEPLAY_INDEX3_URL);
//			WebElement element = driver.findElement(By.name("form:rolePlayID"));
//			element.sendKeys(TEST_ID);
//			driver.findElement(By.id("form:deleteRoleplay")).click();
//			driver.quit();
//		} catch (Exception e) {
//			log.warn(e.getMessage());
//		}
//	}

	@After
	public void tearDown() throws Exception {
		// driver.quit();
		String verificationErrorString = verificationErrors.toString();

	}
}