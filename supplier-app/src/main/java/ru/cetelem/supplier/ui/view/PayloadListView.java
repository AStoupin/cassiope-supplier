package ru.cetelem.supplier.ui.view;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(PayloadListView.class); 
	
	private PayloadService payloadService;
	private XFGridCrud<Payload> crudGrid;
	
	private TextField stateFilter;
	private TextField codeFilter;
	private TextField nameFilter;
	private DatePicker dateFilter;
	private Checkbox archivedFilter;

	private Button btnArchive;
	private Input input;
	
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
		
		grid.addItemDoubleClickListener(event -> {
			grid.select(event.getItem());
			editSelected(null);
		});

		btnArchive = createArchiveButton();
		btnArchive.setIcon(new Icon(VaadinIcon.ARCHIVE));
		btnArchive.setId("btnArchive");
		btnArchive.getElement().setAttribute("title", "Archive old payloads");

		HorizontalLayout horizontalLayout = (HorizontalLayout) crudGrid
				.getAddButton().getParent().get();
		horizontalLayout.add(btnArchive);
		
		grid.setColumns();
		grid.addColumn(payload->DateUtils.asString(payload.getDate()))
			.setComparator((d1, d2) -> DateUtils.compare(d1.getDate(), d2.getDate()))
			.setHeader("Date")
			.setKey("date")
			.setFlexGrow(1)
			.setResizable(true)
			.setSortOrderProvider(d->Arrays.asList(new QuerySortOrder("date", d)).stream());
		
		grid.addColumns("name", "payloadType", "state");

		
		grid.getColumnByKey("name").setResizable(true).setFlexGrow(3);
		grid.getColumnByKey("payloadType").setResizable(true).setFlexGrow(1);
		grid.getColumnByKey("state").setResizable(true).setFlexGrow(1);

		initFilter(grid);

		add(crudGrid);
		crudGrid.setWidthFull();
		setHeightFull();		

	}

	private Button createArchiveButton() {
		return new Button("", clickEvent -> {
			log.info("clickArchiveButton started");
			Div header = new Div();
			header.getElement().setAttribute("style", "text-align: center; font-size: xx-large; padding-bottom: 30px;");
			Span headerLabel = new Span("Archive payloads");
			header.add(headerLabel);

			Dialog dialog = new Dialog(header);
			dialog.setWidth("400px");
			dialog.addDialogCloseActionListener(e -> dialog.close());

			Label leftLabel = new Label("Archive payloads older then ");			
			input = new Input();
			input.setValue("30");
			input.setWidth("40px");
			Label rightLabel = new Label(" day(s)");
			dialog.add(leftLabel, input, rightLabel);

			Div divider = new Div();
			divider.setHeight("20px");
			dialog.add(divider);
			
			Div footer = new Div();
			footer.getElement().setAttribute("style", "float: right; padding-bottom: 20px;");			
			
			Button archiveButton = createArchiveButtonDialog(dialog);			
			archiveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
			Button cancelButton = new Button("Cancel", event -> dialog.close());
			archiveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
			
			footer.add(archiveButton, cancelButton);			
			dialog.add(footer);
						
			dialog.open();
			input.getElement().callJsFunction("focus");
			return;
		});
	}

	private Button createArchiveButtonDialog(Dialog dialog) {
		return new Button("Archive", event -> {
			log.info("clickArchiveButtonDialog started");
			int days = 0;
			try {
				days = Integer.parseInt(input.getValue());
			} catch (NumberFormatException e) {
				log.error("createArchiveButtonDialog", e);
				Notification.show("Must be number: '" + input.getValue() + "'");
			}
			int count = 0;
			if(days > 0) {
				Set<Payload> payloads = new HashSet<>(getFilterd());
				LocalDate today = LocalDate.now();
				count = payloadService.archivePayloads(payloads, today, days);
				
				crudGrid.refreshGrid();
			}
			if(count == 0) {
				Notification.show("There are no payloads for archiving");
			} else {
				Notification.show("Archived " + count + " payloads");
			}
		    dialog.close();
		});
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

		archivedFilter = new Checkbox();
		archivedFilter.getElement().setProperty("title", "Show archived payloads");
		archivedFilter.setLabel("Arch");
		archivedFilter.setClassName("archCheckFilter");
		archivedFilter.setValue(false);
		archivedFilter.addValueChangeListener(e -> crudGrid.refreshGrid());
		crudGrid.getCrudLayout().addFilterComponent(archivedFilter);

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
				&& 
				(archivedFilter.getValue() || payload.getArchivedDate() == null)
				;
			return isFilterd;
			}).collect(Collectors.toList());

		return lPayloads;
	}

}
