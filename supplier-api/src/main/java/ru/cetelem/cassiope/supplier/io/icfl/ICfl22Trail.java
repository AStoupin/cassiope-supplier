package ru.cetelem.cassiope.supplier.io.icfl;

import java.util.List;

public class ICfl22Trail implements ICFLEntry {

	/*
	Record Id				Name of record							PIC X(02)
	Total Amount			Total amount of records with such name	Numeric(17)
	Number of Data Records	Total number of records with such name	PIC 9(05)
	Filler 					Spaces									PIC X(114)
	Creator 				Company	The company creator				PIC X(02)
	*/

	public String recordId = "2Y";
	public long totalAmount;
	public long numberOfRecords;
	public String filler;
	public String creator;
	public String filler2;

	public ICfl22Trail() {

	}

	public ICfl22Trail(String creator) {
		this.creator = creator;
		this.numberOfRecords = 1;

	}

	public ICfl22Trail(String creator, List<ICfl22Item> icfl22Items) {
		this.creator = creator;
		this.numberOfRecords = icfl22Items.size() + 1;
		icfl22Items.stream().forEach(
				item -> this.totalAmount += item.invoiceAmount);
	}

}
