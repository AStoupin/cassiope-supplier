package ru.cetelem.cassiope.supplier.io.f920;

import java.util.Date;

public class F920Header implements F920Entry {
/*05  PAY-H-CODE	Header record identifier	Alphabet(02) 	“AA”
05  PAY-H-COMPANYD	Company code	Numeric(01)	1=Ford, 6=new
05  PAY-H-PAYM-DATE	File generation date	Alphabet(08) - YYYYMMDD	
05  PAY-H-SEQ-NUM	File number	Numeric(07)	Increasing counter
05  PAY-H-REC-TYPE	Free text to indicate origin/purpose	Alphabet(57)	"F5 CAPITAL PAYMENTS”
05  PAY-H-PAYM-TIME	File generation time	Alphabet(06) - hhmmss	
05  FILLER	spaces	Alphabet(04)	
*/
	
	public String recordId = "AA";
	public String company;
	public Date generationDate;
	public long fileNumber;
	public String origin;
	public Long generationTime;
	public String filler;

	
}
