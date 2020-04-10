package ru.cetelem.cassiope.supplier.io.f920;

public class F920Trail implements F920Entry {
	/*
    05  PAY-F-CODE     	Trailer record identifier	Alphabet(02)	"YY"
	05  PAY-F-COMPANYD 	Company code	Numeric(01)	Same as header
	05  PAY-F-ORIGIN   	Free text to indicate origin/purpose	Alphabet(65)	
	05  FILLER         	 (blank)	Alphabet(10)	
	05  PAY-F-REC-COUNT	File record count	Numeric(10)	Counter of lines present in file, including header and trailer.
	05  FILLER         	 (blank)	Alphabet(10)	
	05  PAY-F-TOT-AMNT 	File total amount	Numeric(16.2)	Sum of the PAY-D-PAYAMNT     records in the file.
	05  FILLER         	spaces	Alphabet(14)	

	 */
	public String recordId = "YY";
	public String company;
	public String origin;
	public String filler1;
	public long	  numberOfRecords;
	public String filler2;
	public long	totalAmount;	

}
