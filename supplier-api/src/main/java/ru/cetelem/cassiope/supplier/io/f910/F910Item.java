package ru.cetelem.cassiope.supplier.io.f910;

import java.util.Date;

import ru.cetelem.cassiope.supplier.model.Car;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class F910Item implements F910Entry {
	/*
EntryType				Define type of the entry		Alphabetic(2)	"DA"
DealerIDReference-BL	Reference of the dealer the car is sent to - BL reference (Ex.: 00231, 00007)	Alphabetic(5)
Car Financing model 	Car Financing model				Alphabetic(2)
InvoiceID - BL/VK		ID of the invoice				Alphabetic(8)

VATInvoiceID - BL/VK	ID of the invoice (proformat)	Alphabetic(8)
VIN						Vehicule ID						Alphabetic(17)
InvoiceAmount			Amount to be financed including VAT (in currency of the contract = local currency)	Numeric(15)
FinancingDate			Date fo financing				Alphabetic(8) – YYYYMMDD
	 */
		
	public String entryType = "DA";
	public String dealerCode;
	public String carFinModel = "  ";	// что за модель?	2 пробела
	public String invoiceID;			// Мы выгружаем ваш номер инвойса (F2-INVOICE-NUMBER) из CFL22
	
	public String vatInvoiceId;			// Номер с\ф
	public String vin;
	public long invoiceAmount;
	public Date financingDate;			// Мы выгружаем дату фактического финансирования

	@Override
	public String toString() {
		return new StringBuilder(128)
		    .append("F910Item [entryType=").append(entryType)
		    .append(", dealerCode=").append(dealerCode)
		    .append(", carFinModel=").append(carFinModel)
		    .append(", invoiceID=").append(invoiceID)
		    
		    .append(", vatInvoiceId=").append(vatInvoiceId)
		    .append(", vin=").append(vin)
		    .append(", invoiceAmount=").append(invoiceAmount)
		    .append(", financingDate=").append(financingDate)
		    
		    .append("]").toString();
	}

	@JsonIgnore
	public Car car;	
}
