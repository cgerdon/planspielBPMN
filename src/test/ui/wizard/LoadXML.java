package test.ui.wizard;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
/**
 * 
 * @author Felix
 *
 */
public class LoadXML {
	WebDriver driver = new FirefoxDriver();
	private String baseUrl = "http://bpmnserver:8080";

	@Test
	public void setXML() throws Exception {
		driver.get(baseUrl + "/planspielBPMN/settings");
		Thread.sleep(1000);
		driver.findElement(By.id("setXML:setUserAndMailboxXML")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("setXML:setDocumentsXML")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("setXML:setActionsXML")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("setXML:setDecisionsXML")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("setXML:setReasonsXML")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("setXML:setRoleplayXML")).click();
		Thread.sleep(2000);

	}
}
