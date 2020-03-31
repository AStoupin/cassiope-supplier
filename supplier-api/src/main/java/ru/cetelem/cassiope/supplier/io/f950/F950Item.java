package ru.cetelem.cassiope.supplier.io.f950;

import java.util.Date;

public class F950Item implements F950Entry {
	/*
	EntryType           Define type of the entry                     Alphabetic(2)    "DT"
	DealerIDReference   Reference of the dealer (Ex.: 00007, 00193)  Alphabetic(5)    -
	InvoiceID - BL/VK   ID of the invoice                            Alphabetic(8)    -
	InvoiceAmount       Invoice amount, including invoice VAT        Numeric(13.2)    -
	UIN                 Vehicule ID                                  Alphabetic(20)   -
	*/

	public String entryType;
	public String dealerCode;
	public String invoiceID;
	public String vatInvoiceId;
	public Date grCcDate;
	public String vin;
	public long invoiceAmount;

	@Override
	public String toString() {
		return new StringBuilder(128)
		    .append("F950Item [entryType=").append(entryType)
		    .append(", dealerCode=").append(dealerCode)
		    .append(", invoiceID=").append(invoiceID)
		    .append(", vatInvoiceId=").append(vatInvoiceId)
		    .append(", grCcDate=").append(grCcDate)
		    .append(", vin=").append(vin)
		    .append(", invoiceAmount=").append(invoiceAmount)
		    .append("]").toString();
	}

}
