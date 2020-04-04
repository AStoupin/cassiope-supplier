package ru.cetelem.supplier.ui.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ru.cetelem.cassiope.supplier.io.PayloadType;
import ru.cetelem.cassiope.supplier.model.Payload;
import ru.cetelem.supplier.service.DictionaryService;
import ru.cetelem.supplier.service.PayloadService;
import ru.cetelem.supplier.ui.component.PayloadItemList;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.StreamResource;

@PageTitle("Payload editor")
@Route(value = "payload", layout = MainLayout.class)
public class PayloadEditorView extends BaseView implements RouterLayout,
		HasUrlParameter<String> {
	private static final Log log = LogFactory.getLog(PayloadEditorView.class);

	private PayloadService payloadService;
	private DictionaryService dictionaryService;
	private Payload payload;

	Binder<Payload> binder;

	private TextField nameField;
	private TextField stateField;
	private DatePicker dateField;
	private ComboBox<PayloadType> payloadTypeField;
	private PayloadItemList itemList;
	private Button btnSubmit;
	private Button btnCancel;
	private Anchor download;
	private Button rollback;

	private boolean isReadOnly;

	public PayloadEditorView(@Autowired PayloadService payloadService,
			@Autowired DictionaryService dictionaryService) {
		this.payloadService = payloadService;
		this.dictionaryService = dictionaryService;

	}

	@Override
	public void setParameter(BeforeEvent event,
			@OptionalParameter String payloadName) {
		init(payloadName);
	}

	private void init(String payloadName) {
		Optional<Payload> initPayload = payloadService
				.getPayloadByName(payloadName);
		if (!initPayload.isPresent()) {
			if (payloadName != null) {
				Notification.show(String.format(
						"%s not found. Creating for a new", payloadName));
			}

			initPayload = Optional.of(new Payload());

		}

		this.payload = initPayload.get();
		isReadOnly = "NEW".equals(payload.state);

		binder = new Binder<>();

		FormLayout fieldsLayout = new FormLayout();

		nameField = new TextField("Name");
		nameField.setEnabled(isReadOnly);
		stateField = new TextField("State");
		stateField.setEnabled(isReadOnly);
		dateField = new DatePicker("Date");
		dateField.setId("dateField");
		dateField.setEnabled(isReadOnly);
		payloadTypeField = new ComboBox("Payload Type");
		payloadTypeField.setEnabled(isReadOnly);
		payloadTypeField.setId("payloadTypeField");
		payloadTypeField.setItems(PayloadType
				.getList(PayloadType.Direction.OUT));

		binder.forField(nameField).bind(Payload::getName, null);
		binder.forField(stateField).bind(Payload::getState, null);
		binder.forField(dateField).bind(Payload::getDateOnly, Payload::setDateOnly);
		binder.forField(payloadTypeField).bind(Payload::getPayloadType,
				Payload::setPayloadType);

		fieldsLayout.add(nameField, stateField, payloadTypeField, dateField);
		add(fieldsLayout);

		binder.readBean(payload);

		payloadTypeField.addValueChangeListener(e -> {
			Payload p = getPayload();
			payload.name = payloadService
					.generatePayloadName(payload.payloadType);
			payload.sequenceNumber = payloadService
					.generatePayloadSequence(payload.payloadType);

			p.getPayloadItems().clear();
			itemList.reloadList(p);

			download.setHref(getPayloadResource());
			download.setVisible(payload.name != null
					&& payload.name.length() > 0);

			binder.readBean(payload);
			
		});

		HorizontalLayout actionsLayout = new HorizontalLayout();

		if (isReadOnly) {
			btnSubmit = new Button("Submit", clickEvent -> {
				payloadService.submitPayload(getPayload());

				getUI().ifPresent(ui -> ui.navigate("payloads"));
			});
			btnSubmit.setId("btnSubmit");
			actionsLayout.add(btnSubmit);
		}

		btnCancel = new Button(isReadOnly ? "Cancel" : "OK", clickEvent -> {
			getUI().ifPresent(ui -> ui.navigate("payloads"));
		});

		actionsLayout.add(btnCancel);

		download = new Anchor();
		download.setHref(getPayloadResource());
		download.getElement().setAttribute("download", true);
		download.getElement().setAttribute("title", "Download file");
		download.add(new Button(new Icon(VaadinIcon.DOWNLOAD_ALT)));
		download.setVisible(getPayload().name != null
				&& getPayload().name.length() > 0);

		actionsLayout.add(download);

		if(payload != null && payload.payloadType != null
				&& "F120".equals(payload.payloadType.name())
				&& "PROCESSED".equals(payload.getState())) {
			rollback = new Button("Rollback");
			rollback.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
				@Override
				public void onComponentEvent(ClickEvent<Button> event) {
					ConfirmDialog dialog = new ConfirmDialog
							("Confirm rollback",
					        "Are you sure you want to rollback the payload item?",
					        "Rollback", eventButton -> {
								log.debug("Rollback confirmed");
								payload.setRollbackDate(LocalDate.now());
								payloadService.savePayload(getPayload());
								getUI().ifPresent(ui -> ui.navigate("payloads"));
							}, "Cancel", eventButton -> {});
					dialog.setConfirmButtonTheme("error	primary");
					dialog.open();
				}
	        });
			actionsLayout.add(rollback);
		}

		add(actionsLayout);

		setHeightFull();

		VerticalLayout listLayout = new VerticalLayout();
		itemList = new PayloadItemList(payloadService, dictionaryService,
				payload);
		itemList.setId("itemList");
		itemList.setSizeFull();
		listLayout.add(itemList);

		add(listLayout);

		listLayout.setSizeFull();
		setSizeFull();
		setPadding(true);

		/*
		 * listLayout.setHeightFull(); listLayout.setWidthFull();
		 * itemList.setHeightFull(); itemList.setWidthFull();
		 */

	}

	private StreamResource getPayloadResource() {
		Payload p = getPayload();
		String fileName;
		if (p.name == null || p.name.length() == 0) {
			fileName = "filename.txt";
		} else {
			fileName = p.name;
		}

		return new StreamResource(fileName, () -> createInputStream());
	}

	private InputStream createInputStream() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			PayloadType payloadType = payload.payloadType;
			if (payloadType == null) {
				payloadType = PayloadType.CFL;
			}
			String mapping = payloadType.mapping;
			String streamName = payloadType.streamName;

			StreamFactory factory = StreamFactory.newInstance();
			// load the mapping file
			try {
				InputStream inputStream = PayloadEditorView.class
						.getResourceAsStream("/mappings/" + mapping);
				factory.load(inputStream);
			} catch (Exception e) {
				log.info("createInputStream " + e);
				outputStream.write("createInputStream error".getBytes());
				throw new IOException("createInputStream");
			}

			String fileNameTemp = "./output/tempfile.txt";
			File file = new File(fileNameTemp);
			BeanWriter out = factory.createWriter(streamName, file);

			for (Object item : payload.payloadItems.stream()
					.map(payloadService::createObjectByPayloadItem)
					.collect(Collectors.toList())) {
				out.write(item);
			}

			out.close();

			byte[] array = Files.readAllBytes(Paths.get(fileNameTemp));
			outputStream.write(array);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(outputStream.toByteArray());
	}

	private Payload getPayload() {
		try {
			binder.writeBean(payload);
		} catch (ValidationException e) {
			log.error(e);
			throw new RuntimeException(e);
		}
		return payload;
	}

}
