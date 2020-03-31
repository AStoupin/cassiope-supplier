package ru.cetelem.supplier.ui.view;

import java.util.Optional;

import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;

import ru.cetelem.cassiope.supplier.model.Car;
import ru.cetelem.cassiope.supplier.model.CarModel;
import ru.cetelem.cassiope.supplier.model.Dealer;
import ru.cetelem.cassiope.supplier.model.DealerLimit;
import ru.cetelem.cassiope.supplier.model.FinancePlan;
import ru.cetelem.supplier.service.CarService;
import ru.cetelem.supplier.service.DealerService;
import ru.cetelem.supplier.service.DictionaryService;
import ru.cetelem.supplier.service.PayloadService;
import ru.cetelem.supplier.ui.component.PayloadItemListBrief;
import ru.cetelem.supplier.ui.component.PayloadItemList;
import ru.cetelem.supplier.ui.component.RepaymentItemList;

@PageTitle("Dealer editor")
@Route(value = "dealer", layout = MainLayout.class)
public class DealerEditorView extends BaseView implements RouterLayout, HasUrlParameter<String> {
	private static final Log log = LogFactory.getLog(DealerEditorView.class); 

	private static final long serialVersionUID = 1L;
	private String dealerCode;
	private  CarService carService;
	private DictionaryService dictionaryService;
	private PayloadService payloadService;
	private DealerService delaerService;
	
	private Dealer dealer;
	
	@Autowired
	public DealerEditorView( CarService carService,
			DictionaryService dictionaryService,
			PayloadService payloadService,
			DealerService delaerService) {
		this.carService = carService;
		this.dictionaryService = dictionaryService;
		this.payloadService = payloadService;	
		this.delaerService = delaerService;
	}
	
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String dealerCode) {
   		init(dealerCode);
    }

	private void init(String dealerCode) {
		log.info(String.format("DealerEditorView init started for code: ", dealerCode));
		
		Optional<Dealer> initDealer = delaerService.findByCode(dealerCode);
		if (!initDealer.isPresent()) {
			Notification.show(String.format("%s not found. Createing new", dealerCode));
			initDealer = Optional.of(new Dealer());
		}
		
		this.dealerCode = dealerCode;
		this.dealer = initDealer.get();
		
		Binder<Dealer> binder = new Binder<>(Dealer.class);
		
		FormLayout fields = new FormLayout();
		
		TextField codeField = new TextField("code");
		TextField nameField = new TextField("name");

	    
 		
		
		TextField hardLimitField = new TextField("Hard Limit");
		TextField softLimitField = new TextField("Soft Limit");
		TextField totalFinancedField = new TextField("Total Financed");
		TextField availableAmountField = new TextField("Available Amount");
		TextField sublimitDeCcField = new TextField("Sublimit De/Cc");
		TextField totalFinancedDeCcField = new TextField("Total Financed De/Cc");
		TextField availableAmountDeCcField = new TextField("Available Amount De/Cc");

		
		binder.forField(codeField).bind(Dealer::getCode, this.dealerCode!=null?null:Dealer::setCode);
		binder.forField(nameField).bind(Dealer::getName, Dealer::setName);

		binder.forField(hardLimitField)
			.withConverter(new StringToBigDecimalConverter("Not a double")).bind(d->d.getLimit().getHardLimit(), null);
		binder.forField(softLimitField)
			.withConverter(new StringToBigDecimalConverter("Not a double")).bind(d->d.getLimit().getSoftLimit(), null);
		binder.forField(totalFinancedField)
			.withConverter(new StringToBigDecimalConverter("Not a double")).bind(d->d.getLimit().getTotalFinanced(), null);
		binder.forField(availableAmountField)
			.withConverter(new StringToBigDecimalConverter("Not a double")).bind(d->d.getLimit().getAvailableAmount(), null);
		binder.forField(sublimitDeCcField)
			.withConverter(new StringToBigDecimalConverter("Not a double")).bind(d->d.getLimit().getSublimitDeCc(), null);
		binder.forField(totalFinancedDeCcField)
			.withConverter(new StringToBigDecimalConverter("Not a double")).bind(d->d.getLimit().getTotalFinancedDeCc(), null);
		binder.forField(availableAmountDeCcField)
			.withConverter(new StringToBigDecimalConverter("Not a double")).bind(d->d.getLimit().getAvailableAmountDeCc(), null);


		
		fields.add(codeField, nameField, hardLimitField, softLimitField, totalFinancedField,
				availableAmountField, sublimitDeCcField, totalFinancedDeCcField, availableAmountDeCcField);
		add(fields);
		
		binder.readBean(dealer);
		


		
		HorizontalLayout actionsLayout = new HorizontalLayout();
		
		Button btnOK = new Button("OK", clickEvent->{
			log.info("OK button started");

			try {
				binder.writeBean(dealer);
			} catch (ValidationException e) {
				log.error(e);
				throw new RuntimeException(e);
			}
			delaerService.saveDealer(dealer);

			log.info("OK button finished");
			
			getUI().ifPresent(ui -> ui.navigate("dic/dealers"));
		});
		
		Button btnCancel = new Button("Cancel", clickEvent->{
			log.info("Cancel button started");
			getUI().ifPresent(ui -> ui.navigate("dic/dealers"));
		});
		btnOK.setId("btnOK");
		
		actionsLayout.add(btnOK, btnCancel);
		
		add(actionsLayout);
	


		
		VerticalLayout listPayloadItemsLayout = new VerticalLayout();
		PayloadItemListBrief carPayloadItemList = new PayloadItemListBrief(dealer.getLimit().getPayloadItems());
		carPayloadItemList.setHeight("300px");

		listPayloadItemsLayout.add(carPayloadItemList);
		add(listPayloadItemsLayout);

		listPayloadItemsLayout.setSizeFull();
		setSizeFull();

		showCompanyName(carService.getEnvironment());
		
		log.info("DealerEditorView init finished ");
	}
	
}
