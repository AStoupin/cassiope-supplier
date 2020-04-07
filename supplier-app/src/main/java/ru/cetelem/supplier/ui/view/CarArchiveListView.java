package ru.cetelem.supplier.ui.view;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.crudui.crud.impl.GridCrud;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;

import ru.cetelem.cassiope.supplier.model.Car;
import ru.cetelem.cassiope.supplier.util.DateUtils;
import ru.cetelem.supplier.service.CarService;
import ru.cetelem.supplier.service.DictionaryService;
import ru.cetelem.supplier.ui.component.XFGridCrud;

@PageTitle("Archive")
@Route(value = "archive", layout = MainLayout.class)
public class CarArchiveListView extends BaseView implements RouterLayout {
	private static final Log log = LogFactory.getLog(CarListView.class);

	private CarService carService;
	GridCrud<Car> crudGrid;

	TextField vinFilter;
	TextField stateFilter;
	TextField dealerFilter;
	TextField carModelFilter;
	Checkbox archivedFilter;
	DatePicker issueDateFieldFrom;
	DatePicker issueDateFieldTo;	

	private Button btnArchive;

	public CarArchiveListView(@Autowired CarService carService, @Autowired DictionaryService dictionaryService) {
		log.info("CarList started");

		this.carService = carService;

		crudGrid = new XFGridCrud<>(Car.class);
		Grid<Car> grid = crudGrid.getGrid();

		crudGrid.setAddOperationVisible(false);
		crudGrid.setUpdateOperationVisible(false);
		crudGrid.setDeleteOperationVisible(false);
		crudGrid.setFindAllOperation(this::getFilterdCars);

		btnArchive = createArchiveButton();
		btnArchive.setIcon(new Icon(VaadinIcon.ARCHIVE));
		btnArchive.setId("btnArchive");
		btnArchive.getElement().setAttribute("title", "Archive checked cars");

		HorizontalLayout horizontalLayout = (HorizontalLayout) crudGrid
				.getAddButton().getParent().get();
		horizontalLayout.setSpacing(false);
		horizontalLayout.setMargin(false);
		horizontalLayout.getElement().setAttribute("style", "margin-left: 0px;");
		horizontalLayout.add(btnArchive);

		grid.setMultiSort(true);
		grid.setSelectionMode(SelectionMode.MULTI);

		crudGrid.getGrid().setColumns("vin", "state");

		grid.addColumn(car -> DateUtils.asString(car.getIssueDate()))
				.setComparator((d1, d2) -> DateUtils.compare(d1.getIssueDate(), d2.getIssueDate()))
				.setHeader("Issue Date").setKey("issueDate").setResizable(true)
				.setSortOrderProvider(d -> Arrays.asList(new QuerySortOrder("issueDate", d)).stream());

		crudGrid.getGrid().addColumns("invoiceNum");

		Locale ruLocale = new Locale("ru");
		NumberFormat numberFormat = NumberFormat.getNumberInstance(ruLocale);
		numberFormat.setMinimumFractionDigits(2);

		grid.addColumn(car -> numberFormat.format(car.getValue())).setHeader("Value")
				.setComparator((v1, v2) -> (int) ((v1.getValue() - v2.getValue()) * 100))
				.setTextAlign(ColumnTextAlign.END).setResizable(true).setKey("value")
				.setSortOrderProvider(d -> Arrays.asList(new QuerySortOrder("value", d)).stream());

		crudGrid.getGrid().addColumns("dealer", "carModel");

		grid.getColumnByKey("vin").setResizable(true);
		grid.getColumnByKey("state").setResizable(true);
		grid.getColumnByKey("invoiceNum").setResizable(true);
		grid.getColumnByKey("dealer").setResizable(true);
		grid.getColumnByKey("carModel").setResizable(true);

		grid.getColumnByKey("dealer").setSortable(true);
		grid.getColumnByKey("carModel").setSortable(true);

		initFilter(grid);

		add(crudGrid);
		crudGrid.setWidthFull();
		setHeightFull();

	}

