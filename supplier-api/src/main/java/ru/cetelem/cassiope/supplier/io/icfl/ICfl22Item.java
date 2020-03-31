package ru.cetelem.cassiope.supplier.io.icfl;

import java.util.Date;

import ru.cetelem.cassiope.supplier.model.Car;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ICfl22Item implements ICFLEntry {

	/*
	Record Id				Record identifier					Numeric(02)
	F2-DEALER-CODE			Dealer code. Example: UR007, UR193	Alphabet(05)
	F2-INVOICE-NUMBER		Vehicle invoice						Numeric(07)
	Financed Amount (B)		Vehicle financed amount				Numeric(13)
	Currency				Currency code (RUR)					Alphabet(03)
	Finance 													Date (A)	-	DDMMYYYY    
	Wholesale 													Date (C)	-	DDMMYYYY
	F2-VIN					Vehicle chassis - VIN				Alphabet(17)
	Model					-									Alphabet(04)
	Order Number			-									Alphabet(05)
	Plate Number			-									Alphabet(10)
	VAT Amount				-									Numeric(13)
	F2-PAYMENT-AMOUNT		Vehicle financed amount				Numeric(13)
	F2-Plan-Code			Financial plan code. This data will not be used.	Alphabet(02)
	Business Type 			-									Alphabet(02)
	Filler					-									Alphabet(26)
	Creator 				Company	The company creator			Alphabet(02)
	*/

	public String recordId = "22";
	public String dealerCode;
	public String invoiceNumber;
	public long invoiceAmount;
	public String currency;
	public Date financeDate;
	public Date whosaleDate;
	public String vin;
	public String model;
	public String orderNumber;
	public String plateNumber;
	public long vatAmount;
	public long paymentAmount;
	public String planCode;
	public String businessType;
	public String filler;
	public String creator;
	public String filler2;

	@JsonIgnore
	public Car car;

}
