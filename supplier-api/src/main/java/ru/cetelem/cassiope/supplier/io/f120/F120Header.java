package ru.cetelem.cassiope.supplier.io.f120;

import java.util.Date;

public class F120Header implements F120Entry {
	/*
	EntryType		Define type of the entry	Alphabetic	2	AA
	FlowReference	Flow Reference				Alphabetic	4	F120
	Date			Date of file creation		Alphabetic	8	YYYYMMDD
	Time			Time of file generation		Alphabetic	4	HHMM
	 */
	
	public String entryType;
	public String flowReference;
	public Date date;
	public Date time;

	public F120Header(){
		
	}

	F120Header(Date batchDate){
		this.entryType = "AA";
		this.flowReference = "F120";
		this.date = batchDate;
		this.time = batchDate;
//		this.date.setTime(batchDate.getTime());
	}
	
}
