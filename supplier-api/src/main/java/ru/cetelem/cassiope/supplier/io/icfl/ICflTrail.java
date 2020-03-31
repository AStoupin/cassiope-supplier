package ru.cetelem.cassiope.supplier.io.icfl;

public class ICflTrail implements ICFLEntry {

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
	public String filler2;

	public ICflTrail() {

	}

	public ICflTrail(ICfl icfl, String creator) {
		this.recordId = "ZZ";
		this.creator = creator;
		this.totalAmount = icfl.icfl22Items.size() + icfl.icfl11Items.size() + 6;
	}

}
