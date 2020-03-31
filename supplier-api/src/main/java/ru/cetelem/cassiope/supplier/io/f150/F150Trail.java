package ru.cetelem.cassiope.supplier.io.f150;

public class F150Trail implements F150Entry {
	/*
1) Trailer			The header identifier. Only one HD line in the file, else reject file	Text(2)	“TR” 
2) System Id		The Id of the system										Text(8)	-
3) Location			Country code												Text(2)	“RU”
4) Company code		The code of the company										Text(1)	“6”
5) Sequence number	The sequence number of the file, should always be unique	Number(9)	Auto_increment
6) Number of PTS	Number of PTS records in file								Number(9)	-
	 */
	
	public String entryType;
	public String systemId;
	public String location;
	public String companyCode;
	
	public long sequenceNumber;
	public long nbEntries;

	public F150Trail() {

	}

	public F150Trail(F150 f150) {
		this.entryType = "TR";
		this.systemId = "";
		this.location = "RU";
		this.companyCode = "6";
		
		this.sequenceNumber = f150.f150Header.sequenceNumber;
		this.nbEntries = f150.f150Items.size(); 
	}
}
