package ru.cetelem.supplier.ui.component;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import ru.cetelem.cassiope.supplier.model.Car;
import ru.cetelem.cassiope.supplier.model.Payload;
import ru.cetelem.cassiope.supplier.model.RepaymentItem;
import ru.cetelem.supplier.service.DictionaryService;
import ru.cetelem.supplier.service.PayloadService;


public class RepaymentItemList extends VerticalLayout  {
	private static final Log log = LogFactory.getLog(RepaymentItemList.class); 
	

	private Grid<RepaymentItem> crudGrid ;
	
	private Car car;
	
	public RepaymentItemList(Car car) {
		log.info("RepaymentItemList started");
		

		this.car = car;
		
		Label caption = new Label();
		caption.setText("Repaymants");
		caption.addClassName("splitter");
		add(caption);
		crudGrid = new Grid<RepaymentItem>(RepaymentItem.class);
	
		
		crudGrid.setItems(car.getRepaymentItems());
		

		
		crudGrid.setColumns("date", "value");
		
		add(crudGrid);
		crudGrid.setSizeFull();

	}

	

	public void reloadList(Car car) {
		this.car = car;
		
		crudGrid.setItems(car.getRepaymentItems());
		

		
	}




	
}
