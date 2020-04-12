package ru.cetelem.supplier.ui.view;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.crudui.crud.impl.GridCrud;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
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
	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(CarListView.class);

	private CarService carService;
	private GridCrud<Car> crudGrid;

	private TextField vinFilter;
	private TextField dealerFilter;
	
	private DatePicker dateFrom;
	private DatePicker dateTo;	

	public CarArchiveListView(@Autowired CarService carService, @Autowired DictionaryService dictionaryService) {
		log.info("CarList started");

		this.carService = carService;

		crudGrid = new XFGridCrud<>(Car.class);
		Grid<Car> grid = crudGrid.getGrid();

		crudGrid.setAddOperationVisible(false);
		crudGrid.getUpdateButton().addClickListener(this::editSelectedCar);
		crudGrid.setDeleteOperationVisible(false);
		crudGrid.setFindAllOperation(this::getFilterdCars);
				
		
		grid.addItemDoubleClickListener(event -> {
				grid.select(event.getItem());
				editSelectedCar(null);
		});


		HorizontalLayout horizontalLayout = (HorizontalLayout) crudGrid
				.getAddButton().getParent().get();
		horizontalLayout.setSpacing(false);
		horizontalLayout.setMargin(false);
		horizontalLayout.getElement().setAttribute("style", "margin-left: 0px;");

		grid.setMultiSort(true);

		crudGrid.getGrid().setColumns("vin", "state");

		grid.addColumn(car -> DateUtils.asString(car.getIssueDate()))
				.setComparator((d1, d2) -> DateUtils.compare(d1.getIssueDate(), d2.getIssueDate()))
				.setHeader("Issue Date").setKey("issueDate").setResizable(true).setFlexGrow(1)
				.setSortOrderProvider(d -> Arrays.asList(new QuerySortOrder("issueDate", d)).stream());
		
		grid.addColumn(car -> DateUtils.asString(car.getFullRepaymentDate()))
			.setComparator((d1, d2) -> DateUtils.compare(d1.getFullRepaymentDate(), d2.getFullRepaymentDate()))
			.setHeader("Full Repayment Date").setKey("fullRepaymentDate").setResizable(true).setFlexGrow(1)
			.setSortOrderProvider(d -> Arrays.asList(new QuerySortOrder("fullRepaymentDate", d)).stream());

		crudGrid.getGrid().addColumns("invoiceNum");

		Locale ruLocale = new Locale("ru");
		NumberFormat numberFormat = NumberFormat.getNumberInstance(ruLocale);
		numberFormat.setMinimumFractionDigits(2);

		grid.addColumn(car -> numberFormat.format(car.getValue())).setHeader("Value")
				.setComparator((v1, v2) -> (int) ((v1.getValue() - v2.getValue()) * 100))
				.setTextAlign(ColumnTextAlign.END).setResizable(true).setKey("value").setFlexGrow(1)
				.setSortOrderProvider(d -> Arrays.asList(new QuerySortOrder("value", d)).stream());

		crudGrid.getGrid().addColumns("dealer", "carModel");

		grid.getColumnByKey("vin").setResizable(true).setFlexGrow(3);
		grid.getColumnByKey("state").setResizable(true).setFlexGrow(1);
		grid.getColumnByKey("invoiceNum").setResizable(true).setFlexGrow(1);
		grid.getColumnByKey("dealer").setResizable(true).setFlexGrow(5);
		grid.getColumnByKey("carModel").setResizable(true).setFlexGrow(1);

		grid.getColumnByKey("dealer").setSortable(true);
		grid.getColumnByKey("carModel").setSortable(true);

		initFilter(grid);

		add(crudGrid);
		crudGrid.setWidthFull();
		setHeightFull();

	}


	private void initFilter(Grid<Car> grid) {

		vinFilter = new TextField("Filter by VIN");
		vinFilter.setId("vinFilter");
		vinFilter.setWidth("165px");
		vinFilter.addValueChangeListener(e -> crudGrid.refreshGrid());
		crudGrid.getCrudLayout().addFilterComponent(vinFilter);


		dealerFilter = new TextField("Filter by Dealer");
		dealerFilter.setWidth("165px");
		dealerFilter.addValueChangeListener(e -> crudGrid.refreshGrid());
		crudGrid.getCrudLayout().addFilterComponent(dealerFilter);
		
		dateFrom = new DatePicker("From repayment");
		dateFrom.setWidth("155px");
		dateFrom.addValueChangeListener(e -> crudGrid.refreshGrid());
		crudGrid.getCrudLayout().addFilterComponent(dateFrom);
		
		dateTo = new DatePicker("To repayment");
		dateTo.setWidth("155px");
		dateTo.addValueChangeListener(e -> crudGrid.refreshGrid());
		crudGrid.getCrudLayout().addFilterComponent(dateTo);

		Label labelInfo  = new Label("List of top 100 found cars in the archive");
		labelInfo.setClassName("labelInfo");
		crudGrid.getCrudLayout().addFilterComponent(labelInfo);

		

	}

	public void editSelectedCar(ClickEvent<Button> buttonEvent) {
		Optional<Car> selectedCar = crudGrid.getGrid().getSelectedItems()
				.stream().findFirst();
		if (selectedCar.isPresent())
			getUI().ifPresent(
					ui -> ui.navigate("car/" + selectedCar.get().getVin()));
	}

	public List<Car> getFilterdCars() {
		
		return carService.findInArchive(vinFilter.getValue(), dateFrom.getValue(), dateTo.getValue(), dealerFilter.getValue());

	}

}
