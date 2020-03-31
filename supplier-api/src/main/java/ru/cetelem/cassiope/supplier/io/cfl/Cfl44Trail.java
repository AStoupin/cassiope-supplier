package ru.cetelem.cassiope.supplier.io.cfl;

public class Cfl44Trail implements CFLEntry {
	/*
Record Id	Name of record	PIC X(02)	4Y
Total Amount	Total amount of records with such name	Numeric(17)	-
Number of Data Records	Total number of records with such name	PIC 9(05)	-                               
Filler 	Spaces	PIC X(114)	-                            
Creator Company	The company creator	PIC X(02)	UR 
	 */
	public String recordId = "4Y";
	public long totalAmount;
	public long numberOfRecords;
	public String filler;
	public String creator;
	public String filler2;			// Filler2. Просто пробелы. 116 пробелов

	public Cfl44Trail() {

	}

	public Cfl44Trail(String creator) {
		this.creator = creator;
		this.numberOfRecords = 1;
	}

}
