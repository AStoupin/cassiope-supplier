package ru.cetelem.cassiope.supplier.io.f940;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ru.cetelem.cassiope.supplier.model.Dealer;

/**
 * 
 * This file is dedicated to show Cassiopie the available limits of the dealers in the Xfactor system. 
 * This file is sent to Bless and according to it they will send us the financing request file F120.
 *
 */
public class F940Item implements F940Entry {
	// @formatter:off
	/*
EntryType				Define type of the entry												Alphabetic(2)	"DT"
DealerIDReference-BL	Reference of the dealer the car is sent to - BL reference (Ex.: 00231, 00007)	Alphabetic(5)
HardLimitATLAS			Value of the hard limit in ATLAS												Numeric(13.2)
	If the dealer is blocked – checkbox “Dealer blocked” is activated on the dealer contract page, 
	then the amount is equal to zero,	
SoftLimitATLAS			Value of the soft limit in ATLAS 
	(If the dealer is blocked – checkbox “Dealer blocked” is activated on the dealer contract page, 
	then the amount is equal to zero,)																	Numeric(13.2)
	
TotalFinanced			Total amount already financed from the dealer									Numeric(13.2)
AmountAvailable			Total amount still available for dealer financing. 
	(If the dealer is blocked – checkbox “Dealer blocked” is activated on the dealer contract page, 
	then the amount is equal to zero,)																	Numeric(13.2)
Sublimit_DE_CC			Value of the sublimit for demo and courtesy cars								Numeric(13.2)
TotalFinanced_DE_CC		Total amount of demo and courtesy cars already financed from the dealer			Numeric(13.2)

AmountAvailable_DE_CC	Total amount of demo and courtesy cars still available for dealer financing		Numeric(13.2)
	 */
	// @formatter:on
		
	public String entryType = "DT";
	public String dealerCode;
	public long hardLimitATLAS;
	public long softLimitATLAS;

	public long totalFinanced;
	public long amountAvailable;
	public long sublimit_DE_CC;
	public long totalFinanced_DE_CC;

	public long amountAvailable_DE_CC;

	@Override
	public String toString() {
		return new StringBuilder(128)
		    .append("F940Item [entryType=").append(entryType)
		    .append(", dealerCode=").append(dealerCode)
		    .append(", hardLimitATLAS=").append(hardLimitATLAS)
		    .append(", softLimitATLAS=").append(softLimitATLAS)
		    
		    .append(", totalFinanced=").append(totalFinanced)
		    .append(", amountAvailable=").append(amountAvailable)
		    .append(", sublimit_DE_CC=").append(sublimit_DE_CC)
		    .append(", totalFinanced_DE_CC=").append(totalFinanced_DE_CC)
		    
		    .append(", amountAvailable_DE_CC=").append(amountAvailable_DE_CC)		    
		    .append("]").toString();
	}

	@JsonIgnore
	public Dealer dealer;	
}
