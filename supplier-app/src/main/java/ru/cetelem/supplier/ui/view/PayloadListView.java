package ru.cetelem.supplier.ui.view;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;

import ru.cetelem.cassiope.supplier.model.Payload;
import ru.cetelem.cassiope.supplier.util.DateUtils;
import ru.cetelem.supplier.service.PayloadService;
import ru.cetelem.supplier.ui.component.XFGridCrud;

@PageTitle("Payload list")
@Route(value = "payloads", layout = MainLayout.class)

public class PayloadListView extends BaseView implements RouterLayout {
	private static final Log log = LogFactory.getLog(PayloadListView.class); 
	
	private PayloadService payloadService;
	private XFGridCrud<Payload> crudGrid;
	
	private TextField stateFilter;
	private TextField codeFilter;
	private TextField nameFilter;
	private DatePicker dateFilter;

	@Value("${build.version}")
	private String buildVersion;
	
	public PayloadListView(@Autowired PayloadService payloadService) {
		log.info("PayloadList started");
		
		this.payloadService = payloadService;
		
		crudGrid = new XFGridCrud<>(Payload.class);
		Grid<Payload> grid = crudGrid.getGrid();
		crudGrid.setId("crudGrid");
		
		crudGrid.getAddButton().addClickListener(this::add);
		crudGrid.getUpdateButton().addClickListener(this::editSelected);
		crudGrid.setFindAllOperation(this::getFilterd);
		crudGrid.setDeleteOperationVisible(false);

		grid.setColumns();
		grid.addColumn(payload->DateUtils.asString(payload.getDate()))
			.setComparator((d1, d2) -> DateUtils.compare(d1.getDate(), d2.getDate()))
			.setHeader("Date")
			.setKey("date")
			.setResizable(true)
			.setSortOrderProvider(d->Arrays.asList(new QuerySortOrder("date", d)).stream());

		grid.addColumns("name", "payloadType", "state");
		
		grid.getColumnByKey("name").setResizable(true);
		grid.getColumnByKey("payloadType").setResizable(true);
		grid.getColumnByKey("state").setResizable(true);

		initFilter(grid);

		add(crudGrid);
		crudGrid.setWidthFull();
		setHeightFull();		

		showCompanyName(payloadService.getEnvironment());

	}

	private void initFilter(Grid<Payload> grid) {
		log.info("initFilter started");

		nameFilter=new TextField("Filter by Name");
		nameFilter.addValueChangeListener(e -> crudGrid.refreshGrid());
		crudGrid.getCrudLayout().addFilterComponent(nameFilter);		
		
		
		codeFilter=new TextField("Filter by Type");
		codeFilter.addValueChangeListener(e -> crudGrid.refreshGrid());
		crudGrid.getCrudLayout().addFilterComponent(codeFilter);		
		
		stateFilter=new TextField("Filter by State");
		stateFilter.setId("stateFilter");
		stateFilter.addValueChangeListener(e -> crudGrid.refreshGrid());
		crudGrid.getCrudLayout().addFilterComponent(stateFilter);		

		dateFilter = new DatePicker("Filter by Date");
		dateFilter.setId("dateFilter");
		dateFilter.addValueChangeListener(e -> crudGrid.refreshGrid());
		crudGrid.getCrudLayout().addFilterComponent(dateFilter);		

	}
	
	public void editSelected(ClickEvent<Button> buttonEvent) {
		Optional<Payload> selectedPayload = crudGrid.getGrid().getSelectedItems().stream().findFirst();
		if(selectedPayload.isPresent())
			getUI().ifPresent(ui -> ui.navigate("payload/" + selectedPayload.get().name));		
	}
	
	
	public void add(ClickEvent<Button> buttonEvent) {
			getUI().ifPresent(ui -> ui.navigate("payload/"));		
	}
	
	public List<Payload> getFilterd(){
		log.info("getFilterd started");
		List<Payload> lPayloads = payloadService.getPayloads().stream().filter(payload->{			
			boolean isFilterd = 
				StringUtils.containsIgnoreCase(payload.payloadType.toString(),
						codeFilter.getValue())
				&& 
				StringUtils.containsIgnoreCase(payload.state,
						stateFilter.getValue())					
				&& 
				StringUtils.containsIgnoreCase(payload.name,
						nameFilter.getValue())					
				&& ( dateFilter.getValue() == null ||
				dateFilter.getValue().equals(DateUtils.asLocalDate(payload.date)) )				
				;
			return isFilterd;
			}).collect(Collectors.toList());

		return lPayloads;
	}
	
}
