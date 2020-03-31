package ru.cetelem.cassiope.supplier.io.cfl;

import java.util.List;



public class Cfl22Trail implements CFLEntry {
	/*
	Record Id				Name of record							PIC X(02)
	Total Amount			Total amount of records with such name	Numeric(17)
	Number of Data Records	Total number of records with such name	PIC 9(05)
	Filler 					Spaces									PIC X(114)
	Creator 				Company	The company creator				PIC X(02)
	*/
	public String	recordId = "2Y";
	public long	totalAmount;
	public long		numberOfRecords;
	public String	filler;
	public String	creator;
	public String filler2;			// Filler2. Просто пробелы. 116 пробелов

	public Cfl22Trail() {
		
	}
	
	public Cfl22Trail(String creator) {
		this.creator = creator;
		this.numberOfRecords = 1;		
	}

	public Cfl22Trail(String creator, List<Cfl22Item> cfl22Items) {
		this.creator = creator;
		this.numberOfRecords = cfl22Items.size() + 1;
		cfl22Items.stream().forEach(item->this.totalAmount += item.invoiceAmount);		
	}

}
