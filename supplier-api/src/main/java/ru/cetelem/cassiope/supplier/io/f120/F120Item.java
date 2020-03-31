package ru.cetelem.cassiope.supplier.io.f120;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ru.cetelem.cassiope.supplier.model.Car;
/*
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;*/
/*
@Entity
@Table(name = "F120_ITEM")
*/
public class F120Item implements F120Entry {
	/*
	EntryType	Define type of the entry	Alphabetic	2
	Dealer Code	Examle: 00007, 00193	Alphabetic	5
	InvoiceID - BL/VK	ID of the invoice (proforma), same ad the one used in BLESS and Eurovac	Alphabetic	7
	VATInvoiceID - BL/VK	ID of the VAT invoice = VAT invoice number 	Alphabetic	8
	GR-CC-Date	"Date of car sending to dealer
	GR: Gate Release
	CC: Custom Clearing"	Alphabetic	8
	VIN	Vehicule ID	Alphabetic	17
	InvoiceAmount	Amount to be financed, including VAT (in currency of the contract = local currency)	Numeric	13.2
	 */
/*	@Id
    @GeneratedValue
    @Column(name = "Id", nullable = false)		
	public int id;
	*/
	public String entryType;
	public String dealerCode;
	public String invoiceID;
	public String vatInvoiceId;
	public Date grCcDate;
	public String vin;
	public long invoiceAmount;
	
	public String errorDescr;
	
	
	@JsonIgnore
	public Car car;	
}
