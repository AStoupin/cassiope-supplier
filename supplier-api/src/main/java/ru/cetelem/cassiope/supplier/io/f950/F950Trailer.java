package ru.cetelem.cassiope.supplier.io.f950;

import java.util.Date;

public class F950Trailer implements F950Entry {
	/*
	EntryType       Define type of the entry   Alphabetic(2)   "ZZ"
	FlowReference   Flow Reference             Alphabetic(4)   "F950"
	Date            Date of file creation      Alphabetic(8)   - YYYYMMDD -
	Time            Time of file generation    Alphabetic(4)   - HHMM -
	NbEntries       Number of "DT" entries     Numeric(15)     -
	*/

	public String entryType;
	public String flowReference;
	public Date date;
	public long nbEntries;

	@Override
	public String toString() {
		return new StringBuilder(128)
		    .append("F950Trailer [entryType=").append(entryType)
		    .append(", flowReference=").append(flowReference)
		    .append(", date=").append(date)
		    .append(", nbEntries=").append(nbEntries)
		    .append("]").toString();
	}

}
