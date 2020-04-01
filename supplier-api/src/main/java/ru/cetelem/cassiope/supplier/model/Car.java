package ru.cetelem.cassiope.supplier.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "CAR")
public class Car {

	@Id
	@GeneratedValue
	@Column(name = "Id", nullable = false)
	private int id;

	private String vin;

	private double value;
	private double valueFinance;
	private LocalDate issueDate;
	private LocalDate submitDate;
	private LocalDate financeDate;
	private LocalDate fullRepaymentDate;
	@Column(name = "invoice_num", nullable = true)
	private String invoiceNum;
	@ManyToOne(targetEntity = CarModel.class, optional = false, fetch = FetchType.EAGER)
	private CarModel carModel;
	@ManyToOne(targetEntity = Dealer.class, optional = true, fetch = FetchType.EAGER)
	private Dealer dealer;

	@ManyToOne(targetEntity = FinancePlan.class, optional = true, fetch = FetchType.EAGER)
	private FinancePlan financePlan;

	@OneToMany(mappedBy = "car", cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<RepaymentItem> repaymentItems  = new ArrayList<>();
	@OneToMany(mappedBy = "car", cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<PayloadItem> payloadItems  = new ArrayList<>();
	
	private String eptsNumber;

	//NEW-(USER)->READY TO SUBMIT-(CFL extracted)->SUBMITTED CFL-(F150 extracted)->SUBMITTED-(F950 imported)->
	//    READY TO FINANCE-(F120 extracted)->FINANCE REQUESTED-(F910 imported)->FINANCED-(F920 imported)->REPAID/FULL REPAYED
	// F940 - DEALER LIMIT
	// ICFL - загрузка NEW Car
	public String state;

	private LocalDate archivedDate;

	public Car(){ 
		issueDate = LocalDate.now();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<PayloadItem> getPayloadItems() {
		return payloadItems;
	}

	public void setPayloadItems(List<PayloadItem> payloadItems) {
		this.payloadItems = payloadItems;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getValueFinance() {
		return valueFinance;
	}

	public void setValueFinance(double valueFinance) {
		this.valueFinance = valueFinance;
	}

	public LocalDate getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}

	public LocalDate getFinanceDate() {
		return financeDate;
	}

	public void setFinanceDate(LocalDate financeDate) {
		this.financeDate = financeDate;
	}

	public LocalDate getFullRepaymentDate() {
		return fullRepaymentDate;
	}

	public void setFullRepaymentDate(LocalDate fullRepaymentDate) {
		this.fullRepaymentDate = fullRepaymentDate;
	}

	public String getInvoiceNum() {
		return invoiceNum;
	}

	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	public CarModel getCarModel() {
		return carModel;
	}

	public void setCarModel(CarModel carModel) {
		this.carModel = carModel;
	}

	public Dealer getDealer() {
		return dealer;
	}

	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public FinancePlan getFinancePlan() {
		return financePlan;
	}

	public void setFinancePlan(FinancePlan financePlan) {
		this.financePlan = financePlan;
	}

	public LocalDate getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(LocalDate submitDate) {
		this.submitDate = submitDate;
	}

    public List<RepaymentItem> getRepaymentItems() {
		return repaymentItems;
	}

	public void setRepaymentItems(List<RepaymentItem> repaymentItems) {
		this.repaymentItems = repaymentItems;
	}

	public String getEptsNumber() {
		return eptsNumber;
	}

	public void setEptsNumber(String eptsNumber) {
		this.eptsNumber = eptsNumber;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		if (this.vin == null) return false;
		if (this.getClass() != o.getClass()) return false;


		Car car = (Car) o;
		if (car.vin==null) return false;


		return this.vin.equals(car.vin);
	}

	@Override
	public String toString() {
		return "Car [vin=" + vin + "]";
	}

	public LocalDate getArchivedDate() {
		return archivedDate;
	}

	public void setArchivedDate(LocalDate archivedDate) {
		this.archivedDate=archivedDate;
	}	
}
