package ru.cetelem.cassiope.supplier.io.f950;

import java.util.Date;

public class F950Header implements F950Entry {
	/*
	EntryType       Define type of the entry   Alphabetic    2   AA
	FlowReference   Flow Reference             Alphabetic    4   F950
	Date            Date of file creation      Alphabetic    8   YYYYMMDD
	Time            Time of file generation    Alphabetic    4   HHMM
	*/

	public String entryType;
	public String flowReference;
	public Date date;

	@Override
	public String toString() {
		return new StringBuilder(128)
		    .append("F950Header [entryType=").append(entryType)
		    .append(", flowReference=").append(flowReference)
		    .append(", date=").append(date)
		    .append("]").toString();
	}

}
