package ru.cetelem.supplier.util;

import java.time.LocalDate;

import ru.cetelem.cassiope.supplier.model.Car;
import ru.cetelem.cassiope.supplier.model.CarModel;
import ru.cetelem.cassiope.supplier.model.Dealer;

public class CarExcel {
	private String vin;
	private String state;
	private String issueDate;
	private String invoiceNum;
	private Double value;
	private String dealer;
	private String carModel;

	public CarExcel(Car car) {
		super();
		setVin(car.getVin());
		setState(car.getState());
		setIssueDate(car.getIssueDate());
		setInvoiceNum(car.getInvoiceNum());
		setValue(car.getValue());
		setDealer(car.getDealer());
		setCarModel(car.getCarModel());
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getInvoiceNum() {
		return invoiceNum;
	}

	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getDealer() {
		return dealer;
	}

	public void setDealer(String dealer) {
		this.dealer = dealer;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public void setCarModel(CarModel carModel) {
		String carModelName = "";
		if(carModel != null) {
			carModelName = carModel.getName();
		}
		this.carModel = carModelName;
	}

	public void setDealer(Dealer dealer) {
		String dealerName = "";
		if(dealer != null) {
			dealerName = dealer.getName();
		}
		this.dealer = dealerName;
	}

	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate.toString();
	}


}
