package ru.cetelem.supplier.ui.view;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PayloadEditorViewTest {
	
	
	  @FindBy(id="payloadTypeField")
	  public WebElement payloadTypeField;
	  
	  @FindBy(xpath="//*[@id=\"itemList\"]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-horizontal-layout[1]/vaadin-button[2]")
	  public WebElement itemListButtonAdd;
	  
	  @FindBy(id="btnSubmit")
	  public WebElement btnSubmit;	  
	  
}
