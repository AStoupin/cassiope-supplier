package ru.cetelem.supplier.ui.view;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PayloadListViewTest {
	  @FindBy(xpath="//*[@id=\"crudGrid\"]/vaadin-vertical-layout/vaadin-vertical-layout/vaadin-horizontal-layout/vaadin-horizontal-layout[1]/vaadin-button[2]")
	  public WebElement buttonAdd;

	  @FindBy(id="crudGrid")
	  public WebElement crudGrid;

}
