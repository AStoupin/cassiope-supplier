package ru.cetelem.supplier.ui.view;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ru.cetelem.cassiope.supplier.model.Car;
import ru.cetelem.cassiope.supplier.model.CarModel;
import ru.cetelem.cassiope.supplier.model.Dealer;
import ru.cetelem.cassiope.supplier.model.FinancePlan;
import ru.cetelem.supplier.service.CarService;
import ru.cetelem.supplier.service.DealerService;
import ru.cetelem.supplier.service.DictionaryService;
import ru.cetelem.supplier.service.PayloadService;
import ru.cetelem.supplier.ui.component.PayloadItemListBrief;
import ru.cetelem.supplier.ui.component.RepaymentItemList;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;

@PageTitle("Car editor")
@Route(value = "car", layout = MainLayout.class)
public class CarEditorView extends BaseView implements RouterLayout, HasUrlParameter<String> {

	private static final Log log = LogFactory.getLog(CarEditorView.class); 

	private static final long serialVersionUID = 1L;
	private String vin;
	private CarService carService;
	private DictionaryService dictionaryService;
	@SuppressWarnings("unused")
	private PayloadService payloadService;
	private DealerService dealerService;
	
	private Car car;
	
	@Autowired 
	public CarEditorView(CarService carService,
			PayloadService payloadService,
			DictionaryService dictionaryService,
			DealerService dealerService) {
		
		this.carService = carService;
		this.dictionaryService = dictionaryService;
		this.dealerService = dealerService; 
		this.payloadService = payloadService;		
	}
	
    @Override
    public void setParameter(BeforeEvent event,@OptionalParameter String vin) {
   		init(vin);
    }

	private void init(String vin) {
		log.info(String.format("CarEditorView init started for vin: ", vin));
		
		Optional<Car> initCar = carService.findCarByVin(vin);
		if (!initCar.isPresent()) {
			Notification.show(String.format("%s not found. Createing new", vin));
			initCar = Optional.of(newCar());
		}
		
		this.vin = vin;
		this.car = initCar.get();
		
		Binder<Car> binder = new Binder<>();
		
		FormLayout fields = new FormLayout();
		
		TextField vinField = new TextField("Vin");
		TextField stateField = new TextField("State");
		stateField.setId("stateField");
		NumberField valueField = new NumberField("Value");
		NumberField valueFinanceField = new NumberField("Value Finance");
		valueFinanceField.setVisible(false);		
		DatePicker  issueDateField = new DatePicker("Issue Date");
		ComboBox<CarModel>  carModelField = new ComboBox<CarModel>("Car Model");
		carModelField.setItems(dictionaryService.getModels());
		ComboBox<Dealer>  dealerField = new ComboBox<Dealer>("Dealer");
		dealerField.setItems(dealerService.getDealers());
		TextField invoiceNumField = new TextField("Invoice Num"); 
		invoiceNumField.setId("invoiceNumField");
		TextField eptsNumberField = new TextField("EPTS Number"); 
		eptsNumberField.setId("eptsNumber");
		DatePicker  archivedDateField = new DatePicker("Archived Date");
		
		ComboBox<FinancePlan>  financePlanField = new ComboBox<FinancePlan>("Finance Plan");
		financePlanField.setId("financePlanField");
		financePlanField.setItems(dictionaryService.getFinancePlanes());
		
		
		binder.forField(vinField).bind(Car::getVin, this.vin!=null?null:Car::setVin);
		binder.forField(stateField).bind(Car::getState, null);
		binder.forField(valueField).bind(Car::getValue, Car::setValue);
		binder.forField(issueDateField).bind(Car::getIssueDate, Car::setIssueDate);
		binder.forField(valueFinanceField).bind(Car::getValueFinance, Car::setValueFinance);
		binder.forField(carModelField).bind(Car::getCarModel, Car::setCarModel);
		binder.forField(dealerField).bind(Car::getDealer, Car::setDealer);
		binder.forField(invoiceNumField).bind(Car::getInvoiceNum, Car::setInvoiceNum);
		binder.forField(eptsNumberField).bind(Car::getEptsNumber, Car::setEptsNumber);
		binder.forField(financePlanField).bind(Car::getFinancePlan, Car::setFinancePlan);
		binder.forField(archivedDateField).bind(Car::getArchivedDate, Car::setArchivedDate);
		
		fields.add(vinField, stateField, valueField, valueFinanceField, archivedDateField, 
				issueDateField, carModelField, dealerField, financePlanField,
				invoiceNumField, eptsNumberField);
		add(fields);
		binder.readBean(car);

		
		HorizontalLayout actionsLayout = new HorizontalLayout();
		
		Button btnOK = new Button("OK", clickEvent->{
			log.info("OK button started");
			if(valueFinanceField.getValue() == null){
				valueFinanceField.setValue(0.0);
			}
			if(valueField.getValue() == null){
				valueField.setValue(0.0);
			}
			if(!valueFinanceField.getValue().equals(valueField.getValue())){
				valueFinanceField.setValue(valueField.getValue());
			}

			try {
				binder.writeBean(car);
			} catch (ValidationException e) {
				log.error(e);
				throw new RuntimeException(e);
			}
			carService.saveCar(car);

			log.info("OK button finished");
			
			getUI().ifPresent(ui -> ui.navigate(""));
		});
		
		Button btnCancel = new Button("Cancel", clickEvent->{
			log.info("Cancel button started");
			getUI().ifPresent(ui -> ui.navigate(""));
		});
		btnOK.setId("btnOK");
		
		actionsLayout.add(btnOK, btnCancel);
		
		add(actionsLayout);
	
		VerticalLayout listLayout = new VerticalLayout();
		RepaymentItemList repaymentItemList = new RepaymentItemList(car);
		repaymentItemList.reloadList(car); 
		listLayout.add(repaymentItemList);
		repaymentItemList.setHeight("300px");
		
		add(listLayout);

		listLayout.setSizeFull();

		
		VerticalLayout listPayloadItemsLayout = new VerticalLayout();
		PayloadItemListBrief carPayloadItemList = new PayloadItemListBrief(car.getPayloadItems());
		carPayloadItemList.setHeight("300px");

		listPayloadItemsLayout.add(carPayloadItemList);
		add(listPayloadItemsLayout);

		listPayloadItemsLayout.setSizeFull();
		setSizeFull();
		
		setPadding(true);
		
		log.info("CarEditorView finished ");
	}

	private Car newCar() {
		Car car = new Car();
		car.setState("NEW");
//		car.setIssueDate(LocalDate.now());
		return car;
	}

}
