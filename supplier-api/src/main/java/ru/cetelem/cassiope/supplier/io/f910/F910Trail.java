package ru.cetelem.cassiope.supplier.io.f910;

import java.util.Date;

import ru.cetelem.cassiope.supplier.io.f910.F910;

public class F910Trail implements F910Entry {
	/*
EntryType					Define type of the entry	Alphabetic(2)			"ZZ"
FlowReference				Flow Reference				Alphabetic(4)			"F910"
Date						Date of file creation		Alphabetic(8) – YYYYMMDD	
Time						Time of file generation		Alphabetic(4)	
NbEntriesfinancingRequest	Number of "DA" entries		Numeric(15)	
CheckSumInvoiceAmount		Sum of all details lines amount (technical sum, no exchange value calculation)	Numeric(17)
	 */

	public String entryType;
	public String flowReference;
	public Date date;
	public Date time;
	public long nbEntries;
	public long checkSumInvoiceAmount;

	@Override
	public String toString() {
		return new StringBuilder(128)
			.append("F910Trailer [entryType=").append(entryType)
			.append(", flowReference=").append(flowReference)
			.append(", date=").append(date)
			.append(", nbEntries=").append(nbEntries)
			.append(", checkSumInvoiceAmount=").append(checkSumInvoiceAmount)
			.append("]").toString();
	}

	public F910Trail() {
		
	}
	
	public F910Trail(F910 f910) {
		this.entryType = "ZZ";
		this.flowReference = "F910";
		this.date = f910.f910Header.date;
		this.time = f910.f910Header.time;
		this.checkSumInvoiceAmount = 0;
	    f910.f910Items.stream().forEach(i->this.checkSumInvoiceAmount+=i.invoiceAmount); 
	    this.nbEntries = f910.f910Items.size(); 
	}	
}