	private Button createArchiveButton() {
		return new Button("", clickEvent -> {
			log.info("clickArchiveButton started");
			LocalDate today = LocalDate.now();
			int count = 0;
			Grid<Car> grid = crudGrid.getGrid();
			Set<Car> cars = new HashSet<>(grid.getSelectedItems());
			grid.setSelectionMode(SelectionMode.SINGLE);
			for(Car car : cars) {
				if(car.getArchivedDate() == null) {
					car.setArchivedDate(today);
					carService.saveCar(car);
					count++;
				}
			}		
			grid.setSelectionMode(SelectionMode.MULTI);	
			crudGrid.refreshGrid();
			if(count == 0) {
				Notification.show("There are no cars for archiving");
			} else {
				Notification.show("Archived " + count + " car(s).");
			}
			return;
		});
	}

	private void initFilter(Grid<Car> grid) {

		vinFilter = new TextField("Filter by VIN");
		vinFilter.setId("vinFilter");
		vinFilter.setWidth("165px");
		vinFilter.addValueChangeListener(e -> crudGrid.refreshGrid());
		crudGrid.getCrudLayout().addFilterComponent(vinFilter);

		stateFilter = new TextField("Filter by State");
		stateFilter.setWidth("165px");
		stateFilter.addValueChangeListener(e -> crudGrid.refreshGrid());
		crudGrid.getCrudLayout().addFilterComponent(stateFilter);

		dealerFilter = new TextField("Filter by Dealer");
		dealerFilter.setWidth("165px");
		dealerFilter.addValueChangeListener(e -> crudGrid.refreshGrid());
		crudGrid.getCrudLayout().addFilterComponent(dealerFilter);

		carModelFilter = new TextField("Filter by Car Model");
		carModelFilter.setWidth("165px");
		carModelFilter.addValueChangeListener(e -> crudGrid.refreshGrid());
		crudGrid.getCrudLayout().addFilterComponent(carModelFilter);

		archivedFilter = new Checkbox();
		archivedFilter.getElement().setProperty("title", "Show archived cars");
		archivedFilter.setLabel("Arch");
		archivedFilter.getElement().setAttribute("style", "margin-left: 0px; padding-top: 16px");
		archivedFilter.setValue(false);
		archivedFilter.addValueChangeListener(e -> crudGrid.refreshGrid());

		issueDateFieldFrom = new DatePicker("Date from");
		issueDateFieldFrom.setWidth("115px");
		issueDateFieldFrom.addValueChangeListener(e -> crudGrid.refreshGrid());
		crudGrid.getCrudLayout().addFilterComponent(issueDateFieldFrom);
		
		issueDateFieldTo = new DatePicker("Date to");
		issueDateFieldTo.setWidth("115px");
		issueDateFieldTo.addValueChangeListener(e -> crudGrid.refreshGrid());
		crudGrid.getCrudLayout().addFilterComponent(issueDateFieldTo);
		
		crudGrid.getCrudLayout().addFilterComponent(archivedFilter);

	}

	public List<Car> getFilterdCars() {
		return carService.getCars().stream().filter(car -> {
			
			return StringUtils.containsIgnoreCase(car.getVin(), vinFilter.getValue())
					&& (StringUtils.containsIgnoreCase(car.getState(), "NEW")
							|| StringUtils.containsIgnoreCase(car.getState(), "REPAID")
							|| StringUtils.containsIgnoreCase(car.getState(), "ARCHIVED")
							)
					&& isBetweenDates(car.getIssueDate(), 
							issueDateFieldFrom.getValue(), issueDateFieldTo.getValue())
					&& StringUtils.containsIgnoreCase(car.getState(), stateFilter.getValue())
					&& StringUtils.containsIgnoreCase(car.getDealer() == null 
						? "" : car.getDealer().toString(),dealerFilter.getValue())
					&& StringUtils.containsIgnoreCase(car.getCarModel().toString(), carModelFilter.getValue())
					&& (archivedFilter.getValue() || !"ARCHIVED".equals(car.getState()));

		}).collect(Collectors.toList());
	}

	private boolean isBetweenDates(LocalDate issueDate, LocalDate dateFrom, LocalDate dateTo) {
		int compareFrom = 0;
		int compareTo = 0;
		if(issueDate != null) {
			if(dateFrom != null) {
				compareFrom = dateFrom.compareTo(issueDate);
			}
			if(dateTo != null) {
				compareTo = dateTo.compareTo(issueDate);
			}
		}
		return compareFrom <= 0 && compareTo >= 0;
	}

}
