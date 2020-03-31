package ru.cetelem.cassiope.supplier.io.f150;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ru.cetelem.cassiope.supplier.model.Car;

public class F150Item implements F150Entry {
	/*
1) Details 			The identifier of the details line in the file			Text(2)	“DT”
2) Location			Country code											Text(2)	“RU”
3) Company code		The code of the company									Text(1)	“6”
4) Dealer code		The dealer code											Text(5)	00000
5) VIN				The VIN of the PTS										Text(17)	-
6) Date				The date of PTS registration							Text(8) YYYYMMDD	-
7) Local DHL code	local DHL code where PTS was registered/stamped/signed 
					(the same as PTS originator code)						Text(4)	“MOW1”
8) eptsNumber		Electronic PTS (Car.eptsNumber)							Text(15)	
	 */

	public String entryType;
	public String location;
	public String companyCode;
	public String dealerCode;

	public String vin;
	public Date date;
	public String localCodeDHL;
	public String eptsNumber;

	public String errorDescr;

	@JsonIgnore
	public Car car;
}
