package ru.cetelem.cassiope.supplier.io.cfl;

import java.util.List;

/**
 * trailer CFL11 Cancellation
 */
public class Cfl11Trail implements CFLEntry {
	/*
		Data name				Description								Format			Default
		Record Id				Name of record							PIC X(02)		1Y
		Total Amount			Total amount of records with such name	Numeric(17)		-
		Number of Data Records	Total number of records with such name	Numeric(05)		-                               
		Filler 					Spaces									Alphabet(114)	-                            
		Creator Company			The company creator						Alphabet(02)	UR 
	 */
	public String recordId = "1Y";
	public long totalAmount;		// Поле используется для проверки. 
									// Сумма должна совпадать с суммой по полю  F1-PAYMENT-AMOUNT
	public long numberOfRecords;	// Поле используется для проверки. 
									// Общее кол-во строк с  типом Record identifier = 11
	public String filler;			// что должно быть в этом параметре?	
									// Просто пробелы.
	public String creator;
	public String filler2;			// Filler2. Просто пробелы. 116 пробелов

	public Cfl11Trail() {

	}

	public Cfl11Trail(String creator) {
		this.creator = creator;
		this.numberOfRecords = 1;
	}

	public Cfl11Trail(String creator, List<Cfl11Item> cfl11Items) {
		this.creator = creator;
		this.numberOfRecords = cfl11Items.size() + 1;
		cfl11Items.stream().forEach(
				item -> this.totalAmount += item.paymentAmount);
	}

}
