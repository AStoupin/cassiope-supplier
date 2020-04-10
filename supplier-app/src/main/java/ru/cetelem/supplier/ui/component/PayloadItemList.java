package ru.cetelem.supplier.ui.component;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ru.cetelem.cassiope.supplier.model.Payload;
import ru.cetelem.cassiope.supplier.model.PayloadItem;
import ru.cetelem.supplier.integration.AbstractCarPayloadItemUpdateProcessor;
import ru.cetelem.supplier.integration.AbstractPayloadItemProcessor;
import ru.cetelem.supplier.service.DictionaryService;
import ru.cetelem.supplier.service.PayloadService;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class PayloadItemList extends VerticalLayout  {
	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(PayloadItemList.class); 
	
	private PayloadService payloadService;
	@SuppressWarnings("unused")
	private DictionaryService dictionaryService;
	private XFGridCrud<PayloadItem> crudGrid ;
	private TextField sourceFilter;
	private AbstractPayloadItemProcessor payloadItemProcessor;
	
	private Payload payload;
	
	private boolean isReadOnly;

	public PayloadItemList(@Autowired PayloadService payloadService,
			@Autowired DictionaryService dictionaryService,
			Payload payload) {
		log.info("PayloadItemList started");

		this.payloadService = payloadService;
		this.dictionaryService = dictionaryService;
		this.payload = payload;

		if(payload.payloadType!=null)
			this.payloadItemProcessor = payloadService.getPayloadItemProcessor(payload);
		
		
		isReadOnly = ! "NEW".equals(payload.state);

		Label caption = new Label();
		caption.setText("Payload items	");
		caption.addClassName("splitter");
		add(caption);

		crudGrid = new XFGridCrud<PayloadItem>(PayloadItem.class);
		Grid<PayloadItem> grid = crudGrid.getGrid();

		crudGrid.setUpdateOperationVisible(false);
		crudGrid.setFindAllOperationVisible(false);
		
		crudGrid.setFindAllOperation(this::getPayloadItems);
		
		
		crudGrid.getAddButton().setIcon(new Icon(VaadinIcon.AUTOMATION));
		crudGrid.getAddButton().addClickListener(this::addPayloadItems);
		crudGrid.setAddOperationVisible(!isReadOnly);
		
		crudGrid.setDeleteOperation(this::removePayloadItem);
		crudGrid.setDeleteOperationVisible(!isReadOnly && payloadItemProcessor instanceof AbstractCarPayloadItemUpdateProcessor);
		
		grid.setColumns("source", "errorDescr");
		grid.getColumns().get(0).setResizable(true).setFlexGrow(5);
		grid.getColumns().get(1).setFlexGrow(1);
		
		initFilter();
		
		add(crudGrid);

		crudGrid.setSizeFull();
	}

	
	
	private void initFilter() {
		sourceFilter=new TextField("Filter by Source");
		sourceFilter.setId("vinFilter");
		sourceFilter.addValueChangeListener(e -> crudGrid.refreshGrid());
		crudGrid.getCrudLayout().addFilterComponent(sourceFilter);				
	}


	private void removePayloadItem(PayloadItem payloadItem) {
		payload.getPayloadItems().remove(payloadItem);
		
		if (payloadItemProcessor instanceof AbstractCarPayloadItemUpdateProcessor) {
			((AbstractCarPayloadItemUpdateProcessor)payloadItemProcessor).process(
					payload.getPayloadItems().stream().map(pi->pi.car).collect(Collectors.toList()));
			
		}
	}
	
	public void addPayloadItems(ClickEvent<Button> buttonEvent) {
		
		log.info("addPayloadItems started");
		
		try {
			payloadService.getPayloadItemProcessor(payload).process();
		}
		catch(Exception e){
			log.error(e);
			Notification.show(e.getMessage());
			
		}
		
		crudGrid.refreshGrid();

		log.info("addPayloadItems finished");
	}



	public void reloadList(Payload payload) {
		this.payload = payload;
	
		if(payload.payloadType!=null) 
			this.payloadItemProcessor = payloadService.getPayloadItemProcessor(payload);

		crudGrid.setDeleteOperationVisible(!isReadOnly && payloadItemProcessor instanceof AbstractCarPayloadItemUpdateProcessor);

		
		crudGrid.refreshGrid();
		
		
	}


    public List<PayloadItem> getPayloadItems(){
		return payload.getPayloadItems().stream().filter(payloadItem->{
			
			return StringUtils.containsIgnoreCase(payloadItem.source,
					sourceFilter.getValue()
					);
			
		}).collect(Collectors.toList());    	
    }
	
	
	
}
