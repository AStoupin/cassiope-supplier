package ru.cetelem.cassiope.supplier.io.f910;

import java.util.Date;

public class F910Header implements F910Entry {
	/*
EntryType		Define type of the entry	Alphabetic(2)				"AA"
FlowReference	Flow Reference				Alphabetic(4)				"F910"
Date			Date of file creation		Alphabetic(8) – YYYYMMDD	-
Time			Time of file generation		Alphabetic(4) - HHMM		-
	 */
	
	public String entryType;
	public String flowReference;
	public Date date;
	public Date time;

	public F910Header(){
		
	}

	public F910Header(Date batchDate){
		this.entryType = "AA";
		this.flowReference = "F910";
		this.date = batchDate;
		this.time = batchDate;
//		this.date.setTime(batchDate.getTime());
	}

	@Override
	public String toString() {
		return new StringBuilder(128)
		    .append("F910Header [entryType=").append(entryType)
		    .append(", flowReference=").append(flowReference)
		    .append(", date=").append(date)
		    .append("]").toString();
	}

}
