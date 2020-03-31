package ru.cetelem.cassiope.supplier.io.icfl;

import java.util.Date;

import ru.cetelem.cassiope.supplier.model.Car;

import com.fasterxml.jackson.annotation.JsonIgnore;

// @formatter:off
/**
 * Details ICFL11 Cancellation (setting cancelled status for a car ) <br />
 * Данные по а/м для отмены инициализации машины в Банке (ICFL11 file). <br />
 * ICFL/ICFL11 BANK->SUPPLIER In ICFL Invoice <br />
 * До 11-00 дня дня, предшествующего финансированию
 */
public class ICfl11Item implements ICFLEntry {

	/*
		Details ICFL11 Cancellation  (setting cancelled status for a car )	
		-----------------------------------------------------------------------------------		
		Data name			Description								Format			Default
		-----------------------------------------------------------------------------------		
		F1-RECORD-ID		Record identifier						Numeric(02)		‘11’               
		F1-DEALER-CODE		Dealer code (example: UR291, UR007)		Alphebet(05)	-               
		F1-INVOICE-NUMBER	Vehicle invoice							Numeric(07)		-               
		F1-PAYMENT-AMOUNT	Vehicle financed amount					Numeric(13)		-
		Currency			Currency code (RUR)						Alphabet(03)	-
		Financed Date (A)	Vehicle financed date					DDMMYYYY		-
		F1-VIN				Vehicle chassis - VIN					Alphabet(17)	-
		F1-PROCESING-DATE	Finance cancellation date (yyyymmdd)	DDMMYYYY		-
		Filler				spaces									Alphabet(75)	-          
		Creator Company		The company creator						Alphabet(02)	UR
	 */

	public String recordId = "11"; 	// F1-RECORD-ID
	public String dealerCode; 		// F1-DEALER-CODE
	public String invoiceNumber; 	// F1-INVOICE-NUMBER
									// Номер инвойса из ICFL22. Нам он нужен для
									// поиска в базе
	public long paymentAmount; 		// F1-PAYMENT-AMOUNT
	public String currency; 		// Currency
	public Date financeDate; 		// Financed Date (A)
									// Дата выгрузки файла
	public String vin; 				// F1-VIN
	public Date processingDate; 	// F1-PROCESING-DATE
									// Дата выгрузки файла
	public String filler; 			// Filler
									// Просто пробелы. 75 пробелов
	public String creator; 			// Creator Company
	public String filler2;

	@JsonIgnore
	public Car car;

}
