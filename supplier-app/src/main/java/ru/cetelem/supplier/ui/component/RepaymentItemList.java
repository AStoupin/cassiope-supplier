package ru.cetelem.supplier.ui.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import ru.cetelem.cassiope.supplier.model.Car;
import ru.cetelem.cassiope.supplier.model.RepaymentItem;


public class RepaymentItemList extends VerticalLayout  {
	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(RepaymentItemList.class); 


	private Grid<RepaymentItem> crudGrid ;

	@SuppressWarnings("unused")
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
