package ru.cetelem.cassiope.supplier.io.f120;

import java.util.Date;

public class F120Trail implements F120Entry {
	/*
	EntryType	Define type of the entry	Alphabetic	2	ZZ
	FlowReference	Flow Reference	Alphabetic	4	F120
	Date	Date of file creation	Alphabetic	8	YYYYMMDD
	Time	Time of file generation	Alphabetic	4	HHMM
	NbEntries	Number of "DT" entries	Numeric	15	
	CheckSumInvoiceAmount	Sum of all details lines amount (technical sum, no exchange value calculation)	Numeric	15.2	
	 */
	public String entryType;
	public String flowReference;
	public Date date;
	public Date time;
	public long nbEntries;
	public long checkSumInvoiceAmount;	

	public F120Trail() {
		
	}
	
	public F120Trail(F120 f120) {
		this.entryType = "ZZ";
		this.flowReference = "F120";
		this.date = f120.f120Header.date;
		this.time = f120.f120Header.time;
		this.checkSumInvoiceAmount = 0;
	    f120.f120Items.stream().forEach(i->this.checkSumInvoiceAmount+=i.invoiceAmount); 
	    this.nbEntries = f120.f120Items.size(); 
	}	
}
