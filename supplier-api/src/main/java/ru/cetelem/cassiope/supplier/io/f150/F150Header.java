package ru.cetelem.cassiope.supplier.io.f150;

import java.util.Date;

public class F150Header implements F150Entry {
	/*
1) Header				The header identifier. Only one HD line in the file, else reject file	Text(2)	“HD” 
2) System Id			The Id of the system							Text(8)	-
3) Location				Country code									Text(2)	“RU”
4) Company code			The code of the company							Text(1)	“6”
5) Date					The date of the file sending					Text(8) YYYYMMDD	-
6) Sequence number		The sequence number of the file, should always be unique	Number(9)	Auto_increment
7) Purpose description	The purpose of the file							Text(55)	“PTS_DELIVERED_TO_DHLMOW”
	 */

	public String entryType;
	public String systemId;		// В примерах этот параметр пустой, так и должно быть?	да
	public String location;
	public String companyCode;	// Код компании откуда брать?	константа = 6
	
	public Date date;
	public long sequenceNumber;
	public String purposeDescription;

	public F150Header() {

	}

	F150Header(Date batchDate) {
		this.entryType = "HD";
		this.systemId = "";
		this.location = "RU";
		this.companyCode = "6";
		
		this.date = batchDate;
		this.sequenceNumber = 0;
		this.purposeDescription = "PTS_DELIVERED_TO_DHLMOW";
	}

}
