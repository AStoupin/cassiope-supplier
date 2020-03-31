package ru.cetelem.cassiope.supplier.io.f920;

import java.util.Date;

public class F920Item implements F920Entry {
	/*05  PAY-D-CDISOLOC    	(not used)	Alphabet(02)	"RU"
05  PAY-D-COMPANY     	Company code	Alphabet(01)	1=Ford, 6=new
05  PAY-D-BUS-TYPE    	Business type	Alphabet(02)	To be picked up from portfolio files
05  PAY-D-INVOICENBR  	Vehicle invoice	Numeric(07)	
05  PAY-D-CHASSIS     	Vehicle chassis - UIN	Alphabet(17)	
05  PAY-D-BRANCH      	(not used)	Numeric(02)	00
05  PAY-D-DLR-CODE    	Dealer code	Numeric(05)	
05  PAY-D-VALUEDATE   	Payment value date	Alphabet(08) - YYYYMMDD	
05  PAY-D-ACCTDATE    	Payment accounting date	Alphabet(08) - YYYYMMDD	
05  PAY-D-PAYAMNT     	Payment amount	Numeric(12.2)	
05  PAY-D-VEHGATREL   	(not mandatory) Vehicle gate release	Alphabet(08) - YYYYMMDD	If provided, must match the Vehicle Gate Rel. date; else - 00000000
05  PAY-D-CURRENCY    	Currency code	Alphabet(03)	“RUR”
05  PAY-D-STATUS      	Transaction type: Allocation, Cancellation	Alphabet(02)	“AL”
05  PAY-D-TYPE        	Payment type: Interest or Capital	Alphabet(01)	“C”
05  PAY-D-REFERENCE   	Reference, short description	Alphabet(10)	
05  PAY-D-INTERESTINVO	(not used)	Numeric(8)	00000000
05  PAY-D-MONTH-END   	(not used)	Numeric(8)	00000000
05  FILLER            	spaces	Alphabet(14)	

	 */
	
	
	
	
	public String recordId = "RU";
	public String company;
	public String businessType;
	public String invoiceNumber;
	public String vin;
	public String branch;
	public String dealerCode;
	public Date	valueDate;	
	public Date	accountingDate;
	public long	amount;	
	public long	releaseDate;	
	public String currency;
	public String status;	
	public String paymentType;	
	public String origin;	
	public String intrestingVo;	
	public String payDMonth;	
	public String filler;

}
