package ru.cetelem.supplier.ui.view;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CarEditorViewTest {
	  @FindBy(id="stateField")
	  public WebElement stateField;

	  @FindBy(id="invoiceNumField")
	  public WebElement invoiceNumField;

	  @FindBy(id="financePlanField")
	  public WebElement financePlanField;
	  
	  @FindBy(id="btnOK")
	  public WebElement btnOK;
	  
}
