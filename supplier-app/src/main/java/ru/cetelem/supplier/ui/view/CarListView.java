package ru.cetelem.supplier.ui.view;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.crudui.crud.impl.GridCrud;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;

import ru.cetelem.cassiope.supplier.model.Car;
import ru.cetelem.cassiope.supplier.util.DateUtils;
import ru.cetelem.supplier.service.CarService;
import ru.cetelem.supplier.service.DictionaryService;
import ru.cetelem.supplier.ui.component.XFGridCrud;
import ru.cetelem.supplier.util.ExcelReader;
import ru.cetelem.supplier.util.ExportXls;

@PageTitle("Car list")
@Route(value = "", layout = MainLayout.class)
public class CarListView extends BaseView implements RouterLayout {
	private static final Log log = LogFactory.getLog(CarListView.class);

	private CarService carService;
	GridCrud<Car> crudGrid;
	
	TextField vinFilter;
	TextField stateFilter;
	TextField dealerFilter;
	TextField carModelFilter;
	Checkbox archivedFilter;

	private Button btnUpload;
	private Anchor download;

	public CarListView(@Autowired CarService carService,
			@Autowired DictionaryService dictionaryService) {
		log.info("CarList started");

		this.carService = carService;

		crudGrid = new XFGridCrud<>(Car.class);
		Grid<Car> grid = crudGrid.getGrid();

		crudGrid.getAddButton().addClickListener(this::addCar);
		crudGrid.getUpdateButton().addClickListener(this::editSelectedCar);
		crudGrid.setDeleteOperationVisible(false);
		crudGrid.setFindAllOperation(this::getFilterdCars);

		btnUpload = createUploadButton();
		btnUpload.setIcon(new Icon(VaadinIcon.UPLOAD_ALT));
		btnUpload.setId("btnUpload");
		btnUpload.getElement().setAttribute("title", "Show upload XLS file dialog");
		
		ExportXls exportXls = new ExportXls(this);
		download = new Anchor();
		download.setHref(exportXls.getCarResource());
		download.getElement().setAttribute("title", "Download XLS file");
		download.add(new Button(new Icon(VaadinIcon.DOWNLOAD_ALT)));
		download.getElement().setAttribute("style", "text-decoration: none");
		Button btnDownload = (Button) download.getChildren().findFirst().get();
		btnDownload.setHeight("100%");

		HorizontalLayout horizontalLayout = (HorizontalLayout) crudGrid
				.getAddButton().getParent().get();
		horizontalLayout.setSpacing(false);
		horizontalLayout.setMargin(false);
		horizontalLayout.getElement().setAttribute("style", "margin-left: 0px;");
		horizontalLayout.add(btnUpload, download);

		crudGrid.getGrid().setColumns("vin", "state");

		grid.addColumn(car->DateUtils.asString(car.getIssueDate()))
			.setComparator((d1, d2) -> DateUtils.compare(d1.getIssueDate(), d2.getIssueDate()))
			.setHeader("Issue Date")
			.setKey("issueDate")
			.setResizable(true)
			.setSortOrderProvider(d->Arrays.asList(new QuerySortOrder("issueDate", d)).stream());

		crudGrid.getGrid().addColumns("invoiceNum");

		Locale ruLocale = new Locale("ru");
		NumberFormat numberFormat = NumberFormat.getNumberInstance(ruLocale);
		numberFormat.setMinimumFractionDigits(2);

		grid.addColumn(car -> numberFormat.format(car.getValue())).setHeader("Value")
				.setComparator((v1, v2) -> (int) ((v1.getValue() - v2.getValue()) * 100))
				.setTextAlign(ColumnTextAlign.END)
				.setResizable(true)
				.setKey("value")
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

	private Button createUploadButton() {
		return new Button("", clickEvent -> {
			log.info("clickUploadButton started");

			Div output = new Div();

			MemoryBuffer buffer = new MemoryBuffer();
			Upload upload = new Upload(buffer);

			Span dropLabel = new Span(
					"Drag and drop Excel file (*.xls) with car list here to upload cars into database");
			upload.setDropLabel(dropLabel);
			upload.setId("upload");

			upload.addSucceededListener(event -> {
				Component component = createComponent(event.getMIMEType(),
						event.getFileName(), buffer.getInputStream());
				output.add(component);
			});

			output.add(upload);

			Dialog dialog = new Dialog(output);

			dialog.addDialogCloseActionListener(e -> dialog.close());
			dialog.open();
			return;
		});
	}

	private Component createComponent(String mimeType, String fileName,
			InputStream stream) {
		log.info("createComponent started for mimeType = " + mimeType);
		String text = null;
		if ("application/vnd.ms-excel".equals(mimeType)) {
			text = "XLS file loaded. ";
			try {
				text = text + ExcelReader.readFromExcel(stream, carService);
				crudGrid.refreshGrid();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
				.equals(mimeType)) {
			text = "XLSX file uploads not supported yet. ";
		} else {
			text = "You can load xls file only. ";
		}
		return new Text(text);
	}

	private void initFilter(Grid<Car> grid) {

		vinFilter = new TextField("Filter by VIN");
		vinFilter.setId("vinFilter");
		vinFilter.addValueChangeListener(e -> crudGrid.refreshGrid());
		crudGrid.getCrudLayout().addFilterComponent(vinFilter);

		stateFilter = new TextField("Filter by State");
		stateFilter.addValueChangeListener(e -> crudGrid.refreshGrid());
		crudGrid.getCrudLayout().addFilterComponent(stateFilter);

		dealerFilter = new TextField("Filter by Dealer");
		dealerFilter.addValueChangeListener(e -> crudGrid.refreshGrid());
		crudGrid.getCrudLayout().addFilterComponent(dealerFilter);

		carModelFilter = new TextField("Filter by Car Model");
		carModelFilter.addValueChangeListener(e -> crudGrid.refreshGrid());
		crudGrid.getCrudLayout().addFilterComponent(carModelFilter);
		
		archivedFilter = new Checkbox();
		archivedFilter.getElement().setProperty("title", "Show archived cars");
		archivedFilter.setLabel("Arch");
		archivedFilter.getElement().setAttribute("style", "margin-left: 0px; padding-top: 16px");
		archivedFilter.setValue(false);
		archivedFilter.addValueChangeListener(e -> crudGrid.refreshGrid());
		crudGrid.getCrudLayout().addFilterComponent(archivedFilter);

	}

	public void editSelectedCar(ClickEvent<Button> buttonEvent) {
		Optional<Car> selectedCar = crudGrid.getGrid().getSelectedItems()
				.stream().findFirst();
		if (selectedCar.isPresent())
			getUI().ifPresent(
					ui -> ui.navigate("car/" + selectedCar.get().getVin()));
	}

	public void addCar(ClickEvent<Button> buttonEvent) {
		getUI().ifPresent(ui -> ui.navigate("car/"));
	}

	public List<Car> getFilterdCars() {
		return carService
				.getCars()
				.stream()
				.filter(car -> {

					return StringUtils.containsIgnoreCase(car.getVin(),
							vinFilter.getValue())
							&& StringUtils.containsIgnoreCase(car.getState(),
									stateFilter.getValue())
							&& StringUtils.containsIgnoreCase(
									car.getDealer() == null ? "" : car
											.getDealer().toString(),
									dealerFilter.getValue())
							&& StringUtils.containsIgnoreCase(car.getCarModel()
									.toString(), carModelFilter.getValue())
							&& (archivedFilter.getValue() || !"ARCHIVED".equals(car.getState()));

				}).collect(Collectors.toList());
	}

}
