package ru.cetelem.cassiope.supplier.io.f940;

import java.util.Date;

import ru.cetelem.cassiope.supplier.io.f940.F940;

public class F940Trail implements F940Entry {
	// @formatter:off
	/*
EntryType						Define type of the entry					Alphabetic(2)			"ZZ"
FlowReference					Flow Reference								Alphabetic(4)			"F940"
Date							Date of file creation						Alphabetic(8) – YYYYMMDD	
Time							Time of file generation						Alphabetic(4)	

NbEntries						Number of "DT" entries						Numeric(15.2)
CheckSumHardLimits				Sum of HardLimitATLAS in DT entries			Numeric(15.2)
CheckSumSoftLimits				Sum of SoftLimitATLAS in DT entries			Numeric(15.2)
CheckSumTotalFinanced			Sum of TotalFinanced in DT entries			Numeric(15.2)

CheckSumAvailableAmount			Sum of SumAvialable in DT entries			Numeric(15.2)
CheckSumSubLimits_DE_CC			Sum of SubLimit for DE/CC in DT entries		Numeric(15.2)
CheckSum DE/CCTotalFinanced		Sum of DE/CC TotalFinanced in DT entries	Numeric(15.2)
CheckSumAvailableAmount_DE_CC	Sum of SumAvialable in DT entries for DE/CC	Numeric(15.2)
	 */
	// @formatter:on

	public String entryType;
	public String flowReference;
	public Date date;
	public Date time;

	public long nbEntries;
	public long checkSumHardLimits;
	public long checkSumSoftLimits;
	public long checkSumTotalFinanced;

	public long checkSumAvailableAmount;
	public long checkSumSubLimits_DE_CC;
	public long checkSumTotalFinanced_DE_CC;
	public long checkSumAvailableAmount_DE_CC;

	@Override
	public String toString() {
		return new StringBuilder(128)
			.append("F940Trailer [entryType=").append(entryType)
			.append(", flowReference=").append(flowReference)
			.append(", date=").append(date)
			
			.append(", nbEntries=").append(nbEntries)
			.append(", checkSumHardLimits=").append(checkSumHardLimits)
			.append(", checkSumSoftLimits=").append(checkSumSoftLimits)
			.append(", checkSumTotalFinanced=").append(checkSumTotalFinanced)

			.append(", checkSumAvailableAmount=").append(checkSumAvailableAmount)
			.append(", checkSumSubLimits_DE_CC=").append(checkSumSubLimits_DE_CC)
			.append(", checkSumTotalFinanced_DE_CC=").append(checkSumTotalFinanced_DE_CC)
			.append(", checkSumAvailableAmount_DE_CC=").append(checkSumAvailableAmount_DE_CC)

			.append("]").toString();
	}

	public F940Trail() {
		
	}
	
	public F940Trail(F940 f940) {
		this.entryType = "ZZ";
		this.flowReference = "F940";
		this.date = f940.f940Header.date;
		this.time = f940.f940Header.time;

		this.checkSumHardLimits = 0;
		this.checkSumSoftLimits = 0;
		this.checkSumTotalFinanced = 0;
		
		this.checkSumAvailableAmount = 0;
		this.checkSumSubLimits_DE_CC = 0;
		this.checkSumTotalFinanced_DE_CC = 0;
		this.checkSumAvailableAmount_DE_CC = 0;
		
		for(F940Item item : f940.f940Items) {
			this.checkSumHardLimits += item.hardLimitATLAS;
			this.checkSumSoftLimits += item.softLimitATLAS;
			this.checkSumTotalFinanced += item.totalFinanced;

			this.checkSumAvailableAmount += item.amountAvailable;
			this.checkSumSubLimits_DE_CC += item.sublimit_DE_CC;
			this.checkSumTotalFinanced_DE_CC += item.totalFinanced_DE_CC;
			this.checkSumAvailableAmount_DE_CC += item.amountAvailable_DE_CC;
		}
 
	    this.nbEntries = f940.f940Items.size(); 
	}	
}
