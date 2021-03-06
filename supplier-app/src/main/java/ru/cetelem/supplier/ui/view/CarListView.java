package ru.cetelem.supplier.ui.view;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
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
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
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
	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(CarListView.class);

	private CarService carService;
	GridCrud<Car> crudGrid;
	
	TextField vinFilter;
	TextField stateFilter;
	TextField dealerFilter;
	TextField carModelFilter;

	private Button btnUpload;
	private Anchor download;
	private Button btnArchive;
	private Input input;

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
		
		grid.addItemDoubleClickListener(event -> {
			grid.select(event.getItem());
			editSelectedCar(null);
		});

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

		btnArchive = createArchiveButton();
		btnArchive.setIcon(new Icon(VaadinIcon.ARCHIVE));
		btnArchive.setId("btnArchive");
		btnArchive.getElement().setAttribute("title", "Archive old fully repaid cars");

		HorizontalLayout horizontalLayout = (HorizontalLayout) crudGrid
				.getAddButton().getParent().get();
		horizontalLayout.setSpacing(false);
		horizontalLayout.setMargin(false);
		horizontalLayout.getElement().setAttribute("style", "margin-left: 0px;");
		horizontalLayout.add(btnUpload, download, btnArchive);

		crudGrid.getGrid().setColumns("vin", "state");

		grid.addColumn(car->DateUtils.asString(car.getIssueDate()))
			.setComparator((d1, d2) -> DateUtils.compare(d1.getIssueDate(), d2.getIssueDate()))
			.setHeader("Issue Date")
			.setKey("issueDate")
			.setFlexGrow(1)
			.setResizable(true)
			.setSortOrderProvider(d->Arrays.asList(new QuerySortOrder("issueDate", d)).stream());

		crudGrid.getGrid().addColumns("invoiceNum");

		Locale ruLocale = new Locale("ru");
		NumberFormat numberFormat = NumberFormat.getNumberInstance(ruLocale);
		numberFormat.setMinimumFractionDigits(2);

		grid.addColumn(car -> numberFormat.format(car.getValue())).setHeader("Value")
				.setComparator((v1, v2) -> (int) ((v1.getValue() - v2.getValue()) * 100))
				.setTextAlign(ColumnTextAlign.END)
				.setFlexGrow(1)
				.setResizable(true)
				.setKey("value")
				.setSortOrderProvider(d -> Arrays.asList(new QuerySortOrder("value", d)).stream());

		crudGrid.getGrid().addColumns("dealer", "carModel");

		grid.getColumnByKey("vin").setResizable(true).setFlexGrow(2);
		grid.getColumnByKey("state").setResizable(true).setFlexGrow(1);
		grid.getColumnByKey("invoiceNum").setResizable(true).setFlexGrow(1);
		grid.getColumnByKey("dealer").setResizable(true).setFlexGrow(1);
		grid.getColumnByKey("carModel").setResizable(true).setFlexGrow(1);

		grid.getColumnByKey("dealer").setSortable(true).setFlexGrow(4);
		grid.getColumnByKey("carModel").setSortable(true).setFlexGrow(1);
		
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
			Span headerLabel = new Span("Archive fully repaid cars");
			header.add(headerLabel);

			Dialog dialog = new Dialog(header);
			dialog.setWidth("400px");
			dialog.addDialogCloseActionListener(e -> dialog.close());

			Label leftLabel = new Label("Archive fully repaid cars older than ");			
			input = new Input();
			input.setValue("10");
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
				Set<Car> cars = new HashSet<>(getFilterdCars());
				LocalDate today = LocalDate.now();
				count = carService.archiveCars(cars, today, days);
				
				crudGrid.refreshGrid();
			}
			if(count == 0) {
				Notification.show("There are no cars for archiving");
			} else {
				Notification.show("Archived " + count + " cars");
			}
		    dialog.close();
		});
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
		vinFilter.getElement().setAttribute("style", "width: 170px;");		
		crudGrid.getCrudLayout().addFilterComponent(vinFilter);

		stateFilter = new TextField("Filter by State");
		stateFilter.addValueChangeListener(e -> crudGrid.refreshGrid());
		vinFilter.getElement().setAttribute("style", "width: 170px;");		
		crudGrid.getCrudLayout().addFilterComponent(stateFilter);

		dealerFilter = new TextField("Filter by Dealer");
		dealerFilter.addValueChangeListener(e -> crudGrid.refreshGrid());
		vinFilter.getElement().setAttribute("style", "width: 170px;");		
		crudGrid.getCrudLayout().addFilterComponent(dealerFilter);

		carModelFilter = new TextField("Filter by Car Model");
		carModelFilter.addValueChangeListener(e -> crudGrid.refreshGrid());
		vinFilter.getElement().setAttribute("style", "width: 170px;");		
		crudGrid.getCrudLayout().addFilterComponent(carModelFilter);
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
				.getCarsWithoutArchive()
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
							;

				}).collect(Collectors.toList());
	}

}
