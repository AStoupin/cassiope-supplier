package ru.cetelem.supplier;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.cetelem.supplier.ui.view.CarEditorViewTest;
import ru.cetelem.supplier.ui.view.PayloadEditorViewTest;
import ru.cetelem.supplier.ui.view.PayloadListViewTest;


	
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CarTest.class)
@Import({Application.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarTest   {
    private static final int TIMEOUT_SECONDS = 40;
    @LocalServerPort
    private int port;
    private String baseUrl;
    
    private WebDriver driver;
    

	@Before
	public void setup() throws Exception {
//    	System.setProperty("webdriver.gecko.driver", "c:\\Temp\\20190730\\chromedriver.exe");
    	System.setProperty("webdriver.chrome.driver", "c:\\Temp\\20190730\\chromedriver.exe");
//    	System.setProperty("webdriver.chrome.driver", "C:\\git\\DevTools\\SeleniumTests\\utils\\chromedriver.exe");    	
// 		http://127.0.0.1:4444/wd/hub
    	
    	driver = new ChromeDriver( );
//    	driver = new FirefoxDriver();
    	
		baseUrl = String.format("http://localhost:%d", port);
	}
	

	@Test
	public void testCreateCarToSubmit() {
		CarEditorViewTest carEditorViewTest;
		
		driver.get(baseUrl + "/car/S6FAXXTTGYEY13701");
		carEditorViewTest = PageFactory.initElements(driver, CarEditorViewTest.class);
		
		carEditorViewTest.invoiceNumField.sendKeys("12345", Keys.RETURN);
		carEditorViewTest.financePlanField.sendKeys("plan1", Keys.RETURN);
		carEditorViewTest.btnOK.click();
		
		driver.get(baseUrl + "/car/S6FAXXTTGYEY13701");		
		carEditorViewTest = PageFactory.initElements(driver, CarEditorViewTest.class);

		assertEquals("S6FAXXTTGYEY13701 to be 'READY TO SUBMIT'", carEditorViewTest.stateField.getAttribute("value"), "READY TO SUBMIT");
		testSubmitCarToCFL();
	}

	//@Test	
	public void testSubmitCarToCFL() {
		//testCreateCarToSubmit();
		
		PayloadListViewTest payloadListViewTest;
		driver.get(baseUrl + "/payloads");
		
		WebDriverWait wait = new WebDriverWait(driver, TIMEOUT_SECONDS);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("crudGrid")));

		payloadListViewTest = PageFactory.initElements(driver, PayloadListViewTest.class);
		payloadListViewTest.buttonAdd.click();


		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("payloadTypeField")));
		
		PayloadEditorViewTest payloadEditorViewTest;
		payloadEditorViewTest = PageFactory.initElements(driver, PayloadEditorViewTest.class);
		payloadEditorViewTest.payloadTypeField.sendKeys("CFL", Keys.RETURN);
		payloadEditorViewTest.itemListButtonAdd.click();
		payloadEditorViewTest.btnSubmit.click();

		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("crudGrid")));
	//payloadListViewTest = PageFactory.initElements(driver, PayloadListViewTest.class);
		//payloadListViewTest.crudGrid.findElements(By.ByTagName("")))
		
		//WebDriverWait wait = driver.
	}


	@After
	public void tearDown() throws Exception {
	    // close the browser instance when all tests are done
	      if (driver != null)
	    	  driver.quit();
	}		

}
