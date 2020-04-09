package ru.cetelem.supplier.ui.component;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.RouterLink;

import ru.cetelem.cassiope.supplier.model.PayloadItem;
import ru.cetelem.supplier.ui.view.DictionaryEditorView;
import ru.cetelem.supplier.ui.view.PayloadEditorView;

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

		Grid.Column<PayloadItem> payloadColumn = crudGrid.addComponentColumn(payloadItem -> {
			RouterLink router = new RouterLink("...", PayloadEditorView.class,  payloadItem.getPayload().getName());
			return router;
			
		});
		payloadColumn.setHeader("Payload");

		// setting default sort column
		GridSortOrder<PayloadItem> order = new GridSortOrder<>(crudGrid.getColumnByKey("payload.processedDate"), 
				SortDirection.DESCENDING);
		 
		crudGrid.sort(Arrays.asList(order));
		
	
		add(crudGrid);

		crudGrid.setSizeFull();
		setHeight("300px");
		
	}

	
}
