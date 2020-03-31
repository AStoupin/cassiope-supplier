package ru.cetelem.cassiope.supplier.io.f940;

import java.util.Date;

import ru.cetelem.cassiope.supplier.io.f940.F940Entry;

public class F940Header implements F940Entry {
	// @formatter:off
	/*
EntryType		Define type of the entry	Alphabetic(2)				"AA"
FlowReference	Flow Reference				Alphabetic(4)				"F940"
Date			Date of file creation		Alphabetic(8) – YYYYMMDD	-
Time			Time of file generation		Alphabetic(4) - HHMM		-
	 */
	// @formatter:on
	
	public String entryType;
	public String flowReference;
	public Date date;
	public Date time;

	public F940Header(){
		
	}

	public F940Header(Date batchDate){
		this.entryType = "AA";
		this.flowReference = "F940";
		this.date = batchDate;
		this.time = batchDate;
//		this.date.setTime(batchDate.getTime());
	}

	@Override
	public String toString() {
		return new StringBuilder(128)
		    .append("F940Header [entryType=").append(entryType)
		    .append(", flowReference=").append(flowReference)
		    .append(", date=").append(date)
		    .append("]").toString();
	}

}
