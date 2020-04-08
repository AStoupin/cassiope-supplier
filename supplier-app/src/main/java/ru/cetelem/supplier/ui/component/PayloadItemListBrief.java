package ru.cetelem.supplier.ui.component;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.SortDirection;

import ru.cetelem.cassiope.supplier.model.PayloadItem;

public class PayloadItemListBrief extends VerticalLayout  {
	private static final Log log = LogFactory.getLog(PayloadItemListBrief.class); 
	
	private Grid<PayloadItem> crudGrid ;
	
	public PayloadItemListBrief(List<PayloadItem> payloadItems) {
		log.info("CarPayloadItemList started");		

		Label caption = new Label();
		caption.setText("Payload items	");
		caption.addClassName("splitter");
		add(caption);

		crudGrid = new Grid<PayloadItem>(PayloadItem.class);

		
		crudGrid.setItems(payloadItems);
		
		
		crudGrid.setColumns("payload.payloadType", "payload.state", "payload.processedDate", "eventCode");

		// setting default sort column
		GridSortOrder<PayloadItem> order = new GridSortOrder<>(crudGrid.getColumnByKey("payload.processedDate"), 
				SortDirection.DESCENDING);
		 
		crudGrid.sort(Arrays.asList(order));
		
	
		add(crudGrid);

		crudGrid.setSizeFull();
		setHeight("300px");
		
	}

	
}
