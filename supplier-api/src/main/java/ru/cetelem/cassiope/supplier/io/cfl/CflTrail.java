package ru.cetelem.cassiope.supplier.io.cfl;

public class CflTrail implements CFLEntry {
	/*
	Record Id		Name of record			PIC X(02)	ZZ
	Total Amount	Total amount of records	Numeric(5)	-
	Filler 			Spaces					Alphabet(131)	-                            
	Creator Company	The company creator		Alphabet(02)	UR 
	*/
	
	public String recordId;
	public long totalAmount;
	public String filler;
	public String creator;
	public String filler2;			// Filler2. Просто пробелы. 116 пробелов
	
	public CflTrail(){
		
	}
	
	public CflTrail(Cfl cfl, String creator) {
		this.recordId = "ZZ";
		this.creator = creator;
		this.totalAmount= cfl.cfl11Items.size() + cfl.cfl22Items.size() + 6;
	}
	
}
