package ru.cetelem.cassiope.supplier.io.icfl;

import java.util.List;

/**
 * trailer ICFL11 Cancellation
 */
public class ICfl11Trail implements ICFLEntry {

	/*
		Data name				Description								Format			Default
		Record Id				Name of record							PIC X(02)		1Y
		Total Amount			Total amount of records with such name	Numeric(17)		-
		Number of Data Records	Total number of records with such name	Numeric(05)		-                               
		Filler 					Spaces									Alphabet(114)	-                            
		Creator Company			The company creator						Alphabet(02)	UR 
	 */

	public String recordId = "1Y";
	public long totalAmount; 		// Поле используется для проверки.
									// Сумма должна совпадать с суммой по полю
									// F1-PAYMENT-AMOUNT
	public long numberOfRecords; 	// Поле используется для проверки.
									// Общее кол-во строк с типом Record
									// identifier = 11
	public String filler; 			// что должно быть в этом параметре?
									// Просто пробелы.
	public String creator;
	public String filler2;

	public ICfl11Trail() {

	}

	public ICfl11Trail(String creator) {
		this.creator = creator;
		this.numberOfRecords = 1;
	}

	public ICfl11Trail(String creator, List<ICfl11Item> icfl11Items) {
		this.creator = creator;
		this.numberOfRecords = icfl11Items.size() + 1;
		icfl11Items.stream().forEach(
				item -> this.totalAmount += item.paymentAmount);
	}

}
